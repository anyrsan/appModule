package com.any.imagelibrary.loader

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.any.imagelibrary.model.MediaFile

/**
 * 媒体库扫描类(视频)
 * Create by: chenWei.li
 * Date: 2018/8/21
 * Time: 上午1:01
 * Email: lichenwei.me@foxmail.com
 */
class VideoScanner(private val mContext: Context) : AbsMediaScanner<MediaFile>(mContext) {
    override val scanUri: Uri
        get() = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    override val projection: Array<String>
        get() = arrayOf(
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATE_TAKEN
        )
    override val selection: String?
        get() = null
    override val selectionArgs: Array<String>?
        get() = null
    override val order: String
        get() = MediaStore.Video.Media.DATE_TAKEN + " desc"


    /**
     * 构建媒体对象
     *
     * @param cursor
     * @return
     */
    override fun parse(cursor: Cursor): MediaFile {

        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
        val mime = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE))
        val folderId = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
        val folderName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
        val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
        val dateToken = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN))

        val mediaFile = MediaFile()
        mediaFile.setPath(path)
        mediaFile.setMime(mime)
        mediaFile.setFolderId(folderId)
        mediaFile.setFolderName(folderName)
        mediaFile.setDuration(duration)
        mediaFile.setDateToken(dateToken)

        return mediaFile
    }

    companion object {

        val ALL_IMAGES_FOLDER = -1//全部图片
    }


}
