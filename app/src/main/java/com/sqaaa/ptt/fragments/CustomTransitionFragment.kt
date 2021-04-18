package com.sqaaa.ptt.fragments

import androidx.transition.Transition

interface CustomTransitionFragment {

    fun transitionMap(): Map<Int, Transition>

    suspend fun doCustomPopOutTransition()

}
