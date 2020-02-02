package com.pcb.fintech.data

/**
 * A generic class that holds a value getInstance its loading status.
 *
 * ResponseResult is usually created by the Repository classes where they return
 * `LiveData<ResponseResult<T>>` to pass back the latest data to the UI getInstance its fetch status.
 */

data class ResponseResult<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    fun getMessageAvoidNull(): String {
        return message?: "Error!"
    }

    companion object {
        fun <T> success(data: T): ResponseResult<T> {
            return ResponseResult(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): ResponseResult<T> {
            return ResponseResult(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): ResponseResult<T> {
            return ResponseResult(Status.LOADING, data, null)
        }
    }
}