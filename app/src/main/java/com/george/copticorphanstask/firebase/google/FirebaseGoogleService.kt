package com.george.copticorphanstask.firebase.google

import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import com.george.copticorphanstask.network.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser

interface FirebaseGoogleService {
    val googleSignInClient: GoogleSignInClient
    fun firebaseAuthWithGoogle(idToken: String): Resource<FirebaseUser?>
    fun activityResultHandlerForGoogleLogin(activityResult: ActivityResult): LiveData<Resource<FirebaseUser?>>
}