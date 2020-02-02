package com.alan.app.mvvm.base.util

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("error") var error: String? = null,
    @SerializedName("error_description") var error_description: String? = null
) {

    fun isErrorValid(): Boolean {
        return error != null && error_description != null
    }
}