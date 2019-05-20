package com.any.imagelibrary.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.any.imagelibrary.manager.ConfigManager
import com.any.imagelibrary.model.MediaFile
import com.any.imagelibrary.widget.PinchImageView


import java.util.LinkedList

/**
 * 大图浏览适配器（并不是比较好的方案，后期会用RecyclerView来实现）
 * Create by: chenWei.li
 * Date: 2018/8/30
 * Time: 上午12:57
 * Email: lichenwei.me@foxmail.com
 */
class ImagePreViewAdapter(private val mContext: Context, private val mMediaFileList: List<MediaFile>?) :
    PagerAdapter() {

    internal var viewCache = LinkedList<PinchImageView>()

    override fun getCount(): Int {
        return mMediaFileList?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView: PinchImageView
        if (viewCache.size > 0) {
            imageView = viewCache.remove()
            imageView.reset()
        } else {
            imageView = PinchImageView(mContext)
        }
        try {
            ConfigManager.getInstance().imageLoader!!.loadPreImage(imageView, mMediaFileList!![position].getPath()!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val imageView = `object` as PinchImageView
        container.removeView(imageView)
        viewCache.add(imageView)
    }
}
