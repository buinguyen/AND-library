package com.alan.app.mvvm.base.navigation

import androidx.annotation.IntDef

@IntDef(
    FragmentAnimation.TRANSITION_POP,
    FragmentAnimation.TRANSITION_FADE_IN_OUT,
    FragmentAnimation.TRANSITION_SLIDE_LEFT_RIGHT,
    FragmentAnimation.TRANSITION_SLIDE_LEFT_RIGHT_WITHOUT_EXIT,
    FragmentAnimation.TRANSITION_NONE,
    FragmentAnimation.TRANSITION_SLIDE_LEFT_RIGHT_2
)
annotation class FragmentAnimation {

    companion object {
        const val TRANSITION_POP = 1
        const val TRANSITION_FADE_IN_OUT = 2
        const val TRANSITION_SLIDE_LEFT_RIGHT = 3
        const val TRANSITION_SLIDE_LEFT_RIGHT_WITHOUT_EXIT = 4
        const val TRANSITION_NONE = 5
        const val TRANSITION_SLIDE_LEFT_RIGHT_2 = 6
    }
}