package com.any.imagelibrary.utils

import com.any.imagelibrary.model.MediaFile

import java.util.ArrayList

/**
 * 数据保存类
 * （随着Android版本的提高，对Intent传递数据的大小也做了不同的限制，在一些高版本或者低配机型上可能会发生
 * android.os.TransactionTooLargeException: data parcel size xxxx bytes异常，故用此方案适配）
 * Create by: chenWei.li
 * Date: 2019/1/24
 * Time: 12:38 AM
 * Email: lichenwei.me@foxmail.com
 */
class DataUtil private constructor() {

    var mediaData = mutableListOf<MediaFile>()

    companion object {

        private val dataUtil by lazy { DataUtil() }

        fun getInstance(): DataUtil {
            return dataUtil
        }
    }

    fun removeAll() {
        mediaData.clear()
    }

}
