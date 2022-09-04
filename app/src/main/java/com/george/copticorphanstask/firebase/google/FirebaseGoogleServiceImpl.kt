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
    override fun activityResultHandlerForGoogleLogin(activityResult: ActivityResult): LiveData<Resource<FirebaseUser?>> {
        val mutableLiveData = MutableLiveData<Resource<FirebaseUser?>>()
        GoogleSignIn.getSignedInAccountFromIntent(activityResult.data).addOnCompleteListener {
            try {
                val account = it.getResult(ApiException::class.java)
                Timber.d("firebaseAuthWithGoogle: Account ID ${account.id}")
                Timber.d("firebaseAuthWithGoogle: Account ID Token ${account.idToken}")
                account.idToken?.let { token ->
                    mutableLiveData.value = firebaseAuthWithGoogle(token)
                }
            } catch (e: ApiException) {
                Timber.w("Google sign in failed", e)
                mutableLiveData.value =
                    Resource.error(e.localizedMessage ?: "Google sign in failed")
            }
        }
        return mutableLiveData
    }


}