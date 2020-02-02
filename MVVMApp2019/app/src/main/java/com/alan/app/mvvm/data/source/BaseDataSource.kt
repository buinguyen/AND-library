package com.alan.app.mvvm.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pcb.fintech.data.ResponseResult
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

/**
 * Abstract Base Data source class getInstance error handling
 */
abstract class BaseDataSource {

    protected suspend fun <T, R> callSingle(
            call: suspend () -> Response<T>,
            transform: (callResult: T) -> ResponseResult<R>,
            errorMsgExtractor: ((response: Response<T>) -> String)? = null): ResponseResult<R> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return transform.invoke(body)
                }
            }
            if (errorMsgExtractor != null) {
                return error(errorMsgExtractor.invoke(response))
            } else {
                return error("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            return error(e.localizedMessage ?: e.toString())
        }
    }

    protected suspend fun <T1, T2, R> callTwice(
            call1: suspend () -> Response<T1>,
            call2: suspend () -> Response<T2>,
            transform: (callResult1: T1, callResult2: T2) -> ResponseResult<R>,
            errorMsgExtractor: ((response1: Response<T1>, response2: Response<T2>) -> String)? = null): ResponseResult<R> {
        try {
            val response1 = call1.invoke()
            val response2 = call2.invoke()
            val body1 = if (response1.isSuccessful) response1.body() else null
            val body2 = if (response2.isSuccessful) response2.body() else null
            if (body1 != null && body2 != null) {
                return transform.invoke(body1, body2)
            }
            if (errorMsgExtractor != null) {
                return error(errorMsgExtractor.invoke(response1, response2))
            } else {
                return error("Error network call")
            }
        } catch (e: Exception) {
            return error(e.localizedMessage ?: e.toString())
        }
    }

    private fun <T> error(message: String): ResponseResult<T> {
        Log.e("DataSource", message)
        return ResponseResult.error(message)
    }

    companion object {
        fun <T> resultLiveData(call: suspend () -> ResponseResult<T>): LiveData<ResponseResult<T>> {
            return liveData(Dispatchers.IO) {
                emit(ResponseResult.loading<T>())
                val response = call.invoke()
                emit(response)
            }
        }

    }
}