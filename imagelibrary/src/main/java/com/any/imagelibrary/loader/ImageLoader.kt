package com.any.imagelibrary.loader

import android.widget.ImageView

import java.io.Serializable

/**
 * 开放图片加载接口
 * Create by: chenWei.li
 * Date: 2018/8/30
 * Time: 下午11:07
 * Email: lichenwei.me@foxmail.com
 */
interface ImageLoader : Serializable {

    /**
     * 缩略图加载方案
     *
     * @param imageView
     * @param imagePath
     */
    fun loadImage(imageView: ImageView, imagePath: String?)

    /**
     * 大图加载方案
     *
     * @param imageView
     * @param imagePath
     */
    fun loadPreImage(imageView: ImageView, imagePath: String)


    /**
     * 视频播放方案
     *
     * @param imageView
     * @param path
     */
    //    void loadVideoPlay(ImageView imageView, String path);

    /**
     * 缓存管理
     */
    fun clearMemoryCache()

}
