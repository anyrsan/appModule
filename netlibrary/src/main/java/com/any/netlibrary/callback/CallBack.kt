package com.any.netlibrary.callback

import com.any.netlibrary.net.ExceptionHandle
import io.reactivex.disposables.Disposable

/**
 * User: any
 * Date: 2019/2/28
 * 如果不是200，全部失败
 */
interface CallBack<T> {

    fun success(t: T)

    fun start(d: Disposable)

    fun error(dataEx: ExceptionHandle.ResponeThrowable)

    fun complete()
}