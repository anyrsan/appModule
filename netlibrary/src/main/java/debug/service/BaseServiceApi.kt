package debug.service

import debug.manager.ApiUrl
import debug.manager.NetRequestManager
import debug.PostData
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 *
 * @author any
 * @time 2019/05/23 16.40
 * @details  基本网络接口
 */
object BaseServiceApi {

    fun getBaseServiceApi(): BaseService = NetRequestManager.getInstance().create(BaseService::class.java)

    interface BaseService {

        @FormUrlEncoded
        @POST(ApiUrl.POST_QADEL)
        fun postData(@Header("token") key: String, @FieldMap map: Map<String, String>): Observable<PostData>

        @GET(ApiUrl.GET_QADEL)
        fun getData(@Header("token") token: String, @QueryMap map: Map<String, String>): Observable<PostData>


        @GET(ApiUrl.TEST_JSON)
        fun getData(): Observable<PostData>


        @GET(ApiUrl.TEST_JSON)
        fun getDataT(): Call<ResponseBody>


        @GET(ApiUrl.TEST_JSON)
        fun getDataT2():Call<ResponseBody>
    }

}