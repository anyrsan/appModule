package com.any.imagelibrary


import android.app.Activity
import android.content.Intent
import com.any.imagelibrary.activity.ImagePickerActivity
import com.any.imagelibrary.loader.ImageLoader
import com.any.imagelibrary.loaderimp.ImageLoadManager
import com.any.imagelibrary.manager.ConfigManager
import com.any.imagelibrary.model.MediaModel
import java.util.*

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
        ConfigManager.getInstance().isOpenCamera = false
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
     * 设置可选视频的最大时长
     */
    fun setVideoMaxDuration(duration: Long): ImagePicker {
        ConfigManager.getInstance().maxDuration = duration
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
    fun setImagePaths(imagePaths: ArrayList<MediaModel>): ImagePicker {
        ConfigManager.getInstance().mediaList = imagePaths
        return mImagePicker
    }

    /**
     * 设置压缩
     */
    fun setIsCompress(isCompress: Boolean): ImagePicker {
        ConfigManager.getInstance().isCompress = isCompress
        return mImagePicker
    }

    /**
     * 设置忽略大小
     */
    fun setIgnoreBy(ignoreBy: Int): ImagePicker {
        ConfigManager.getInstance().ignoreBy = ignoreBy
        return mImagePicker
    }


    /**
     * 启动
     *
     * @param activity
     */
    fun start(activity: Activity, requestCode: Int) {
        val intent = Intent(activity, ImagePickerActivity::class.java)
        //默认注入一个
        if (ConfigManager.getInstance().imageLoader == null) {
            setImageLoader(ImageLoadManager(activity.applicationContext))
        }
        activity.startActivityForResult(intent, requestCode)
    }


    //获取数据
    fun getDataFromIntent(data: Intent?): ArrayList<MediaModel>? {
        return data?.getParcelableArrayListExtra(EXTRA_SELECT_IMAGES)
    }


    companion object {


        const val RESULT_OPEN_CODE = 0x10   //拍照code

        const val RESULT_SELECT_CODE = 0x11 //选择code

        const val EXTRA_SELECT_IMAGES = "selectItems"

        const val REQUEST_SELECT_IMAGES_CODE = 0x01//用于在大图预览页中点击提交按钮标识

        const val REQUEST_CODE_CAPTURE = 0x02//点击拍照标识

        private val mImagePicker by lazy { ImagePicker() }

        fun getInstance(): ImagePicker {
            return mImagePicker
        }

    }

}
