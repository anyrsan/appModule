package debug.manager

import com.any.netlibrary.net.RetrofitBase
import retrofit2.Retrofit

/**
 *
 * @author any
 * @time 2019/05/23 16.29
 * @details 网络请求类
 */
class NetRequestManager : RetrofitBase() {
    override val baseUrl: String
        get() = ApiUrl.BASE_URL

    //处理
    companion object {
        private val netRequestManager by lazy { NetRequestManager() }
        fun getInstance(): NetRequestManager {
            return netRequestManager
        }
    }


    fun createHttpsRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(configOkHttpClient())
            .baseUrl(ApiUrl.BASE_URL)
            .build()
    }


}