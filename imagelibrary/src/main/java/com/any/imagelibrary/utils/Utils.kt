package com.any.imagelibrary.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * 屏幕相关工具类
 * Create by: chenWei.li
 * Date: 2018/8/25
 * Time: 上午1:53
 * Email: lichenwei.me@foxmail.com
 */
object Utils {

    /**
     * 获取屏幕的宽和高
     *
     * @param context
     * @return
     */
    fun getScreenSize(context: Context): IntArray {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return intArrayOf(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    /**
     * 获取图片格式化时间
     *
     * @param timestamp
     * @return
     */
    fun getImageTime(timestamp: Long): String {
        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = Date()
        val imageCalendar = Calendar.getInstance()
        imageCalendar.timeInMillis = timestamp
        if (currentCalendar.get(Calendar.DAY_OF_YEAR) == imageCalendar.get(Calendar.DAY_OF_YEAR) && currentCalendar.get(
                Calendar.YEAR
            ) == imageCalendar.get(Calendar.YEAR)
        ) {
            return "今天"
        } else if (currentCalendar.get(Calendar.WEEK_OF_YEAR) == imageCalendar.get(Calendar.WEEK_OF_YEAR) && currentCalendar.get(
                Calendar.YEAR
            ) == imageCalendar.get(Calendar.YEAR)
        ) {
            return "本周"
        } else if (currentCalendar.get(Calendar.MONTH) == imageCalendar.get(Calendar.MONTH) && currentCalendar.get(
                Calendar.YEAR
            ) == imageCalendar.get(Calendar.YEAR)
        ) {
            return "本月"
        } else {
            val date = Date(timestamp)
            val sdf = SimpleDateFormat("yyyy/MM")
            return sdf.format(date)
        }
    }

    /**
     * 获取视频时长（格式化）
     *
     * @param timestamp
     * @return
     */
    fun getVideoDuration(timestamp: Long): String {
        if (timestamp < 1000) {
            return "00:01"
        }
        val date = Date(timestamp)
        val simpleDateFormat = SimpleDateFormat("mm:ss")
        return simpleDateFormat.format(date)
    }

}
