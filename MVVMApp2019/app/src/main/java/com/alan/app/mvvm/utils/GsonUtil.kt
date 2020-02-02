package com.alan.app.mvvm.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.lang.reflect.Type

object GsonUtil {

    @JvmStatic
    fun <T> parse(text: String?, oClass: Class<T>): T? {
        return try {
            Gson().fromJson(text, oClass)
        } catch (e: Exception) {
            null
        }
    }

    @JvmStatic
    fun <T> parse(text: String?, oType: Type): T? {
        return try {
            Gson().fromJson(text, oType)
        } catch (e: Exception) {
            null
        }
    }

    @JvmStatic
    fun <T> parse(obj: JsonObject?, oClass: Class<T>): T? {
        return try {
            Gson().fromJson(obj, oClass)
        } catch (e: Exception) {
            null
        }
    }

    @JvmStatic
    fun <T> parse(obj: JsonObject?, oType: Type): T? {
        return try {
            Gson().fromJson(obj, oType)
        } catch (e: Exception) {
            null
        }
    }
}