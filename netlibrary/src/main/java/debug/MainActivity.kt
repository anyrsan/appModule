package debug

import android.os.Bundle
import android.util.Log
import android.view.View
import com.any.netlibrary.R
import com.any.netlibrary.callback.CallBack
import debug.manager.NetManageProvide
import com.any.netlibrary.net.ExceptionHandle
import debug.service.BaseServiceApi
import com.trello.rxlifecycle2.components.RxActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.netpk_activity_main.*
import java.io.File

class MainActivity : RxActivity() {


    val downUrl = "http://services.gradle.org/distributions/gradle-4.4-rc-3-src.zip"

    val fileName = "gradle-4.4-rc-3-src.zip"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.netpk_activity_main)

        val file = File(applicationContext.cacheDir, fileName)
//        file.deleteRecursively()

        Log.e("msg", "filePath...${file.absolutePath}")
    }


    fun downFile(v: View) {
        //23751998
        //23751998 - 1597033 =22154965
        val file = File(applicationContext.cacheDir, fileName)
        val size = if (file.exists()) file.length() else 0
        Log.e("msg", "...$size")
        val range = "bytes=$size-"
        DownFileZip.downApk(file.absolutePath, downUrl, range, { progress, isDone ->
            runOnUiThread {
                progressTv.text = "当前下载进度 $progress , 是否完成 ： $isDone"
            }
        }, {
            Log.e("msg", "下载结果 $it")
        })


    }

    fun postData(v: View) {
//        val token = "xxxx"
//        val map = mutableMapOf<String, String>()
//        val observable = BaseServiceApi.getBaseServiceApi().getData(token, map)
//        doTask(observable, this, applicationContext, object : CallBack<PostData> {
//            override fun success(t: PostData) {
//
//            }
//
//            override fun start(d: Disposable) {
//            }
//
//            override fun error(dataEx: ExceptionHandle.ResponeThrowable) {
//            }
//
//            override fun complete() {
//            }
//        })


        NetManageProvide.doTask(
            BaseServiceApi.getBaseServiceApi().getData(),
            this,
            applicationContext,
            object : CallBack<PostData> {
                override fun success(t: PostData) {

                }

                override fun start(d: Disposable) {
                }

                override fun error(dataEx: ExceptionHandle.ResponeThrowable) {
                }

                override fun complete() {
                }

            })

    }

}
