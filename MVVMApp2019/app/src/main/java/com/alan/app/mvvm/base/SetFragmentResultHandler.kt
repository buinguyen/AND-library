package com.alan.app.mvvm.base

import android.content.Intent

interface SetFragmentResultHandler {

    fun setResult(@ResultCode action: Int, intent: Intent? = null)
}