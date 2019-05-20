package com.any.imagelibrary.loader

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.any.imagelibrary.model.MediaFile

/**
 * 媒体库扫描类(图片)
 * Create by: chenWei.li
 * Date: 2018/8/21
 * Time: 上午1:01
 * Email: lichenwei.me@foxmail.com
 */
class ImageScanner(context: Context) : AbsMediaScanner<MediaFile>(context) {
    override val scanUri: Uri
        get() = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    override val projection: Array<String>
        get() = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN
        )
    override val selection: String?
        get() = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?" + " or " + MediaStore.Images.Media.MIME_TYPE + "=?"
    override val selectionArgs: Array<String>?
        get() = arrayOf("image/jpeg", "image/png", "image/gif")
    override val order: String
        get() = MediaStore.Images.Media.DATE_TAKEN + " desc"

    /**
     * 构建媒体对象
     *
     * @param cursor
     * @return
     */
    override fun parse(cursor: Cursor): MediaFile {

        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        val mime = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))
        val folderId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
        val folderName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
        val dateToken = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN))

        val mediaFile = MediaFile()
        mediaFile.setPath(path)
        mediaFile.setMime(mime)
        mediaFile.setFolderId(folderId)
        mediaFile.setFolderName(folderName)
        mediaFile.setDateToken(dateToken)

        return mediaFile
    }


}
