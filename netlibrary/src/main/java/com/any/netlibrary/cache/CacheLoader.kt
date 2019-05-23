package com.any.netlibrary.cache

import android.content.Context
import android.util.Log
import io.reactivex.Observable
import java.io.File

/**
 * @author any
 * @date 2017/11/30
 */
class CacheLoader private constructor(private val appContext: Context) {

    private val diskCache: ICache

    init {
        diskCache = DiskCache(appContext)
    }


    companion object {
        var cacheLoader: CacheLoader? = null

        @Synchronized
        fun getInstance(appContext: Context): CacheLoader? {
            if (cacheLoader == null) {
                cacheLoader = CacheLoader(appContext.applicationContext)
            }
            return cacheLoader
        }

    }


    fun getCacheDir(): File {
        return appContext.cacheDir
    }


    fun <T> asDataObservable(key: String, cls: Class<T>, networkCache: NetworkCache<T>): Observable<T> {
        return Observable.concat(disk(key, cls), net(key, cls, networkCache))
    }


    fun <T> disk(key: String, cls: Class<T>): Observable<T> {
        return diskCache[key, cls].doOnNext {
            Log.e("msg", "我是磁盘缓存")
            // 放入内存
        }
    }

    fun <T> net(key: String, cls: Class<T>, networkCache: NetworkCache<T>): Observable<T> {
        return networkCache[key, cls]!!.doOnNext { t ->
            if (t != null) {
                diskCache.put(key, t)
                Log.e("msg", "我是网络缓存")
            }
        }
    }


    fun clearMemoryDisk(key: String) {
        diskCache.clearCache(key)
    }

}



