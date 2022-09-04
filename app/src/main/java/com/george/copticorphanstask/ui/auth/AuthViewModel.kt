package com.george.copticorphanstask.ui.auth

import androidx.activity.result.ActivityResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.repository.AuthRepo
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val repo: AuthRepo
) : ViewModel() {

    private val _user = MutableLiveData<Resource<FirebaseUser?>>()
    val user: LiveData<Resource<FirebaseUser?>> get() = _user

    init {
        checkLoggedInUser()
    }

    // B ************************************************************************* {Check for users}
    // DONE
    private fun checkLoggedInUser() {
        _user.value = Resource.loading()
        val currentUser = repo.auth.currentUser
        _user.value =
            if (currentUser != null) Resource.success(currentUser)
            else Resource.error("no users found")
    }

    // B ******************************************************************************** {FACEBOOK}
    val facebookCallbackManager = repo.callbackManager
    fun loginWithFacebook(fragment: Fragment) {
        repo.activityResultHandlerForFacebookLogin(fragment)
    }

    // B *********************************************************************************** {GMAIL}
    fun googleSignInIntent() = repo.googleSignInIntent()

    fun loginWithGmail(activityResult: ActivityResult) {
        Timber.i("activityResult.data: ${activityResult.data}")
        Timber.i("activityResult.resultCode: ${activityResult.resultCode}")
        _user.value = repo.activityResultHandlerForGoogleLogin(activityResult).value
    }

    // B ************************************************************************ {Email & Password}

    // B ******************************************************************************** {Register}

    // B ********************************************************************************* {Log out}
    // DONE
    fun logout() {
        Timber.i("Logging out")
        repo.auth.signOut()
    }

}