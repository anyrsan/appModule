package com.any.imagelibrary.manager


import com.any.imagelibrary.loader.ImageLoader
import com.any.imagelibrary.model.MediaModel

import java.util.ArrayList

/**
 * 统一配置管理类
 * Create by: chenWei.li
 * Date: 2019/1/23
 * Time: 10:32 AM
 * Email: lichenwei.me@foxmail.com
 */
class ConfigManager private constructor() {

    var title: String? = null//标题
    var isShowCamera: Boolean = false//是否显示拍照Item，默认不显示
    var isShowImage = true//是否显示图片，默认显示
    var isShowVideo = true//是否显示视频，默认显示
    var selectionMode = SELECT_MODE_SINGLE//选择模式，默认单选
    var maxCount = 1
        set(maxCount) {
            if (maxCount > 1) {
                selectionMode = SELECT_MODE_MULTI
            }
            field = maxCount
        }//最大选择数量，默认为1
    var isSingleType: Boolean = false//是否只支持选单类型（图片或者视频）
    var mediaList: ArrayList<MediaModel>? = null//上一次选择的图片地址集合

    var isOpenCamera: Boolean = false //是不是直接打开拍照

    var imageLoader: ImageLoader? = null
        @Throws(Exception::class)
        get() {
            if (field == null) {
                throw Exception("imageLoader is null")
            }
            return field
        }

    //duration
    var maxDuration: Long = Long.MAX_VALUE

    //默认启动压缩
    var isCompress = true
    //不处理100kb的图
    var ignoreBy = 100

    companion object {

        const val SELECT_MODE_SINGLE = 0
        const val SELECT_MODE_MULTI = 1

        private val mConfigManager by lazy { ConfigManager() }

        fun getInstance(): ConfigManager {
            return mConfigManager
        }
    }
}
