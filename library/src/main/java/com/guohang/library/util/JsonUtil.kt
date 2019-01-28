package com.guohang.library.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object JsonUtil {
    val gson: Gson by lazy {
        GsonBuilder().serializeNulls().create()
    }

    fun toStr(any: Any?) = gson.toJson(any)

    fun <T> toBean(json: String , clazz: Class<T>): T {
        return gson.fromJson(json , clazz)
    }

    fun <T> toList(json: String):List<T> {

        val parameterizedType = com.google.gson.internal.`$Gson$Types`.newParameterizedTypeWithOwner(null, List::class.java, object : TypeToken<List<T>>(){}.type)
        return gson.fromJson(json , parameterizedType)
    }

    fun <K , V> toMap(json: String):Map<K , V> {
        val parameterizedType = com.google.gson.internal.`$Gson$Types`.newParameterizedTypeWithOwner(null, Map::class.java, object : TypeToken<K>(){}.type , object : TypeToken<V>(){}.type)
        return gson.fromJson(json , parameterizedType)
    }

    fun <T> toMap(json: String , t: T):T {
        val parameterizedType = com.google.gson.internal.`$Gson$Types`.newParameterizedTypeWithOwner(null, Map::class.java, object : TypeToken<T>(){}.type)
        return gson.fromJson(json , parameterizedType)
    }

//    class ParamterType(var clazz: Class<Any> , vararg ars: String): ParameterizedType {
//
//        override fun getRawType(): Type = clazz
//
//        override fun getOwnerType(): Type? = null
//
//        override fun getActualTypeArguments(): Array<Type>? = null
//    }
}
