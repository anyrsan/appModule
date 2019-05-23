package debug

import android.util.Log
import com.any.netlibrary.cache.FileUtils
import com.any.netlibrary.service.DownFileServiceApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile

/**
 *
 * @author any
 * @time 2019/05/23 16.47
 * @details
 */
object DownFileZip {

    private val downFileService by lazy { DownFileServiceApi.getFileService() }

    private fun writeResponseBodyToFile(
        response: Response<ResponseBody>,
        filePath: String,
        isCanResume: Boolean = false,
        downProgress: ((progress: Int, isDone: Boolean) -> Unit)?
    ): Boolean {
        val file = File(filePath)
        return try {
            //创建文件
            FileUtils.createFile(filePath)
            val byteBuffer = ByteArray(1024)
            // 文件大小
            var fileSize = response.body()?.contentLength() ?: 0
            Log.e("msg", "fileSize...$fileSize")
            //输入流
            val inputStream = response.body()?.byteStream()
            //输出流
            val raf = RandomAccessFile(file, "rw")

            var downSize = 0L
            // 续点下载
            if (isCanResume) {
                downSize = raf.length()
                raf.seek(downSize)
                //加上以前的大小
                fileSize += downSize
            } else {
                raf.seek(0)
            }

            // 处理写入
            inputStream?.let {
                while (true) {
                    val read = it.read(byteBuffer)
                    if (read == -1) {
                        break
                    }
                    //输出到文件
                    raf.write(byteBuffer, 0, read)
                    //进度条
                    downSize += read
                    //回调进度
                    //进度百分比
                    val percent = (downSize.toFloat() / fileSize * 100).toInt()

                    downProgress?.invoke(percent, downSize == fileSize)
                }
            }
            raf.close()
            true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            file.deleteRecursively()
            Log.e("msg", "down...err..." + e.localizedMessage + "===>" + e.message)
            false
        } catch (e: Exception) {
            e.printStackTrace()
            //其它异常暂时不删除文件
//            file.deleteRecursively()
            Log.e("msg", "down...err..." + e.localizedMessage + "===>" + e.message)
            false
        }
    }


    fun downApk(
        filePath: String,
        url: String,
        range: String,
        downProgress: ((progress: Int, isDone: Boolean) -> Unit)?,
        callBack: ((isDone: Boolean) -> Unit)?
    ) {

        // 执行任务
        fun doTask(): Boolean {
            return try {
                val responseBody = DownFileServiceApi.getFileService().downloadFile(url, range)
                Log.e("msg","responseBody...$responseBody")
                val response = responseBody.execute()
                Log.e("msg","response...$response")
                writeResponseBodyToFile(response, filePath, true, downProgress)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        Observable.create<Boolean> {
            it.onNext(doTask())
            it.onComplete()
        }.subscribeOn(Schedulers.newThread()).subscribe {
            Log.e("msg", ".....下载结果。。。$it")
            callBack?.invoke(it)
        }
    }


}