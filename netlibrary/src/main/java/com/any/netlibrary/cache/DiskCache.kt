package com.any.netlibrary.cache


import android.content.Context
import android.util.Log
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import java.io.File


/**
 * @author any
 * @date 2017/11/30
 */
class DiskCache(appContext: Context) : ICache {
    /**
     * 更改存储路径
     */
    private val fileDir: File = appContext.cacheDir

    override fun <T> get(key: String, cls: Class<T>): Observable<T> {
        return Observable.create(ObservableOnSubscribe<T> { e ->
            val t = getDataByFilePath(getKey(key), cls) as T
            if (t != null) {
                e.onNext(t)
            }
            Log.e("msg", "DiskCache..." + t!!)
            e.onComplete()
        }).subscribeOn(Schedulers.io())
    }

    /**
     * 存放本地
     *
     * @param key
     * @param t
     * @param <T>
    </T> */
    override fun <T> put(key: String, t: T) {
        Observable.create(ObservableOnSubscribe<T> { e ->
            val isSuccess = isSave(getKey(key), t)
            if (isSuccess) {
                e.onNext(t)
            }
            e.onComplete()
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }


    fun <T> isSave(filePath: String, t: T): Boolean {
        val result = Gson().toJson(t)
        return FileUtils.writeResultToFileInKotlin(filePath, result)
    }


    fun <T> getDataByFilePath(filePath: String, cls: Class<T>): T {
        val result = FileUtils.getResultByFilePathInKotlin(filePath)
        return Gson().fromJson(result, cls)
    }

    //
    //    /**
    //     * 判断缓存是否已经失效
    //     */
    //    private boolean isCacheDataFailure(File dataFile) {
    //        if (!dataFile.exists()) {
    //            return false;
    //        }
    //        long existTime = System.currentTimeMillis() - dataFile.lastModified();
    //        boolean failure = false;
    ////        if (NetWorkUtls.getNetworkType(CacheLoader.getApplication()) == NetWorkUtls.NETTYPE_WIFI) {
    ////            failure = existTime > WIFI_CACHE_TIME ? true : false;
    ////        } else {
    ////            failure = existTime > OTHER_CACHE_TIME ? true : false;
    ////        }
    //        return failure;
    //    }
    //
    override fun clearCache(key: String) {
        val file = File(fileDir, getKey(key))
        if (file.exists()) file.delete()
    }


    private fun getKey(key: String): String {
        return fileDir.absolutePath + File.separator + FileUtils.getMD5(key) + NAME
    }

    companion object {

        private val NAME = ".sk"
    }


}