package com.sqaaa.ptt

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.coroutineScope
import com.sqaaa.ptt.fragments.CustomTransitionFragment
import com.sqaaa.ptt.fragments.customa.CustomAnimation1Fragment
import com.sqaaa.ptt.fragments.customa.CustomAnimation2Fragment
import com.sqaaa.ptt.fragments.fade.Fade1Fragment
import com.sqaaa.ptt.fragments.fade.Fade2Fragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PttActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackStackListener()
        setContentView(R.layout.activity_ptt)
        initViews()
    }

    private fun setBackStackListener() {
    }

    private fun initViews() {
        findViewById<View>(R.id.fadeInOut).setOnClickListener {
            supportFragmentManager.commit {
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                replace(R.id.container, Fade1Fragment::class.java, null)
                addToBackStack(null)
            }
            supportFragmentManager.commit {
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                replace(R.id.container, Fade2Fragment::class.java, null)
                addToBackStack(null)
            }
        }
        findViewById<View>(R.id.customA).setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.container, CustomAnimation1Fragment::class.java, null)
                addToBackStack(null)
            }
            lifecycle.coroutineScope.launch {
                delay(1500)
                supportFragmentManager.apply {
                    val fragment = findFragmentById(R.id.container)
                    if (fragment != null && fragment.isVisible && fragment is CustomTransitionFragment) {
                        fragment.doCustomPopOutTransition()
                    }
                    commit {
                        setReorderingAllowed(true)
                        replace(R.id.container, CustomAnimation2Fragment::class.java, null)
                        addToBackStack(null)
                    }
                }
            }
        }
    }

}
