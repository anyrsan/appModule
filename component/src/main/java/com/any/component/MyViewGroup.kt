package com.any.component

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.util.Log
import android.support.v4.view.MotionEventCompat
import android.view.*
import android.view.MotionEvent


/**
 *
 * @author any
 * @time 2019/06/10 10.29
 * @details
 */
class MyViewGroup : NestedScrollView {  //NestedScrollView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    // 头 view
    private lateinit var topView: View

    //主view
    private lateinit var contentView: View

    //头部控制的高度
    private var topHeight = 0

    private var mCurrTop = 0

    private var interceptDrag = false

    private val INVALID_POINTER = -1
    private var mTouchSlop: Int = 0
    private var mInitialDownY: Float = 0.toFloat()
    private var mInitialDownX: Float = 0.toFloat()
    private var mIsBeingDragged: Boolean = false
    private var mActivePointerId = INVALID_POINTER

    private val damping = 0.75f

    //处理子view     [ LinearLayout ]
    override fun onFinishInflate() {
        super.onFinishInflate()
        val view = getChildAt(0) as ViewGroup
        topView = view.getChildAt(0)
        contentView = view.getChildAt(1)

        topView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // view的布局
                topHeight = topView.measuredHeight
                Log.e("msg", "topHeight...$topHeight")
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })

        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (isStick()) {
            mIsBeingDragged = false
            return super.onInterceptTouchEvent(ev)
        }
        when (MotionEventCompat.getActionMasked(ev)) {
            MotionEvent.ACTION_DOWN -> {
                interceptDrag = false
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0)
                mIsBeingDragged = false
                val initialDownY = getMotionEventY(ev, mActivePointerId)
                val initialDownX = getMotionEventX(ev, mActivePointerId)
                if (initialDownY == -1f || initialDownX == -1f || isStick()) {
                    return false
                }
                mInitialDownY = initialDownY
                mInitialDownX = initialDownX
            }
            MotionEvent.ACTION_MOVE -> {
                if (mActivePointerId == INVALID_POINTER) {
                    return false
                }
                val y = getMotionEventY(ev, mActivePointerId)
                val x = getMotionEventX(ev, mActivePointerId)
                if (y == -1f || x == -1f || isStick()) {
                    return false
                }
                val xDiff = mInitialDownX - x
                val yDiff = mInitialDownY - y
                //从这里开始处理
                if (Math.abs(xDiff) > mTouchSlop || Math.abs(xDiff) > Math.abs(yDiff) || interceptDrag) {
                    return false
                }

                if (!mIsBeingDragged) {
                    mInitialDownY = y
                    mIsBeingDragged = true
                }
            }
            MotionEventCompat.ACTION_POINTER_UP -> onSecondaryPointerUp(ev)
            MotionEvent.ACTION_UP -> {
                mIsBeingDragged = false
                interceptDrag = mIsBeingDragged
                mActivePointerId = INVALID_POINTER
            }
            MotionEvent.ACTION_CANCEL -> {
                mIsBeingDragged = false
                mActivePointerId = INVALID_POINTER
            }
        }
        return mIsBeingDragged
    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val action = MotionEventCompat.getActionMasked(ev)
        var pointerIndex: Int
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0)
                mIsBeingDragged = true
            }
            MotionEvent.ACTION_MOVE -> {
                var initialDownY = getMotionEventY(ev, mActivePointerId)
                if (initialDownY == -1f) {
                    return false
                }
                //降低滑动速率
                val offset = ((initialDownY - mInitialDownY) * damping).toInt()
                mInitialDownY = initialDownY
                // 不能处理时,应该返回false
                if (mIsBeingDragged) {
                    val isTrue = drag(offset)
                    if (!isTrue) {
                        resetMotionEvent(ev)
                    }
                }
            }
            MotionEventCompat.ACTION_POINTER_DOWN -> {
                pointerIndex = MotionEventCompat.getActionIndex(ev)
                if (pointerIndex < 0) {
                    return false
                }
                mActivePointerId = MotionEventCompat.getPointerId(ev, pointerIndex)
                var initialDownY = getMotionEventY(ev, mActivePointerId)
                if (initialDownY == -1f) {
                    return false
                }
                mInitialDownY = initialDownY
                mIsBeingDragged = true
            }
            MotionEvent.ACTION_UP -> {
                pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId)
                if (pointerIndex < 0) {
                    return false
                }
                mIsBeingDragged = false
                mActivePointerId = INVALID_POINTER
                return false
            }
        }
        return true
    }


    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, Math.max(0, Math.min(topHeight, y)))  // 0--topHeight
    }

    private fun drag(offset: Int): Boolean {
        val tempOffset = scrollY - mCurrTop
        scrollBy(0, -offset)
        mCurrTop = scrollY
        Log.e("msg", "....mCurrTop = $mCurrTop   tempOffset=$tempOffset   offset=$offset")
        return !isStick()
    }


    private fun isStick(): Boolean {
        val isStick = mCurrTop == topHeight
        Log.e("msg", "isStick....$isStick")
        return isStick
    }


    private fun getMotionEventY(ev: MotionEvent, activePointerId: Int): Float {
        val index = MotionEventCompat.findPointerIndex(ev, activePointerId)
        return if (index < 0) {
            -1f
        } else MotionEventCompat.getY(ev, index)
    }

    private fun getMotionEventX(ev: MotionEvent, activePointerId: Int): Float {
        val index = MotionEventCompat.findPointerIndex(ev, activePointerId)
        return if (index < 0) {
            -1f
        } else MotionEventCompat.getX(ev, index)
    }

    private fun resetMotionEvent(ev: MotionEvent): MotionEvent {
        val action = ev.action
        ev.action = MotionEvent.ACTION_CANCEL
        super.dispatchTouchEvent(ev)
        ev.action = MotionEvent.ACTION_DOWN
        super.dispatchTouchEvent(ev)
        ev.action = action
        return ev
    }

    private fun onSecondaryPointerUp(ev: MotionEvent) {
        val pointerIndex = MotionEventCompat.getActionIndex(ev)
        val pointerId = MotionEventCompat.getPointerId(ev, pointerIndex)
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            val newPointerIndex = if (pointerIndex == 0) 1 else 0
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex)
        }
    }

}