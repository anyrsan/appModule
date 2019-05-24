package debug.service

import debug.manager.NetRequestManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 *
 * @author any
 * @time 2019/05/23 16.37
 * @details  测试下载文件类
 */
object DownFileServiceApi {


    fun getFileService(): DownFileService {
        return NetRequestManager.getInstance().createHttpsRetrofit().create(DownFileService::class.java)
    }


    //下载文件demo
    interface DownFileService {
        @Streaming
        @GET
        fun downloadFile(@Url url: String, @Header("Range") range: String?): Call<ResponseBody>

//        @Streaming
//        @GET
//        fun downloadFile(@Url url: String): Call<ResponseBody>
    }

}