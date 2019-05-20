package com.any.imagelibrary.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import com.any.imagelibrary.R
import com.any.imagelibrary.adapter.ImageFoldersAdapter
import com.any.imagelibrary.model.MediaFolder
import com.any.imagelibrary.utils.Utils

/**
 *
 * @author any
 * @time 2019/05/20 10.41
 * @details
 */
class ImageFolderPopupWindow(var mContext: Context, var mMediaFolderList: List<MediaFolder>) : PopupWindow() {

    private val DEFAULT_IMAGE_FOLDER_SELECT = 0//默认选中文件夹

    private var mRecyclerView: RecyclerView? = null
    private var mImageFoldersAdapter: ImageFoldersAdapter? = null

    init {
        initView()
    }

    /**
     * 初始化布局
     */
    private fun initView() {
        val view = LayoutInflater.from(mContext).inflate(R.layout.imgpk_window_image_folders, null)
        mRecyclerView = view.findViewById(R.id.rv_main_imageFolders)
        mRecyclerView?.layoutManager = LinearLayoutManager(mContext)
        mImageFoldersAdapter = ImageFoldersAdapter(mContext, mMediaFolderList, DEFAULT_IMAGE_FOLDER_SELECT)
        mRecyclerView?.adapter = mImageFoldersAdapter
        initPopupWindow(view)
    }

    /**
     * 初始化PopupWindow的一些属性
     */
    private fun initPopupWindow(view: View) {
        contentView = view
        val screenSize = Utils.getScreenSize(mContext)
        width = screenSize[0]
        height = (screenSize[1] * 0.6).toInt()
        setBackgroundDrawable(ColorDrawable())
        isOutsideTouchable = true
        isFocusable = true
        setTouchInterceptor { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_OUTSIDE) {
                dismiss()
            }
            false
        }
    }

    fun getAdapter(): ImageFoldersAdapter? {
        return mImageFoldersAdapter
    }

}