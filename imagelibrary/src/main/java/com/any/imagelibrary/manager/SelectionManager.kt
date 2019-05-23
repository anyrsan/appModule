package com.any.imagelibrary.manager

import com.any.imagelibrary.model.MediaModel
import com.any.imagelibrary.utils.MediaFileUtil

/**
 * 媒体选择集合管理类
 * Create by: chenWei.li
 * Date: 2018/8/23
 * Time: 上午1:19
 * Email: lichenwei.me@foxmail.com
 */
class SelectionManager private constructor() {

    /**
     * 获取当前所选图片集合path
     *
     * @return
     */
    val selectMap = mutableMapOf<String, MediaModel>()

    /**
     * 获取当前设置最大选择数
     *
     * @return
     */
    /**
     * 设置最大选择数
     *
     * @param maxCount
     */
    var maxCount = 1

    /**
     * 是否还可以继续选择图片
     *
     * @return
     */
    val isCanChoose: Boolean
        get() = selectMap.size < maxCount

    /**
     * 添加/移除图片到选择集合
     *
     * @param imagePath
     * @return
     */
    fun addImageToSelectList(imagePath: String?): Boolean {
        return imagePath?.let {
            if (selectMap.containsKey(it)) {
                selectMap.remove(it)
                true
            } else {
                if (isCanChoose) {
                    selectMap[it] = MediaModel(it)
                    true
                } else {
                    false
                }
            }
        } ?: false
    }

    /**
     * 添加图片到选择集合
     *
     * @param imagePaths
     */
    fun addImagePathsToSelectList(imagePaths: List<MediaModel>?) {
        //处理数据
        imagePaths?.forEach {
            it.path?.let { path ->
                if (isCanChoose && !isImageSelect(path)) {
                    selectMap[path] = it
                }
            }
        }
    }

    //如果为视频
    fun checkPathVideoFileType(): Boolean {
        if (selectMap.isEmpty()) return false
        val path = selectMap.keys.iterator().next()
        return MediaFileUtil.isVideoFileType(path)
    }


    /**
     * 判断当前图片是否被选择
     *
     * @param imagePath
     * @return
     */
    fun isImageSelect(imagePath: String?): Boolean {
        return selectMap.containsKey(imagePath)
    }

    /**
     * 清除已选图片
     */
    fun removeAll() {
        selectMap.clear()
    }

    companion object {

        private val mSelectionManager by lazy { SelectionManager() }

        fun getInstance(): SelectionManager {
            return mSelectionManager
        }
    }

}
