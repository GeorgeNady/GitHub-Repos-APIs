package com.george.copticorphanstask.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.george.copticorphanstask.base.BaseFragment
import com.george.copticorphanstask.databinding.FragmentLoginBinding
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.ui.auth.AuthViewModel
import com.george.copticorphanstask.ui.main.MainActivity
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val authViewModel by activityViewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = this@LoginFragment

            btnSignin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                authViewModel.login(email ,password)
            }

            btnSignUp.setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToSignupFragment())
            }

            authViewModel.loginLiveData.observe(viewLifecycleOwner, loginObserver())
        }
    }


    private fun loginObserver() = Observer<Resource<FirebaseUser?>> { res ->
        res.handler(
            mLoading = { Toast.makeText(requireContext(), "login...", Toast.LENGTH_SHORT).show()},
            mFailed = { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()},
            mError = { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()},
        ) {
            startActivity<MainActivity>(replace = true)
        }
    }

}