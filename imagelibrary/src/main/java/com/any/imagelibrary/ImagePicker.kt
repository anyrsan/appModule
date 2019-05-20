package com.any.imagelibrary

import android.app.Activity
import android.content.Intent
import com.any.imagelibrary.activity.ImagePickerActivity
import com.any.imagelibrary.loader.ImageLoader
import com.any.imagelibrary.manager.ConfigManager


import java.util.ArrayList

/**
 * 统一调用入口
 * Create by: chenWei.li
 * Date: 2018/8/26
 * Time: 下午6:31
 * Email: lichenwei.me@foxmail.com
 */
class ImagePicker private constructor() {


    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    fun setTitle(title: String): ImagePicker {
        ConfigManager.getInstance().isOpenCamera =false
        ConfigManager.getInstance().title = title
        return mImagePicker
    }


    fun setOpenCamera(isOpenCamera: Boolean): ImagePicker {
        ConfigManager.getInstance().isOpenCamera = isOpenCamera
        return mImagePicker
    }

    /**
     * 是否支持相机
     *
     * @param showCamera
     * @return
     */
    fun showCamera(showCamera: Boolean): ImagePicker {
        ConfigManager.getInstance().isShowCamera = showCamera
        return mImagePicker
    }

    /**
     * 是否展示图片
     *
     * @param showImage
     * @return
     */
    fun showImage(showImage: Boolean): ImagePicker {
        ConfigManager.getInstance().isShowImage = showImage
        return mImagePicker
    }

    /**
     * 是否展示视频
     *
     * @param showVideo
     * @return
     */
    fun showVideo(showVideo: Boolean): ImagePicker {
        ConfigManager.getInstance().isShowVideo = showVideo
        return mImagePicker
    }


    /**
     * 图片最大选择数
     *
     * @param maxCount
     * @return
     */
    fun setMaxCount(maxCount: Int): ImagePicker {
        ConfigManager.getInstance().maxCount = maxCount
        return mImagePicker
    }

    /**
     * 设置单类型选择（只能选图片或者视频）
     *
     * @param isSingleType
     * @return
     */
    fun setSingleType(isSingleType: Boolean): ImagePicker {
        ConfigManager.getInstance().isSingleType = isSingleType
        return mImagePicker
    }


    /**
     * 设置图片加载器
     *
     * @param imageLoader
     * @return
     */
    fun setImageLoader(imageLoader: ImageLoader): ImagePicker {
        ConfigManager.getInstance().imageLoader = imageLoader
        return mImagePicker
    }

    /**
     * 设置图片选择历史记录
     *
     * @param imagePaths
     * @return
     */
    fun setImagePaths(imagePaths: ArrayList<String>): ImagePicker {
        ConfigManager.getInstance().imagePaths = imagePaths
        return mImagePicker
    }

    /**
     * 启动
     *
     * @param activity
     */
    fun start(activity: Activity, requestCode: Int) {
        val intent = Intent(activity, ImagePickerActivity::class.java)
        activity.startActivityForResult(intent, requestCode)
    }

    companion object {


        val RESULT_OPEN_CODE = 0x10   //拍照code

        val RESULT_SELECT_CODE = 0x11 //选择code


        val EXTRA_SELECT_IMAGES = "selectItems"


        private val mImagePicker by lazy { ImagePicker() }

        fun getInstance():ImagePicker {
            return  mImagePicker
        }

    }

}
