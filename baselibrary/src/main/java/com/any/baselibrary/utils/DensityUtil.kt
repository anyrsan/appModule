package com.any.baselibrary.utils

import android.content.Context
import android.content.res.Resources

/**
 * anyrsan
 */
object DensityUtil {

    fun dp2px(res: Resources, paramFloat: Float): Int {
        return (0.5f + paramFloat * res.displayMetrics.density).toInt()
    }

    fun dip2px(paramContext: Context, paramFloat: Float): Int {
        return (0.5f + paramFloat * paramContext.resources.displayMetrics.density).toInt()
    }

    fun getHeightInDp(paramContext: Context): Int {
        return px2dip(
            paramContext, paramContext.resources
                .displayMetrics.heightPixels.toFloat()
        )
    }

    fun getHeightInPx(paramContext: Context): Int {
        return paramContext.resources.displayMetrics.heightPixels
    }

    fun getWidthInDp(paramContext: Context): Int {
        return px2dip(
            paramContext, paramContext.resources
                .displayMetrics.widthPixels.toFloat()
        )
    }

    fun getWidthInPx(paramContext: Context): Int {
        return paramContext.resources.displayMetrics.widthPixels
    }

    fun px2dip(paramContext: Context, paramFloat: Float): Int {
        return (0.5f + paramFloat / paramContext.resources.displayMetrics.density).toInt()
    }

    fun px2sp(paramContext: Context, paramFloat: Float): Int {
        return (0.5f + paramFloat / paramContext.resources.displayMetrics.density).toInt()
    }

    fun sp2px(paramContext: Context, paramFloat: Float): Int {
        return (0.5f + paramFloat * paramContext.resources.displayMetrics.scaledDensity).toInt()
    }
}