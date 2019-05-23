package com.any.netlibrary.cache


import io.reactivex.Observable

/**
 * @author any
 * @date 2017/11/30
 */
abstract class NetworkCache<T> {
    abstract operator fun get(key: String, cls: Class<T>): Observable<T>?
}