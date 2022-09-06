package com.george.copticorphanstask.firebase.facebook

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.george.copticorphanstask.network.Resource
import com.google.firebase.auth.FirebaseUser

interface FirebaseFacebookService {
    val callbackManager: CallbackManager
    fun handleFacebookAccessToken(token: AccessToken): Resource<FirebaseUser?>
    fun registerCallBack(fragment: Fragment): LiveData<Resource<FirebaseUser?>>
    // fun disconnectFromFacebook()
}