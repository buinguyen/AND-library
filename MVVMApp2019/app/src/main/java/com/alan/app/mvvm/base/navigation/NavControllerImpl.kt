package com.alan.app.mvvm.base.navigation

import android.app.ActivityOptions
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.alan.app.mvvm.base.OnFragmentResultHandler
import com.alan.app.mvvm.base.navigation.NavController.Companion.KEY_ANIM_ENTER_RES_ID
import com.alan.app.mvvm.base.navigation.NavController.Companion.KEY_ANIM_EXIT_RES_ID
import java.lang.ref.WeakReference

class NavControllerImpl private constructor() : NavController {

    private var containerId: Int = 0
    private var fragmentMngWeakRef: WeakReference<FragmentManager>? = null
    private var activityWeakRef: WeakReference<FragmentActivity>? = null

    constructor(containerId: Int, fm: FragmentManager, activity: FragmentActivity) : this() {
        this.containerId = containerId
        this.fragmentMngWeakRef = WeakReference(fm)
        this.activityWeakRef = WeakReference(activity)
    }

    override val activeFragment: Fragment?
        get() = fragmentMngWeakRef?.get()?.findFragmentById(containerId)

    override fun openFragment(
            fragment: Fragment, addToBackStack: Boolean,
            @FragmentAnimation animationType: Int, forResult: Pair<Fragment, Int>?) {
        val fm = fragmentMngWeakRef?.get() ?: return
        if (forResult != null) {
            fragment.setTargetFragment(forResult.first, forResult.second)
        }
        NavController.replace(fm, fragment, containerId, addToBackStack, animationType)
    }

    override fun openActivity(intent: Intent?, enterAnim: Int, exitAnim: Int) {
        val activity = activityWeakRef?.get()
        if (activity == null || intent == null) return
        try {
            val bundle =
                    ActivityOptions.makeCustomAnimation(activity, enterAnim, exitAnim).toBundle()
            intent.putExtra(KEY_ANIM_ENTER_RES_ID, enterAnim)
            intent.putExtra(KEY_ANIM_EXIT_RES_ID, exitAnim)
            activity.startActivity(intent, bundle)
        } catch (e: Exception) {
            Log.e("NavController", e.toString())
        }
    }

    /**
     * @param action: OnFragmentResultHandler action
     * @param intent: callback intent data
     */
    override fun setResult(action: Int, intent: Intent?) {
        val targetFragment = activeFragment?.targetFragment
        if (targetFragment != null && targetFragment is OnFragmentResultHandler) {
            val requestCode = activeFragment!!.targetRequestCode
            targetFragment.onFragmentResult(requestCode, action, intent)
            return
        }
        val hostActivity = activeFragment?.activity
        if (hostActivity != null && hostActivity is HasNavTarget) {
            val requestCode = hostActivity.getTargetCode() ?: return
            activeFragment?.onActivityResult(requestCode, action, intent)
        }
    }

    override fun goBack() {
        val activity = activityWeakRef?.get() ?: return
        val fragmentMng = fragmentMngWeakRef?.get() ?: return
        if (activeFragment is OnBackHandler) {
            val handleBackFragment = activeFragment as OnBackHandler
            if (handleBackFragment.onBackHandled()) return
        }
        if (fragmentMng.backStackEntryCount == 0) {
            activity.finish()
            return
        }
        fragmentMng.popBackStack()
        val transaction = fragmentMng.beginTransaction()
        if (activeFragment != null) {
            transaction.remove(activeFragment!!)
            transaction.commitAllowingStateLoss()
        }
    }

    override fun clearController() {
        fragmentMngWeakRef?.clear()
        activityWeakRef?.clear()
    }

}