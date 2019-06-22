package debug.manager

import android.content.Context
import android.util.Log
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import debug.parseJson.ParseHandler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.IOException
import java.lang.Exception

/**
 *
 * @author any
 * @time 2019/06/22 10.57
 * @details
 */


// 接收Call<ResponseBody>


object NetManagerProvide2 {


    //
    inline fun <reified T> doTask(
        call: Call<ResponseBody>, lifecycleProvider: LifecycleProvider<ActivityEvent>, crossinline resultCall:(t:T?)->Unit
    ) {
        Observable.create<Pair<Boolean, T?>> {
            //处理网络请求异常
            var t: T? = null
            try {
                val responseBody = call.execute()
                val jsonData = responseBody?.body()?.string()
                t = ParseHandler.getModel<T>(jsonData)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                it.onNext((t != null) to t)
                it.onComplete()
            }
        }
            .compose(lifecycleProvider.bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resultCall.invoke(it.second)
            }, {
                resultCall.invoke(null)
            }, {

            })

    }


}


