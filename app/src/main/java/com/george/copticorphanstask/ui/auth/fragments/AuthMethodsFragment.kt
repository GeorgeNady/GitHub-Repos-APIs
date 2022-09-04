package com.george.copticorphanstask.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.george.copticorphanstask.base.BaseFragment
import com.george.copticorphanstask.databinding.FragmentAuthMethodsBinding
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.ui.auth.AuthViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class AuthMethodsFragment : BaseFragment() {

    private val binding by lazy { FragmentAuthMethodsBinding.inflate(layoutInflater) }
    private val authViewModel by activityViewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = this@AuthMethodsFragment

            with(authViewModel) {

                btnGmail.setOnClickListener {

                }

                btnFacebook.setOnClickListener {
                    facebookLogin(this@AuthMethodsFragment)
                }

                btnSignUp.setOnClickListener {

                }

                btnLogin.setOnClickListener {

                }

                user.observe(viewLifecycleOwner, authObserver())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Pass the activity result back to the Facebook SDK
        authViewModel.repo.callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun authObserver() = Observer<Resource<FirebaseUser?>> {
        it.handler {
            // TODO: logic here
        }
    }

}