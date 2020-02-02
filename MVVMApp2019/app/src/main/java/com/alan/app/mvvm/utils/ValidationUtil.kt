package com.alan.app.mvvm.utils

import android.text.TextUtils
import java.util.regex.Pattern

object ValidationUtil {

    private val EMAIL_ADDRESS = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z\\-]{1,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z\\-]{1,25}" +
                ")+"
    )

    @JvmStatic
    fun isValidEmail(target: CharSequence?): Boolean {
        return target != null && !TextUtils.isEmpty(target) && EMAIL_ADDRESS.matcher(target).matches()
    }
}