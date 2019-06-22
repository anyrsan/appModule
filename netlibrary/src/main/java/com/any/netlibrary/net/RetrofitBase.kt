package com.any.netlibrary.net


import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * @author any
 * @date 2017/11/30
 */
abstract class RetrofitBase {

    private val mRetrofit: Retrofit


    abstract val baseUrl: String

    init {
        mRetrofit = initRetrofit()
    }

    /**
     * 配置Okhttp
     * @return
     */
     fun configOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d(TAG, message) })
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(CONNECT_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
        return builder.build()
    }

    /**
     * 获取Retrofit对象
     *
     * @return
     */
    fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(configOkHttpClient())
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    /**
     * 创建ServiceApi
     *
     * @param service
     * @param <T>
     * @return
    </T> */
    fun <T> create(service: Class<T>?): T {
        if (service == null) {
            throw RuntimeException("Api service_agent is null!")
        }
        return mRetrofit.create(service)
    }

    companion object {

        private val TAG = "msg"
        /**
         * 连接超时时长x秒
         */
        val CONNECT_TIME_OUT = 30
        /**
         * 读数据超时时长x秒
         */
        val READ_TIME_OUT = 30
        /**
         * 写数据接超时时长x秒
         */
        val WRITE_TIME_OUT = 30
    }
    //
    //    /**
    //     * 对于有设计需求变更的url获取clone对象
    //     *
    //     * @param baseurl
    //     * @return
    //     */
    //    public Retrofit cloneRetrofit(String baseurl) {
    //        Retrofit retrofit = initRetrofit(interceptor).newBuilder().baseUrl(baseurl).build();
    //        return retrofit;
    //    }


    //    /**
    //     * 添加头
    //     */
    //    private Interceptor interceptor = new Interceptor() {
    //        @Override
    //        public Response intercept(Chain chain) throws IOException {
    //            Request original = chain.request();
    //            Request.Builder requestBuilder = original.newBuilder();
    //            if (SPUtil.getToken() != null) {
    //                requestBuilder.addHeader("token", SPUtil.getToken())
    //                        .addHeader("Accept", "application/json");
    //            }
    //            Logger.e("msg", "-->SPUtil.getToken()" + SPUtil.getToken());
    //            requestBuilder.method(original.method(), original.body());
    //            Request request = requestBuilder.build();
    //            return chain.proceed(request);
    //        }
    //    };


    /**
     * 获取Https的证书
     *
     * @param context Activity（fragment）的上下文
     * @return SSL的上下文对象
     */
    private fun getSSLContext(): SSLContext? {
        try {
            val sslContext = SSLContext.getInstance("TLS")

            //信任所有证书 （官方不推荐使用）
            sslContext.init(null, arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }


                @Throws(CertificateException::class)
                override fun checkServerTrusted(arg0: Array<X509Certificate>, arg1: String) {

                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(arg0: Array<X509Certificate>, arg1: String) {

                }
            }), SecureRandom())
            return sslContext
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        return null
    }


}
