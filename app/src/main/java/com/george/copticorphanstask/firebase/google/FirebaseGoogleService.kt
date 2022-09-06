package com.george.copticorphanstask.firebase.google

import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import com.george.copticorphanstask.network.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface FirebaseGoogleService {
    val googleSignInClient: GoogleSignInClient
    fun firebaseAuthWithGoogle(idToken: String): Task<AuthResult>
    fun activityResultHandlerForGoogleLogin(activityResult: ActivityResult): String
}