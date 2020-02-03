package com.alan.app.mvvm.base

import android.content.Intent

interface OnFragmentResultHandler {

    fun onFragmentResult(requestCode: Int, @ResultCode action: Int, extraData: Intent?)
}
