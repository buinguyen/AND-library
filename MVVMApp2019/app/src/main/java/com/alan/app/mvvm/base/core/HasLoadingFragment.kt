package com.alan.app.mvvm.base.core

import android.app.ProgressDialog
import android.os.Bundle
import com.alan.app.mvvm.R

open class HasLoadingFragment : SyncBackFragment() {

    @Suppress("DEPRECATION")
    protected var mLoadingDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLoadingDialog = ProgressDialog(activity, R.style.DialogCustomStyle)
    }

    protected fun showLoading(cancelable: Boolean = false) {
        if (mLoadingDialog?.isShowing == true) {
            mLoadingDialog?.dismiss()
        }
        mLoadingDialog?.setCancelable(cancelable)
        mLoadingDialog?.show()
    }

    protected fun hideLoading() = mLoadingDialog?.dismiss()
}