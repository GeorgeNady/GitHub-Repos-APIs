package com.george.copticorphanstask.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.george.copticorphanstask.base.BaseFragment
import com.george.copticorphanstask.databinding.FragmentAuthMethodsBinding
import com.george.copticorphanstask.firebase.google.FirebaseGoogleService
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.ui.auth.AuthViewModel
import com.george.copticorphanstask.ui.main.MainActivity
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class AuthMethodsFragment : BaseFragment() {

    private val binding by lazy { FragmentAuthMethodsBinding.inflate(layoutInflater) }
    private val authViewModel by activityViewModels<AuthViewModel>()

    private val googleActivityResult = registerForActivityResult(StartActivityForResult()) {
        it?.let { authViewModel.loginWithGmail(it).observe(viewLifecycleOwner, googleLoginObserver()) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleOwner = this@AuthMethodsFragment

            with(authViewModel) {

                btnGmail.setOnClickListener {
                    googleActivityResult.launch(googleSignInIntent)
                }

                btnFacebook.setOnClickListener {
                    loginWithFacebook(this@AuthMethodsFragment)
                }

                btnSignUp.setOnClickListener {
                    findNavController().navigate(
                        AuthMethodsFragmentDirections.actionAuthMethodsFragmentToSignupFragment()
                    )
                }

                btnLogin.setOnClickListener {
                    findNavController().navigate(
                        AuthMethodsFragmentDirections.actionAuthMethodsFragmentToLoginFragment()
                    )
                }

                // googleLogin.observe(viewLifecycleOwner) { it.authObserver() }
                facebookLogin.observe(viewLifecycleOwner, facebookLoginObserver())
                /*successGoogleSignIn.observe(viewLifecycleOwner) {
                    if (it) startActivity<MainActivity>()
                }*/
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authViewModel.facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
    }


    private fun googleLoginObserver() = Observer<Resource<FirebaseUser>> { resources ->
        resources?.let {
            resources.handler {
                startActivity<MainActivity>(replace = true)
            }
        }
    }

    private fun facebookLoginObserver() = Observer<Resource<FirebaseUser?>> {
        it?.let {
            it.handler(
                mLoading = {
                    Toast.makeText(requireContext(), "facebook logging...", Toast.LENGTH_SHORT).show()
                },
                mError = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                },
                mFailed = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            ) { firebaseUser ->
                Timber.i("FirebaseUser >>>> ${firebaseUser?.email}")
                firebaseUser?.let {
                    if (it.email != null) startActivity<MainActivity>(replace = true)
                }
            }
        }
    }

}