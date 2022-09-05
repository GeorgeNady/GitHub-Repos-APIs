package com.george.copticorphanstask.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.george.copticorphanstask.network.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth
):ViewModel() {

    private val _user = MutableLiveData<Resource<FirebaseUser?>>()

    val user: LiveData<Resource<FirebaseUser?>> get() = _user


    init {
        checkLoggedInUser()
    }

    // info ********************************************************************** {Check for users}
    // DONE
    private fun checkLoggedInUser() {
        _user.value = Resource.loading()
        val currentUser = auth.currentUser
        _user.value =
            if (currentUser != null) Resource.success(currentUser)
            else Resource.error("no users found")
    }

}
