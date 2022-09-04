@file:Suppress("DEPRECATION")

package com.george.copticorphanstask.repository

import androidx.fragment.app.Fragment
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.george.copticorphanstask.network.BaseDataSource
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import javax.inject.Inject

class AuthRepo @Inject constructor(
    val auth: FirebaseAuth,
    val callbackManager: CallbackManager,
    val facebookLoginManager: LoginManager,
) : BaseDataSource() {

    // B ******************************************************************************** {FACEBOOK}
    fun facebookLogin(fragment: Fragment) {
        val facebookCallback = object : FacebookCallback<LoginResult> {
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
        facebookLoginManager.apply {
            logInWithReadPermissions(fragment, setOf("email", "public_profile"))
            registerCallback(callbackManager, facebookCallback)
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Timber.d("handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Timber.d("signInWithCredential:success")
                val user = auth.currentUser
                // updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Timber.w("signInWithCredential:failure", task.exception)
                // updateUI(null)
            }
        }
    }

    fun disconnectFromFacebook() {
        // already logged out
        if (AccessToken.getCurrentAccessToken() == null) return
        val graphRequest = GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null,
            HttpMethod.DELETE,
            { facebookLoginManager.logOut() }
        )
        graphRequest.executeAsync()
    }

}