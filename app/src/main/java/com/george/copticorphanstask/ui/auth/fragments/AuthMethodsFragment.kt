package com.george.copticorphanstask.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.george.copticorphanstask.base.BaseFragment
import com.george.copticorphanstask.databinding.FragmentAuthMethodsBinding
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.ui.auth.AuthViewModel
import com.george.copticorphanstask.ui.main.MainActivity
import com.george.copticorphanstask.util.GOOGLE_RC_SIGN_IN
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class AuthMethodsFragment : BaseFragment() {

    private val binding by lazy { FragmentAuthMethodsBinding.inflate(layoutInflater) }
    private val authViewModel by activityViewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleOwner = this@AuthMethodsFragment

            with(authViewModel) {

                btnGmail.setOnClickListener {
                    startActivityForResult(googleSignInIntent, GOOGLE_RC_SIGN_IN)
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

                googleLogin.observe(viewLifecycleOwner, googleLoginObserver())
                facebookLogin.observe(viewLifecycleOwner, facebookLoginObserver())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authViewModel.facebookCallbackManager.onActivityResult(requestCode, resultCode, data)

        // ---------------------------------------------------------------------------------- GOOGLE
        if (requestCode == GOOGLE_RC_SIGN_IN) {
            val task  = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                Timber.d("firebaseAuthWithGoogle:" + account.id)
                account.idToken?.let { authViewModel.loginWithGmail(it) }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.w("Google sign in failed", e)
            }
        }
    }


    // TODO : handle error and loading and
    private fun googleLoginObserver(): Observer<Resource<FirebaseUser?>> {
        return Observer { it ->
            it?.let {
                it.handler(
                    mLoading = {
                        Toast.makeText(requireContext(), "google logging...", Toast.LENGTH_SHORT).show()
                               },
                    mError = { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                ) {
                    startActivity<MainActivity>(replace = true)
                }
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
                }
            ) {
                startActivity<MainActivity>(replace = true)
            }
        }
    }

}