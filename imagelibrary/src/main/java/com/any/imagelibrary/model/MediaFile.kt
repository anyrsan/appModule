package com.any.imagelibrary.model

/**
 *
 * @author any
 * @time 2019/05/20 10.42
 * @details
 */
class MediaFile {
    private var path: String? = null
    private var mime: String? = null
    private var folderId: Int? = null
    private var folderName: String? = null
    private var duration: Long = 0
    private var dateToken: Long = 0

    fun getPath(): String? {
        return path
    }

    fun setPath(path: String) {
        this.path = path
    }

    fun getMime(): String? {
        return mime
    }

    fun setMime(mime: String) {
        this.mime = mime
    }

    fun getFolderId(): Int? {
        return folderId
    }

    fun setFolderId(folderId: Int?) {
        this.folderId = folderId
    }

    fun getFolderName(): String? {
        return folderName
    }

    fun setFolderName(folderName: String) {
        this.folderName = folderName
    }

    fun getDuration(): Long {
        return duration
    }

    fun setDuration(duration: Long) {
        this.duration = duration
    }

    fun getDateToken(): Long {
        return dateToken
    }

    fun setDateToken(dateToken: Long) {
        this.dateToken = dateToken
    }
}