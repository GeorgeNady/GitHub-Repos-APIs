package com.george.copticorphanstask.firebase.google

import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.george.copticorphanstask.network.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
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
    override fun firebaseAuthWithGoogle(idToken: String): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.signInWithCredential(credential)
            .addOnSuccessListener {

            }

    }

    /**
     * ## Activity Result Handler
     * ## Google Login
     */
    override fun activityResultHandlerForGoogleLogin(activityResult: ActivityResult): String {
        var token: String? = null
        GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    token = task.result.idToken
                }
            }
        return token!!
    }


}