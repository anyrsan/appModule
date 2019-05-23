package com.any.netlibrary.cache

import io.reactivex.Observable

/**
 * @author any
 * @date 2017/11/30
 */
interface ICache {
    operator fun <T> get(key: String, cls: Class<T>): Observable<T>

    fun <T> put(key: String, t: T)

    fun clearCache(key: String)
}
