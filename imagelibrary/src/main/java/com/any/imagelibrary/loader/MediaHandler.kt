package com.any.imagelibrary.loader

import android.content.Context
import com.any.imagelibrary.R
import com.any.imagelibrary.model.MediaFile
import com.any.imagelibrary.model.MediaFolder
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * 媒体处理类（对扫描出来的图片、视频做对应聚类处理）
 * Create by: chenWei.li
 * Date: 2019/1/22
 * Time: 1:17 AM
 * Email: lichenwei.me@foxmail.com
 */
object MediaHandler {

    val ALL_MEDIA_FOLDER = -1//全部媒体
    val ALL_VIDEO_FOLDER = -2//全部视频

    /**
     * 对查询到的图片进行聚类（相册分类）
     *
     * @param context
     * @param imageFileList
     * @return
     */
    fun getImageFolder(context: Context, imageFileList: ArrayList<MediaFile>): List<MediaFolder> {
        return getMediaFolder(context, imageFileList, null)
    }


    /**
     * 对查询到的视频进行聚类（相册分类）
     *
     * @param context
     * @param imageFileList
     * @return
     */
    fun getVideoFolder(context: Context, imageFileList: ArrayList<MediaFile>): List<MediaFolder> {
        return getMediaFolder(context, null, imageFileList)
    }


    /**
     * 对查询到的图片和视频进行聚类（相册分类）
     *
     * @param context
     * @param imageFileList
     * @param videoFileList
     * @return
     */
    fun getMediaFolder(
        context: Context,
        imageFileList: ArrayList<MediaFile>?,
        videoFileList: ArrayList<MediaFile>?
    ): List<MediaFolder> {

        //根据媒体所在文件夹Id进行聚类（相册）
        val mediaFolderMap = HashMap<Int, MediaFolder>()

        //全部图片、视频文件
        val mediaFileList = ArrayList<MediaFile>()
        if (imageFileList != null) {
            mediaFileList.addAll(imageFileList)
        }
        if (videoFileList != null) {
            mediaFileList.addAll(videoFileList)
        }

        //对媒体数据进行排序
      val tempList =  mediaFileList.sortedByDescending {
            it.getDateToken()
        }

        //排序后的
        val mList = ArrayList<MediaFile>()
        mList.addAll(tempList)

        //全部图片或视频
        if (mList.isNotEmpty()) {
            val allMediaFolder = MediaFolder(
                ALL_MEDIA_FOLDER,
                context.getString(R.string.imgpk_all_media),
                mediaFileList[0].getPath(),
                mediaFileList = mList
            )
            mediaFolderMap[ALL_MEDIA_FOLDER] = allMediaFolder
        }

        //全部视频
        if (videoFileList != null && videoFileList.isNotEmpty()) {
            val allVideoFolder = MediaFolder(
                ALL_VIDEO_FOLDER,
                context.getString(R.string.imgpk_all_video),
                videoFileList[0].getPath(),
                mediaFileList = videoFileList
            )
            mediaFolderMap[ALL_VIDEO_FOLDER] = allVideoFolder
        }

        //对图片进行文件夹分类
        if (imageFileList != null && imageFileList.isNotEmpty()) {
            val size = imageFileList.size
            //添加其他的图片文件夹
            for (i in 0 until size) {
                val mediaFile = imageFileList[i]
                val imageFolderId = mediaFile.getFolderId()!!
                var mediaFolder = mediaFolderMap[imageFolderId]
                if (mediaFolder == null) {
                    mediaFolder = MediaFolder(
                        imageFolderId,
                        mediaFile.getFolderName(),
                        mediaFile.getPath(),
                        mediaFileList = ArrayList()
                    )
                }
                val imageList = mediaFolder!!.mediaFileList
                imageList!!.add(mediaFile)
                mediaFolder!!.mediaFileList = imageList
                mediaFolderMap[imageFolderId] = mediaFolder
            }
        }

        //整理聚类数据
        var mediaFolderList = ArrayList<MediaFolder>()

        //转换数据
        mediaFolderMap.values.forEach {
            mediaFolderList.add(it)
        }

        //按照图片文件夹的数量排序
        return mediaFolderList.sortedByDescending {
           it.mediaFileList?.size
         }
    }

}
