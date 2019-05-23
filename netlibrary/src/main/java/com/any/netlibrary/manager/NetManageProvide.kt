package com.any.netlibrary.manager

import android.content.Context
import com.any.netlibrary.cache.CacheLoader
import com.any.netlibrary.cache.NetworkCache
import com.any.netlibrary.callback.CallBack
import com.any.netlibrary.callback.CallBackDoNext
import com.any.netlibrary.model.BaseModel
import com.any.netlibrary.net.ExceptionHandle

import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * User: any
 * Date: 2019/3/5
 */
object NetManageProvide {

//    // 统一处理数据
//    private val pageSize = 10
//
//    fun getParamMap(pageNo: Int): Map<String, Any> {
//        val objectMap = HashMap<String, Any>()
//        objectMap["pageNo"] = pageNo
//        objectMap["pageSize"] = pageSize
//        return objectMap
//    }
//
//
//    /**
//     * 网络加载，并且生成缓存数据 控制数据流
//     *
//     * @param context
//     * @param key
//     * @param t
//     * @param data
//     */
//    fun <T : BaseModel<*>> doTask(
//        key: String,
//        t: Class<T>,
//        data: Observable<T>?,
//        lifecycleProvider: LifecycleProvider<ActivityEvent>,
//        context: Context,
//        iCallBack: CallBack<T>
//    ) {
//        val networkCache = object : NetworkCache<T>() {
//            override fun get(key: String, cls: Class<T>): Observable<T>? {
//                return data
//            }
//        }
//        val observable = CacheLoader.getInstance(context)?.net(key, t, networkCache)
//        observable
//            ?.compose(lifecycleProvider.bindToLifecycle())
//            ?.subscribeOn(Schedulers.io())
//            ?.observeOn(AndroidSchedulers.mainThread())
//            ?.subscribe(object : Observer<T> {
//                override fun onSubscribe(d: Disposable) {
//                    iCallBack.start(d)
//                }
//
//                override fun onNext(newsBean: T) {
//                    if (newsBean.isSuccess()) {
//                        iCallBack.success(newsBean)
//                    } else {
//                        onError(ExceptionHandle.CustomException(newsBean.message, newsBean.code?.toInt()))
//                    }
//                }
//
//                override fun onError(e: Throwable) {
//                    val dataException = ExceptionHandle.handleException(e, context)
//                    iCallBack.error(dataException)
//                    //错误后，也执行完成回调
//                    onComplete()
//                }
//
//                override fun onComplete() {
//                    iCallBack.complete()
//                }
//            })
//    }

    /***
     * 直接加载网络，并无缓存处理
     * @param data
     * @param lifecycleProvider
     * @param iCallBack
     */
    fun  <T : BaseModel<*>> doTask(
        data: Observable<T>?,
        lifecycleProvider: LifecycleProvider<ActivityEvent>,
        context: Context?,
        iCallBack: CallBack<T>
    ) {
        data?.let {
            it
                .compose(lifecycleProvider.bindToLifecycle()) // 注意代码
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<T> {
                    override fun onSubscribe(d: Disposable) {
                        iCallBack.start(d)
                    }

                    override fun onNext(newsBean: T) {
                        if (newsBean.isSuccess()) {
                            iCallBack.success(newsBean)
                        } else {
                            onError(ExceptionHandle.CustomException(newsBean.message, newsBean.code?.toInt()))
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        val dataException = ExceptionHandle.handleException(e, context)
                        iCallBack.error(dataException)
                        //错误后，也执行完成回调
                        onComplete()
                    }

                    override fun onComplete() {
                        iCallBack.complete()
                    }
                })
        }


    }
//
//    /***
//     * 直接加载网络，并无缓存处理
//     * @param data
//     * @param iCallBack
//     */
//    fun <T : BaseModel<*>> doTask(data: Observable<T>?, context: Context?, iCallBack: CallBack<T>) {
//        data?.let {
//            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<T> {
//                    override fun onSubscribe(d: Disposable) {
//                        iCallBack.start(d)
//                    }
//
//                    override fun onNext(newsBean: T) {
//                        if (newsBean.isSuccess()) {
//                            iCallBack.success(newsBean)
//                        } else {
//                            onError(ExceptionHandle.CustomException(newsBean.message, newsBean.code?.toInt()))
//                        }
//                    }
//
//                    override fun onError(e: Throwable) {
//                        val dataException = ExceptionHandle.handleException(e, context)
//                        iCallBack.error(dataException)
//                        //错误后，也执行完成回调
//                        onComplete()
//                    }
//
//                    override fun onComplete() {
//                        iCallBack.complete()
//                    }
//                })
//        }
//    }
//
//    /***
//     * 直接加载网络，可以在子线中处理一些其它事情操作
//     * @param data
//     * @param lifecycleProvider
//     * @param iCallBack
//     */
//    fun <T : BaseModel<*>> doTask(
//        data: Observable<T>?,
//        lifecycleProvider: LifecycleProvider<ActivityEvent>,
//        context: Context?,
//        iCallBack: CallBack<T>,
//        iCallBackDoNext: CallBackDoNext<T>
//    ) {
//        data?.let {
//            it.compose(lifecycleProvider.bindToLifecycle()) // 注意代码
//                .subscribeOn(Schedulers.io())
//                .doOnNext { t -> iCallBackDoNext.handleData(t) }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<T> {
//                    override fun onSubscribe(d: Disposable) {
//                        iCallBack.start(d)
//                    }
//
//                    override fun onNext(newsBean: T) {
//                        if (newsBean.isSuccess()) {
//                            iCallBack.success(newsBean)
//                        } else {
//                            onError(ExceptionHandle.CustomException(newsBean.message, newsBean.code?.toInt()))
//                        }
//                    }
//
//                    override fun onError(e: Throwable) {
//                        val dataException = ExceptionHandle.handleException(e, context)
//                        iCallBack.error(dataException)
//                        //错误后，也执行完成回调
//                        onComplete()
//
//                    }
//
//                    override fun onComplete() {
//                        iCallBack.complete()
//                    }
//                })
//        }
//    }


}
