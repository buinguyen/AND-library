package com.alan.app.mvvm.base.navigation

import android.content.Intent

interface IFragmentResult {

    companion object {
        const val ACTION_OK = 1
        const val ACTION_CANCEL = 0
    }

    fun onFragmentResult(requestCode: Int, action: Int, extraData: Intent?)

}
