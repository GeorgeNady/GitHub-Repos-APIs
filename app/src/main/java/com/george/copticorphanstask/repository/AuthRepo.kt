package com.george.copticorphanstask.repository

import androidx.activity.result.ActivityResult
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.george.copticorphanstask.firebase.facebook.FirebaseFacebookService
import com.george.copticorphanstask.firebase.google.FirebaseGoogleService
import com.george.copticorphanstask.network.BaseDataSource
import com.george.copticorphanstask.network.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthRepo @Inject constructor(
    val auth: FirebaseAuth,
    private val googleService: FirebaseGoogleService,
    private val facebookService: FirebaseFacebookService,
    val callbackManager: CallbackManager
) : BaseDataSource() {

    ///////////////////////////////////////////////////////////////////////////////////////// GOOGLE
    val googleSignInIntent = googleService.googleSignInClient.signInIntent

    fun activityResultHandlerForGoogleLogin(activityResult: ActivityResult) =
        googleService.activityResultHandlerForGoogleLogin(activityResult)

    /////////////////////////////////////////////////////////////////////////////////////// FACEBOOK
    fun activityResultHandlerForFacebookLogin(fragment: Fragment) =
        facebookService.registerCallBack(fragment)

    ///////////////////////////////////////////////////////////////////////////////////////// SIGNUP
    fun createAccountWithEmailAndPassword(email: String, password: String): Resource<AuthResult> {
        var resource: Resource<AuthResult> = Resource.loading()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                resource = Resource.success(it)
            }
            .addOnFailureListener {
                resource = Resource.failed(it.message ?: "failure")
            }
            .addOnCanceledListener {
                resource = Resource.error("canceled")
            }
        return resource
    }

    //////////////////////////////////////////////////////////////////////////////////////// SING_IN
    fun signInWithEmailANdPassword(email: String, password: String): Resource<AuthResult> {
        var resource: Resource<AuthResult> = Resource.loading()
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                resource = Resource.success(it)
            }
            .addOnFailureListener {
                resource = Resource.failed(it.message ?: "failure")
            }
            .addOnCanceledListener {
                resource = Resource.error("canceled")
            }
        return resource
    }

}