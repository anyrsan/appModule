package com.any.netlibrary.net

import android.content.Context
import android.net.ConnectivityManager
import retrofit2.HttpException

/**
 * @author any
 * @date 2017/11/30
 */
class ExceptionHandle {


    /**
     * 约定异常
     */
    object ERROR {
        /**
         * 未知错误
         */
        val UNKNOWN = 10000
        /**
         * 解析错误
         */
        val PARSE_ERROR = 10001
        /**
         * 网络错误
         */
        val NETWORD_ERROR = 10002
        /**
         * 协议出错
         */
        val HTTP_ERROR = 10003

        /**
         * 证书出错
         */
        val SSL_ERROR = 10005

        /**
         * 连接超时
         */
        val TIMEOUT_ERROR = 10006

        /**
         * 无网络
         */
        val NONET = 10007

        //toke失效
        val TOKENLOSE = 10008
    }

    open class ResponeThrowable : Exception {
        var code: Int? = 0
        var msg: String? = null

        constructor(msg: String?, code: Int?) {
            this.msg = msg
            this.code = code
        }
    }

    inner class ServerException : RuntimeException() {
        var code: Int = 0
        var msg: String? = null
    }


    //自定义异常
    class CustomException(msgg: String?,codee: Int?) : ResponeThrowable(msgg,codee)


    companion object {

        private val UNAUTHORIZED = 401
        private val FORBIDDEN = 403
        private val NOT_FOUND = 404
        private val METHED_NOT_ALLOWED = 405
        private val REQUEST_TIMEOUT = 408
        private val INTERNAL_SERVER_ERROR = 500
        private val BAD_GATEWAY = 502
        private val SERVICE_UNAVAILABLE = 503
        private val GATEWAY_TIMEOUT = 504

        val ACTION_LOGIN = "com.need.login"

        /**
         * 获取当前是否联网
         *
         * @param context
         * @return
         */
        fun isNetWorkAvailable(context: Context): Boolean {
            var isAvailable = false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null && networkInfo.isAvailable) {   //??? todo isAvailable
                isAvailable = true
            }
            return isAvailable
        }


        //简单处理异常
        fun handleException(e: Throwable, context: Context?): ResponeThrowable {
            val ex: ResponeThrowable
            if (context != null && !isNetWorkAvailable(context)) {
                ex = ResponeThrowable(
                    "当前手机无网络",
                    ERROR.NONET
                )
                return ex
            } else if (e is CustomException) {
                // 处理自定义异常 todo
                return ResponeThrowable(e.msg, e.code)
            } else if (e is HttpException) {
                ex = ResponeThrowable(
                    e.message,
                    ERROR.HTTP_ERROR
                )
                return ex
            } else if(e is ServerException){
                ex = ResponeThrowable(e.message, e.code)
                return ex
            } else {
                ex = ResponeThrowable(
                    "请求失败",
                    ERROR.UNKNOWN
                )
                ex.msg = "未知错误"
                ex.printStackTrace()
                return ex
            }
        }


//        fun handleException(e: Throwable, context: Context?): ResponeThrowable {
//            val ex: ResponeThrowable
//            if (context != null && !isNetWorkAvailable(context)) {
//                ex = ResponeThrowable("当前手机无网络", ERROR.NONET)
//                return ex
//            } else if (e is CustomException) {  //自定义异常
//                //　这种也是有问题的登录情况
//                if (e.code == 5024) {
//                    LocalBroadcastManager.getInstance(context!!).sendBroadcast(Intent(ACTION_LOGIN))
//                }
//                return ResponeThrowable(e.msg, e.code)
//            } else if (e is HttpException) {
//                ex = ResponeThrowable(e.message, ERROR.HTTP_ERROR)
//                when (e.code()) {
//                    UNAUTHORIZED -> {
//                        ex.code = ERROR.TOKENLOSE
//                        ex.msg = "登录过期"
//                        LocalBroadcastManager.getInstance(context!!).sendBroadcast(Intent(ACTION_LOGIN))
//                    }
//                    FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE, METHED_NOT_ALLOWED -> ex.msg =
//                        "服务器开小差了，稍后重试"
//                    else -> ex.msg = "服务器开小差了，稍后重试"
//                }
//                return ex
//            } else if (e is ServerException) {
//                ex = ResponeThrowable(e.message, e.code)
//                return ex
//            } else if (e is JSONException || e is ParseException) {
//                ex = ResponeThrowable("解析错误", ERROR.PARSE_ERROR)
//                ex.msg = "解析错误"
//                return ex
//            } else if (e is ConnectException) {
//                ex = ResponeThrowable("连接失败", ERROR.NETWORD_ERROR)
//                ex.msg = "连接失败"
//                return ex
//            } else if (e is javax.net.ssl.SSLHandshakeException) {
//                ex = ResponeThrowable("证书验证失败", ERROR.SSL_ERROR)
//                ex.msg = "证书验证失败"
//                return ex
//            } else if (e is ConnectTimeoutException) {
//                ex = ResponeThrowable("连接超时", ERROR.TIMEOUT_ERROR)
//                ex.msg = "连接超时"
//                return ex
//            } else if (e is java.net.SocketTimeoutException) {
//                ex = ResponeThrowable("连接超时", ERROR.TIMEOUT_ERROR)
//                ex.msg = "连接超时"
//                return ex
//            } else {   //java.io.EOFException: \n not found: limit=1 content=0d…
//                ex = ResponeThrowable( "未知错误", ERROR.UNKNOWN)
//                ex.msg = "未知错误"
//                ex.printStackTrace()
//                return ex
//            }
//        }
    }

}

