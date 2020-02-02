package com.alan.app.mvvm.base.core

import android.content.Intent
import androidx.fragment.app.Fragment
import com.alan.app.mvvm.base.navigation.HasBackAction
import com.alan.app.mvvm.base.navigation.IFragmentResult
import com.alan.app.mvvm.base.navigation.NavController
import com.alan.app.mvvm.base.navigation.NavControllerProvider

open class SyncBackFragment : Fragment(), HasBackAction, IFragmentResult {

    private val mNavController: NavController? by lazy {
        var navControllerProvider: NavControllerProvider? = null
        if (activity is NavControllerProvider) navControllerProvider =
            activity as NavControllerProvider
        return@lazy navControllerProvider?.provideNavController()
    }

    override fun onFragmentResult(requestCode: Int, action: Int, extraData: Intent?) {
        /* Need override in subclass */
    }

    /**
     * @return true: don't handle by NavController
     * @return false: handle go back by NavController
     * */
    override fun handleBack(): Boolean = false

    /**
     * Use for transiting between screen
     */
    protected fun sendToNav(action: ((nav: NavController) -> Unit)?) {
        mNavController?.let { action?.invoke(it) }
    }

}