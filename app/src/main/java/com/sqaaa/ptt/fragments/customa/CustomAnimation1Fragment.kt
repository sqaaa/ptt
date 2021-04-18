package com.sqaaa.ptt.fragments.customa

import android.view.Gravity
import androidx.transition.Slide
import androidx.transition.Transition
import com.sqaaa.ptt.R
import com.sqaaa.ptt.fragments.BaseFragment
import com.sqaaa.ptt.fragments.CustomTransitionFragment

class CustomAnimation1Fragment : BaseFragment(R.layout.fragment_one), CustomTransitionFragment {

    override fun transitionMap(): Map<Int, Transition> {
        return mapOf(
            R.id.btn1 to Slide(Gravity.BOTTOM),
            R.id.btn2 to Slide(Gravity.TOP),
        )
    }

}
