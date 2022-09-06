package com.george.copticorphanstask.firebase.google

import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.george.copticorphanstask.network.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
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
    override fun firebaseAuthWithGoogle(idToken: String): LiveData<Resource<FirebaseUser>> {
        val resource = MutableLiveData<Resource<FirebaseUser>>()
        resource.value = Resource.loading()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Timber.i("GOOGLE_LOGIN >>> #3_1")
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                Timber.i("GOOGLE_LOGIN >>> #3_2")
                val user = auth.currentUser
                resource.value =
                    if (user != null) Resource.success(user)
                    else Resource.error("User Not Found")

            }
            .addOnFailureListener { exception ->
                Timber.i("GOOGLE_LOGIN >>> #3_3")
                Timber.w("signInWithCredential:failure $exception")
                resource.value = Resource.failed(exception.localizedMessage ?: "")
            }
            .addOnCanceledListener {
                Timber.i("GOOGLE_LOGIN >>> #3_4")
                Timber.w("signInWithCredential:canceled")
                resource.value = Resource.error("Canceled")
            }
        Timber.i("GOOGLE_LOGIN >>> #3_5")
        return resource
    }

    /**
     * ## Activity Result Handler
     * ## Google Login
     */
    override suspend fun activityResultHandlerForGoogleLogin(activityResult: ActivityResult): LiveData<Resource<FirebaseUser>> {
        val mutableLiveData = MutableLiveData<Resource<FirebaseUser>>()
        mutableLiveData.value = Resource.loading()
        Timber.i("GOOGLE_LOGIN >>> #2_1")
        try {
            GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
                .addOnSuccessListener { account ->
                    account.idToken?.let { token ->
                        Timber.i("GOOGLE_LOGIN >>> #2_2")
                        mutableLiveData.value = firebaseAuthWithGoogle(token).value
                    }
                }
                .addOnFailureListener { e ->
                    Timber.i("GOOGLE_LOGIN >>> #2_3")
                    Timber.w("Google sign in failed", e)
                    mutableLiveData.value =
                        Resource.failed(e.localizedMessage ?: "Google sign in failed")
                }
                .addOnCanceledListener {
                    Timber.i("GOOGLE_LOGIN >>> #2_4")
                    Timber.w("Canceled")
                    mutableLiveData.value = Resource.error("Canceled")
                }
        } catch (e: Exception) {
            Timber.e("$e")
            mutableLiveData.value = Resource.error("Error $e")
        }

        Timber.i("GOOGLE_LOGIN >>> #2_5")
        return mutableLiveData
    }


}