package com.george.copticorphanstask.ui.auth.fragments

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.george.copticorphanstask.base.BaseFragment
import com.george.copticorphanstask.databinding.FragmentSplashBinding
import com.george.copticorphanstask.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private val binding by lazy { FragmentSplashBinding.inflate(layoutInflater) }
    private val authViewModel by activityViewModels<AuthViewModel>()

    private val animatorListener by lazy {
        object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                Timber.d("navigate to auth methods fragment")
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAuthMethodsFragment())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = this@SplashFragment
            authViewModel
            animationView.addAnimatorListener(animatorListener)
        }
    }

}