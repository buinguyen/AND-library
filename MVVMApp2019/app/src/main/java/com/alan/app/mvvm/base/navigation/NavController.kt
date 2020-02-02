package com.alan.app.mvvm.base.navigation

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.alan.app.mvvm.R

interface NavController {

    val activeFragment: Fragment?

    fun openFragment(fragment: Fragment, addToBackStack: Boolean = true,
                     @FragmentAnimation animationType: Int = FragmentAnimation.TRANSITION_SLIDE_LEFT_RIGHT,
                     forResult: Pair<Fragment, Int>? = null)

    fun openActivity(intent: Intent?, enterAnim: Int = R.anim.slide_in_from_right,
                     exitAnim: Int = R.anim.slide_out_to_left)

    fun goBack()

    fun clearController()

    companion object {
        const val KEY_ANIM_ENTER_RES_ID = "KEY_ANIM_ENTER_RES_ID"
        const val KEY_ANIM_EXIT_RES_ID = "KEY_ANIM_EXIT_RES_ID"

        fun replace(fm: FragmentManager, fragment: Fragment, id: Int, addToBackStack: Boolean,
                    @FragmentAnimation animationType: Int) {
            val transaction = fm.beginTransaction()

            when (animationType) {
                FragmentAnimation.TRANSITION_POP -> transaction.setCustomAnimations(
                        R.anim.anim_enter,
                        R.anim.anim_exit,
                        R.anim.anim_pop_enter,
                        R.anim.anim_pop_exit
                )
                FragmentAnimation.TRANSITION_FADE_IN_OUT -> transaction.setCustomAnimations(
                        R.anim.anim_frag_fade_in,
                        R.anim.anim_frag_fade_out
                )
                FragmentAnimation.TRANSITION_SLIDE_LEFT_RIGHT_2 -> transaction.setCustomAnimations(
                        R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left
                )
                FragmentAnimation.TRANSITION_SLIDE_LEFT_RIGHT -> transaction.setCustomAnimations(
                        R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left,
                        R.anim.slide_in_from_left,
                        R.anim.slide_out_to_right
                )
                FragmentAnimation.TRANSITION_SLIDE_LEFT_RIGHT_WITHOUT_EXIT -> transaction.setCustomAnimations(
                        R.anim.slide_in_from_right, 0
                )

                FragmentAnimation.TRANSITION_NONE -> transaction.setCustomAnimations(0, 0, 0, 0)
                else -> transaction.setCustomAnimations(0, 0)
            }

            if (addToBackStack)
                transaction.addToBackStack(fragment.javaClass.canonicalName)

            transaction.replace(id, fragment, fragment.javaClass.canonicalName)
            transaction.commit()
        }
    }
}