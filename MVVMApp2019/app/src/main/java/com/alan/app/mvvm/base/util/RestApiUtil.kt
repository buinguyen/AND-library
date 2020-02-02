package com.alan.app.mvvm.base.util

import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object RestApiUtil {

    fun getError(throwable: Throwable): String {
        var error: String? = null
        if (throwable is UnknownHostException
            || throwable is ConnectException
            || throwable is SocketTimeoutException
        ) { // internet connection error
            error = "Connection error!"
        } else if (throwable is JsonSyntaxException) {
            error = "Data format error!"
        } else if (throwable is HttpException) { // HTTP exception (code != 200)
            val errorBody = throwable.response()?.errorBody()
            if (errorBody != null) {
                val errorMessage = parseErrorBody(errorBody, ApiError::class.java)
                if (errorMessage?.isErrorValid() == true) {
                    error = errorMessage.error
                }
            }
        }
        return error ?: throwable.localizedMessage
    }

    @JvmStatic
    fun <T> parseErrorBody(errorBody: ResponseBody, clazz: Class<T>): T? {
        return GsonUtil.parse(errorBody.string(), clazz)
    }

}
