package com.guohang.library.net

import okhttp3.*
import okio.Buffer
import retrofit2.http.Multipart
import java.io.IOException

abstract class GInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        headers()?.apply { request = addHeaders(request , this) }
        params()?.apply { request = addParams(request , this) }

       return chain.proceed(request)
    }

    /**
     * ===========================  添加公共头   ===========================================
     */
    fun headers() : Map<String , String>? = null
    fun addHeaders(request: Request , params: Map<String , String>): Request {
        return request.newBuilder()
            .apply {
                params.forEach {
                    this.addHeader(it.key , it.value)
                }
            }
            .build()
    }

    /**
     * ======================== 添加公共请求参数    ==========================================
     */
    //请求参数
    abstract fun params(): Map<String , String>

    //添加请求参数
    fun addParams(request: Request , params: Map<String , String>): Request =
        when(request.method()) {
            "GET" -> addGetParams(request , params)
            "POST" -> addPostParams(request , params)
            else -> request
        }

    //添加get请求参数
    fun addGetParams(request: Request , params: Map<String , String>): Request {
        var url =  request.url().newBuilder()
            .apply {
                params.forEach {
                    this.addQueryParameter(it.key , it.value)
                }
            }
            .build()

        return request.newBuilder().url(url).build()
    }

    //添加Post请求参数
    fun addPostParams(request: Request , params: Map<String , String>): Request {
        var oldBody = request.body()

        val newBody = when(oldBody) {
            is MultipartBody -> addMultiPartParams(oldBody , params)
            is FormBody -> addFormParams(oldBody , params)
            else -> addFormParams(null , params)
        }

        return request.newBuilder()
                .post(newBody)
                .build()
    }

    fun addMultiPartParams(body: MultipartBody , params: Map<String , String>): RequestBody {
        return MultipartBody.Builder()
                .addPart(body)
                .apply {
                    params.forEach { this.addFormDataPart(it.value , it.key) }
                }
                .build()
    }


    fun addFormParams(body: FormBody? , params: Map<String, String>): RequestBody {
        var commonBody = FormBody.Builder()
            .apply {
                params.forEach { this.add(it.value , it.key) }
            }
            .build()

        var postString = bodyToString(body)
        postString += if (postString.isEmpty()) "" else "&" + bodyToString(commonBody)

        return RequestBody.create(
            MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"),
            postString)
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val copy = request
            val buffer = Buffer()
            if (copy != null)
                copy.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }

    }
}
