package com.alan.app.mvvm.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceHelper constructor(context: Context) {

    private var mPref: SharedPreferences =
            context.getSharedPreferences("MVVMAppStorage", Context.MODE_PRIVATE)

    fun setAccessToken(token: String?) {
        token ?: return
        mPref.edit(commit = true) { putString(ACCESS_TOKEN, token) }
    }

    fun getAccessToken(): String {
        return mPref.getString(ACCESS_TOKEN, "") ?: ""
    }

    fun clear() {
        setAccessToken("")
    }

    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
    }
}