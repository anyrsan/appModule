package com.any.imagelibrary.model

import java.util.ArrayList

/**
 *
 * @author any
 * @time 2019/05/20 10.44
 * @details
 */
data class MediaFolder(
    var folderId: Int,
    var folderName: String?,
    var folderCover: String?,
    var isCheck: Boolean = false,
    var mediaFileList: ArrayList<MediaFile>?
)