package com.alan.app.mvvm.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object PermissionUtil {

    fun checkPermission(context: Context?, permission: String): Boolean {
        context ?: return false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
        val perResult = ContextCompat.checkSelfPermission(context, permission)
        return perResult == PackageManager.PERMISSION_GRANTED
    }
}