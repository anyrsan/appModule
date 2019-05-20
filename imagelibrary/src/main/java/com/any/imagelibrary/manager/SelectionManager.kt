package com.any.imagelibrary.manager

import java.util.ArrayList

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
    val selectPaths = ArrayList<String>()

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
        get() = selectPaths.size < maxCount

    /**
     * 添加/移除图片到选择集合
     *
     * @param imagePath
     * @return
     */
    fun addImageToSelectList(imagePath: String): Boolean {
        return if (selectPaths.contains(imagePath)) {
            selectPaths.remove(imagePath)
        } else {
            if (selectPaths.size < maxCount) {
                selectPaths.add(imagePath)
            } else {
                false
            }
        }
    }

    /**
     * 添加图片到选择集合
     *
     * @param imagePaths
     */
    fun addImagePathsToSelectList(imagePaths: List<String>?) {
        if (imagePaths != null) {
            for (i in imagePaths.indices) {
                val imagePath = imagePaths[i]
                if (!selectPaths.contains(imagePath) && selectPaths.size < maxCount) {
                    selectPaths.add(imagePath)
                }
            }
        }
    }


    /**
     * 判断当前图片是否被选择
     *
     * @param imagePath
     * @return
     */
    fun isImageSelect(imagePath: String?): Boolean {
        return selectPaths.contains(imagePath)
    }

    /**
     * 清除已选图片
     */
    fun removeAll() {
        selectPaths.clear()
    }

    companion object {

        private val mSelectionManager by lazy { SelectionManager() }

        fun getInstance(): SelectionManager {
            return mSelectionManager
        }
    }

}
