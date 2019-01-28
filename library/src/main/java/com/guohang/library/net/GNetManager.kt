package com.guohang.library.net

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.guohang.library.base.GBaseApp
import com.guohang.library.util.JsonUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.util.concurrent.TimeUnit

abstract class GNetManager<T: GApiService>{
    val mOkHttp by lazy { initOkHttp() }
    val mRetrofit by lazy { initRetrofit() }
    val mService by lazy { initServiceApi() }

    fun initOkHttp() =
        OkHttpClient().newBuilder()
            .readTimeout(readTimeOut(), TimeUnit.SECONDS)
            .connectTimeout(connectTimeOUt() , TimeUnit.SECONDS)
            .addInterceptor(interceptor())
            .addInterceptor(interceptorLog())
            .addNetworkInterceptor(StethoInterceptor())
            .build()


    fun initRetrofit() =
        Retrofit.Builder()
            .baseUrl(baseUrl())
            .client(mOkHttp)
            .addConverterFactory(converter())
            .addCallAdapterFactory(callAdapter())
            .build()

    private fun initServiceApi() {
        var clazz = (this.javaClass.genericSuperclass as? ParameterizedType)?.let {
            it.actualTypeArguments[0] as Class<T>
        }
        mRetrofit.create(clazz)
    }

    fun readTimeOut() = 2000L
    fun connectTimeOUt() = 2000L
    fun interceptorLog() = HttpLoggingInterceptor()

    fun interceptor() : GInterceptor? = null

    //Retrofit参数配置
    abstract fun baseUrl(): String
    fun converter() = GsonConverterFactory.create(JsonUtil.gson)
    fun callAdapter() = RxJava2CallAdapterFactory.create()
}