package com.alan.app.mvvm.base

import androidx.annotation.IntDef

@IntDef(ResultCode.ACTION_OK, ResultCode.ACTION_CANCEL)
annotation class ResultCode {
    companion object {
        const val ACTION_OK = 1
        const val ACTION_CANCEL = 0
    }
}