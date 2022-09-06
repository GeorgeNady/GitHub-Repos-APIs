package com.george.copticorphanstask.firebase.facebook

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.george.copticorphanstask.network.Resource
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber
import javax.inject.Inject

class FirebaseFacebookServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    override val callbackManager: CallbackManager,
    private val loginManager: LoginManager
) : FirebaseFacebookService {


    /**
     * ## Firebase auth with [Facebook]
     */
    override fun handleFacebookAccessToken(token: AccessToken): Resource<FirebaseUser?> {
        var resource: Resource<FirebaseUser?> = Resource.loading()

        val credential = FacebookAuthProvider.getCredential(token.token)
        Timber.d("handleFacebookAccessToken: $token")

        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                Timber.d("signInWithCredential:success")
                val user = auth.currentUser
                resource = Resource.success(user)
            }
            .addOnFailureListener { exception ->
                Timber.w("signInWithCredential:failure $exception")
                resource = Resource.failed(exception.localizedMessage ?: "")
            }
            .addOnCanceledListener {
                Timber.w("signInWithCredential:canceled")
                resource = Resource.error("Canceled")
            }

        return resource
    }


    /**
     * ## Facebook Login
     */
    override fun registerCallBack(fragment: Fragment): LiveData<Resource<FirebaseUser?>> {
        val mutableLiveData = MutableLiveData<Resource<FirebaseUser?>>()

        with(loginManager) {
            logInWithReadPermissions(
                fragment, //This is an androidx.fragment.app.Fragment
                listOf("public_profile", "email")
            )
            registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val accessToken = result.accessToken
                    Timber.i("Facebook Access Token >>> $accessToken")
                    mutableLiveData.value = handleFacebookAccessToken(accessToken)
                }

                override fun onCancel() {
                    mutableLiveData.value = Resource.failed("Canceled")
                }

                override fun onError(error: FacebookException) {
                    mutableLiveData.value = Resource.error(error.localizedMessage ?: "")
                }
            })
        }
        return mutableLiveData
    }

    fun disconnectFromFacebook() {
        // already logged out
        if (AccessToken.getCurrentAccessToken() == null) return
        val graphRequest = GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null,
            HttpMethod.DELETE,
            { loginManager.logOut() }
        )
        graphRequest.executeAsync()
    }
}