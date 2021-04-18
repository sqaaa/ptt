package com.sqaaa.ptt.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

abstract class BaseFragment(@LayoutRes id: Int) : Fragment(id) {

    private val dispatcher by lazy { CustomTransactionBackPressDispatcher() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var transitionMap: Map<Int, Transition>? = null
        if (this is CustomTransitionFragment) {
            postponeEnterTransition()
            transitionMap = this.transitionMap()
            requireActivity().onBackPressedDispatcher.addCallback(
                dispatcher
            )
        }
        super.onViewCreated(view, savedInstanceState)
        transitionMap ?: return
        doCustomPopInTransition(transitionMap)
        startPostponedEnterTransition()
    }

    override fun onStart() {
        super.onStart()
        dispatcher.isEnabled = true
    }

    override fun onStop() {
        super.onStop()
        dispatcher.isEnabled = false
    }

    open suspend fun doCustomPopOutTransition() {
        if (this !is CustomTransitionFragment) {
            throw IllegalStateException("You probably forgot to implement CustomTransitionFragment")
        }
        val transitionMap: Map<Int, Transition> = this.transitionMap()
        suspendCancellableCoroutine<Unit> {
            val listener = object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) {
                }

                override fun onTransitionEnd(transition: Transition) {
                    if (it.isActive) {
                        it.resume(Unit) { }
                    }
                }

                override fun onTransitionCancel(transition: Transition) {
                    if (it.isActive) {
                        it.resume(Unit) { }
                    }
                }

                override fun onTransitionPause(transition: Transition) {
                }

                override fun onTransitionResume(transition: Transition) {
                }
            }
            createTransitionSet(transitionMap, requireView(), listener).forEach { (view, _) ->
                view.isVisible = false
            }
        }
    }

    private fun doCustomPopInTransition(transitionMap: Map<Int, Transition>) {
        requireView().doOnPreDraw { root ->
            createTransitionSet(transitionMap, root).forEach { (transitionView, _) ->
                transitionView.isVisible = true
            }
        }
    }

    private fun createTransitionSet(
        transitionMap: Map<Int, Transition>,
        root: View,
        transitionListener: Transition.TransitionListener? = null
    ): Map<View, Transition> {
        val set = TransitionSet()
        val transitionViews = transitionMap.mapKeys { (id, _) ->
            root.findViewById<View>(id)
        }
        transitionViews.forEach { (transitionView, transition) ->
            transition.addTarget(transitionView)
            set.addTransition(transition)
        }
        transitionListener?.let {
            set.addListener(transitionListener)
        }
        set.duration = 1000
        set.interpolator = AccelerateDecelerateInterpolator()
        set.ordering = TransitionSet.ORDERING_TOGETHER
        TransitionManager.beginDelayedTransition(root as ViewGroup, set)
        return transitionViews
    }

    private inner class CustomTransactionBackPressDispatcher : OnBackPressedCallback(false) {

        override fun handleOnBackPressed() {
            lifecycle.coroutineScope.launch {
                isEnabled = false
                doCustomPopOutTransition()
                requireActivity().onBackPressed()
            }
        }
    }

}
