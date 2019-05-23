package com.any.baselibrary

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.any.baselibrary.utils.DensityUtil

/**
 * User: any
 * Date: 2019/4/15
 */
object CustomToast {


    fun showMsg(context: Context, message: String) {
        message(false, message, context)
    }

    //Top显示
    fun showTopMsg(context: Context,message: String){
        messageTop(message,context)
    }


    private fun message(isLong: Boolean, message: String, context: Context) {
        //加载Toast布局
        val toastRoot = LayoutInflater.from(context).inflate(R.layout.view_toast, null)
        //初始化布局控件
        val mTextView: TextView = toastRoot.findViewById(R.id.txt_toast)
        //为控件设置属性
        mTextView.text = message
        //Toast的初始化
        val toastStart = Toast(context.applicationContext)
        //获取屏幕高度
        toastStart.setGravity(Gravity.CENTER, 0, 0)
        toastStart.duration = if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        toastStart.view = toastRoot
        toastStart.show()
    }



    private fun messageTop(message: String, context: Context) {
        //加载Toast布局
        val toastRoot = LayoutInflater.from(context).inflate(R.layout.view_toast, null)
        //初始化布局控件
        val mTextView: TextView = toastRoot.findViewById(R.id.txt_toast)
        //为控件设置属性
        mTextView.text = message
        //Toast的初始化
        val toastStart = Toast(context.applicationContext)
        //获取屏幕高度
        toastStart.setGravity(Gravity.TOP, 0, DensityUtil.dip2px(context,70f))
        toastStart.duration = Toast.LENGTH_SHORT
        toastStart.view = toastRoot
        toastStart.show()
    }
}
