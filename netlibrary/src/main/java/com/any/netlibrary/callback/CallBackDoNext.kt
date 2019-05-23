package com.any.netlibrary.callback

/**
 * User: any
 * Date: 2019/3/5
 */
interface CallBackDoNext<T> {
    fun handleData(t: T)
}