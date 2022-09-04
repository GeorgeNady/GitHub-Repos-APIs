package com.george.copticorphanstask.ui.auth.fragments

import android.app.ProgressDialog
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
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.ui.auth.AuthViewModel
import com.george.copticorphanstask.ui.main.MainActivity
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class AuthMethodsFragment : BaseFragment() {

    private val binding by lazy { FragmentAuthMethodsBinding.inflate(layoutInflater) }
    private val authViewModel by activityViewModels<AuthViewModel>()

    private val googleActivityResult = registerForActivityResult(StartActivityForResult()) {
        it?.let { authViewModel.loginWithGmail(it) }
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
                    val intent = googleSignInIntent()
                    googleActivityResult.launch(intent)
                }

                btnFacebook.setOnClickListener {
                    loginWithFacebook(this@AuthMethodsFragment)
                }

                btnSignUp.setOnClickListener {
                    findNavController().navigate(
                        AuthMethodsFragmentDirections.actionAuthMethodsFragmentToSignupFragment())
                }

                btnLogin.setOnClickListener {
                    findNavController().navigate(
                        AuthMethodsFragmentDirections.actionAuthMethodsFragmentToLoginFragment())
                }

                user.observe(viewLifecycleOwner, authObserver())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authViewModel.facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun authObserver() = Observer<Resource<FirebaseUser?>> {
        it.handler(
            mLoading = {},
            mError = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            },
            mFailed = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        ) { firebaseUser ->
            Timber.i("FirebaseUser >>>> ${firebaseUser?.email}")
            if (firebaseUser != null) startActivity<MainActivity>(replace = true)
        }
    }

}