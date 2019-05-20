package com.any.imagelibrary.task

import android.content.Context
import com.any.imagelibrary.listener.MediaLoadCallback
import com.any.imagelibrary.loader.ImageScanner
import com.any.imagelibrary.loader.MediaHandler
import com.any.imagelibrary.loader.VideoScanner
import com.any.imagelibrary.model.MediaFile
import java.util.*

/**
 * 媒体库扫描任务（图片、视频）
 * Create by: chenWei.li
 * Date: 2018/8/25
 * Time: 下午12:31
 * Email: lichenwei.me@foxmail.com
 */
class MediaLoadTask(private val mContext: Context, private val mMediaLoadCallback: MediaLoadCallback?) : Runnable {
    private val mImageScanner: ImageScanner?
    private val mVideoScanner: VideoScanner?

    init {
        mImageScanner = ImageScanner(mContext)
        mVideoScanner = VideoScanner(mContext)
    }

    override fun run() {
        //存放所有照片
        var imageFileList = ArrayList<MediaFile>()
        //存放所有视频
        var videoFileList = ArrayList<MediaFile>()

        if (mImageScanner != null) {
            imageFileList = mImageScanner.queryMedia()
        }
        if (mVideoScanner != null) {
            videoFileList = mVideoScanner.queryMedia()
        }

        mMediaLoadCallback?.loadMediaSuccess(MediaHandler.getMediaFolder(mContext, imageFileList, videoFileList))

    }

}
