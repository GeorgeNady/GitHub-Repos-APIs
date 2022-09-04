package com.george.copticorphanstask.ui.auth

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.repository.AuthRepo
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val repo: AuthRepo
) : ViewModel() {

    private val _user = MutableLiveData<Resource<FirebaseUser?>>()
    val user: LiveData<Resource<FirebaseUser?>> get() = _user


    // TODO #1: login with facebook
    // B ******************************************************************************** {FACEBOOK}
    fun facebookLogin(fragment: Fragment) {
        _user.value = Resource.loading()
        with(repo) {
            facebookLoginManager.apply {
                logInWithReadPermissions(fragment, setOf("email", "public_profile"))
                registerCallback(callbackManager, facebookCallback)
            }
        }
    }

    private val facebookCallback by lazy {
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Timber.i("facebookLogin success")
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                Timber.i("facebookLogin cancel")
            }

            override fun onError(error: FacebookException) {
                Timber.d("facebookLogin error: ${error.message}")
            }
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Timber.d("handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        repo.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success")
                    val user = repo.auth.currentUser
                    _user.value = Resource.success(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w("signInWithCredential:failure", task.exception)
                    _user.value = Resource.failed(task.exception?.localizedMessage ?: "")
                }
            }
    }

    // TODO #1: login with gmail

    // TODO #1: login with email and password

    // TODO #1: register

    // TODO #1: login out
    fun logout() = repo.auth.signOut()

}