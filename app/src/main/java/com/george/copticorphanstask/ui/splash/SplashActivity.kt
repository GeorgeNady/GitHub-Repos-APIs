package com.george.copticorphanstask.ui.splash

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.george.copticorphanstask.databinding.ActivitySplashBinding
import com.george.copticorphanstask.ui.auth.AuthActivity
import com.george.copticorphanstask.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SplashViewModel>()

    private val animatorListener by lazy {
        object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                Timber.d("navigate to auth methods fragment")
                viewModel.user.observe(this@SplashActivity) { res ->
                    res.data?.email?.let {
                        Timber.d("logged in user: $it")
                        startActivity<MainActivity>()
                    } ?: startActivity<AuthActivity>()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.animationView.addAnimatorListener(animatorListener)
    }

    private inline fun <reified A : Activity> startActivity() {
        val intent = Intent(this, A::class.java)
        startActivity(intent)
        finish()
    }
}