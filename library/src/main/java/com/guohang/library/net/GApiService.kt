package com.guohang.library.net

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap


/**
 * =======================  @get    ======================================
 * @Path                    :  Url占位符，
 * @Query.@QueryMap         : @get 查询参数
 *
 *
 * =======================  @post   =====================================
 * @Body                    : 请求体 ，实例对象(RequestBody , map等)
 * @Field , @FiledMap       : 键值对 （需要添加@FormUrlEncoded）
 * @Part , @PartMap         : 文件上传 （需要添加@Mutipart）
 */
interface GApiService {

    /**
     * 单图片上传图片上传
     */
    @Multipart
    @POST("upload")
    fun uploadFile(@Part file: MultipartBody.Part)


    /**
     * 多图片上传
     */
    @Multipart
    @POST("upload")
    fun uploadFiles(@PartMap map: Map<String , RequestBody>)
}