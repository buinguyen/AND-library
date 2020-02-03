package com.alan.app.mvvm.base.core

import android.content.Intent
import androidx.fragment.app.Fragment
import com.alan.app.mvvm.base.OnFragmentResultHandler
import com.alan.app.mvvm.base.ResultCode
import com.alan.app.mvvm.base.navigation.NavController
import com.alan.app.mvvm.base.navigation.NavControllerProvider
import com.alan.app.mvvm.base.navigation.OnBackHandler

open class SyncBackFragment : Fragment(), OnBackHandler, OnFragmentResultHandler {

    private val mNavController: NavController? by lazy {
        var navControllerProvider: NavControllerProvider? = null

        if (activity is NavControllerProvider) {
            navControllerProvider = activity as NavControllerProvider
        }

        return@lazy navControllerProvider?.provideNavController()
    }

    override fun onFragmentResult(requestCode: Int, @ResultCode action: Int, extraData: Intent?) {
        /* Need override in subclass */
    }

    fun setFragmentResult(@ResultCode action: Int, intent: Intent?) {
        mNavController?.setResult(action, intent)
    }

    /**
     * @return true: don't handle by NavController
     * @return false: handle go back by NavController
     * */
    override fun onBackHandled(): Boolean = false

    /**
     * Use for transiting between screen
     */
    protected fun sendToNav(action: ((nav: NavController) -> Unit)?) {
        mNavController?.let { action?.invoke(it) }
    }

}