package com.george.copticorphanstask.firebase.google

import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.george.copticorphanstask.network.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import timber.log.Timber
import javax.inject.Inject

class FirebaseGoogleServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    override val googleSignInClient: GoogleSignInClient
) : FirebaseGoogleService {

    /**
     * ## Firebase auth with [Google]
     */
    override fun firebaseAuthWithGoogle(idToken: String): Resource<FirebaseUser?> {
        var resource: Resource<FirebaseUser?> = Resource.loading()

        val credential = GoogleAuthProvider.getCredential(idToken, null)

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
     * ## Activity Result Handler
     * ## Google Login
     */
    override fun activityResultHandlerForGoogleLogin(activityResult: ActivityResult): Resource<FirebaseUser?> {
        var resource: Resource<FirebaseUser?> = Resource.loading()

        GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            .addOnSuccessListener { account ->
                Timber.d("firebaseAuthWithGoogle: Account ID ${account.id}")
                Timber.d("firebaseAuthWithGoogle: Account ID Token ${account.idToken}")
                account.idToken?.let { token ->
                    resource = firebaseAuthWithGoogle(token)
                }
            }
            .addOnFailureListener { e ->
                Timber.w("Google sign in failed", e)
                resource = Resource.failed(e.localizedMessage ?: "Google sign in failed")
            }
            .addOnCanceledListener {
                Timber.w("Canceled")
                resource = Resource.error("Canceled")
            }

        return resource
    }


}