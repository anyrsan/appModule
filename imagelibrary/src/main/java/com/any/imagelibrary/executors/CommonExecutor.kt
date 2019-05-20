package com.any.imagelibrary.executors

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

/**
 * 公用线程池，执行一些耗时操作
 * Create by: chenWei.li
 * Date: 2018/8/24
 * Time: 下午7:53
 * Email: lichenwei.me@foxmail.com
 */
class CommonExecutor private constructor() {
    private val mExecutorService: ExecutorService

    init {
        mExecutorService = Executors.newCachedThreadPool { runnable ->
            val thread = Thread(runnable)
            thread.name = "CommonExecutor"
            thread
        }
    }


    fun execute(runnable: Runnable) {
        mExecutorService.execute(runnable)
    }

    companion object {
        private val mCommonExecutor by lazy { CommonExecutor() }

        fun getInstance(): CommonExecutor {
            return mCommonExecutor
        }

    }

}
