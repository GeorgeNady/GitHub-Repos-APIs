package com.george.copticorphanstask.ui.auth

import androidx.activity.result.ActivityResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.george.copticorphanstask.firebase.google.FirebaseGoogleService
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.repository.AuthRepo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepo
) : ViewModel() {

    private val _facebookLogin = MutableLiveData<Resource<FirebaseUser?>>()
    private val _googleLogin = MutableLiveData<Resource<FirebaseUser?>>()
    private val _loginMutableLiveData = MutableLiveData<Resource<FirebaseUser?>>()
    private val _signupMutableLiveData = MutableLiveData<Resource<FirebaseUser?>>()

    val googleLogin: LiveData<Resource<FirebaseUser?>> get() = _googleLogin
    val facebookLogin: LiveData<Resource<FirebaseUser?>> get() = _facebookLogin
    val loginLiveData: LiveData<Resource<FirebaseUser?>> get() = _loginMutableLiveData
    val signupLiveData: LiveData<Resource<FirebaseUser?>> get() = _signupMutableLiveData

    // info ***************************************************************************** {FACEBOOK}
    val facebookCallbackManager = repo.callbackManager
    fun loginWithFacebook(fragment: Fragment) {
        _facebookLogin.value = repo.activityResultHandlerForFacebookLogin(fragment).value
    }

    // info ******************************************************************************** {GMAIL}
    val googleSignInIntent = repo.googleService.googleSignInClient.signInIntent
    fun loginWithGmail(idToken: String) {
        _googleLogin.value = Resource.loading()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        repo.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success")
                    val user = repo.auth.currentUser
                    _googleLogin.value = Resource.success(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w("signInWithCredential:failure", task.exception)
                    _googleLogin.value = Resource.error("${task.exception}")
                }
            }
    }


    // info ********************************************************************* {Email & Password}
    fun signupFormValid(email:String, password:String, rePassword: String): Boolean {
        if (email.isNotEmpty() && password.length >= 8 && password == rePassword) return true
        return false
    }

    /**
     * # [Signup][FirebaseAuth.createUserWithEmailAndPassword]
     * */
    fun signup(email: String, password: String, rePassword: String, onComplete: (() -> Unit)? = null) {
        _signupMutableLiveData.postValue(Resource.loading())
        if (signupFormValid(email, password, rePassword)) {
            repo.auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    Timber.d("login: ${result.user}")
                    _signupMutableLiveData.postValue(Resource.success(result.user))
                }
                .addOnFailureListener { e ->
                    Timber.e("login: $e")
                    _signupMutableLiveData.postValue(Resource.error(e.toString()))
                }
                .addOnCompleteListener {
                    onComplete?.let { it() }
                }
        } else {
            _signupMutableLiveData.postValue(Resource.error("please enter inputs correctly"))
        }

    }


    // info ******************************************************************************** {Login}
    fun loginFormValid(email:String, password:String): Boolean {
        if (email.isNotEmpty() && password.length >= 8) return true
        return false
    }

    /**
     * # [Login][FirebaseAuth.createUserWithEmailAndPassword]
     * */
    fun login(email: String, password: String, onComplete: (() -> Unit)? = null) {
        _loginMutableLiveData.postValue(Resource.loading())
        if (loginFormValid(email, password)) {
            repo.auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    Timber.d("login: ${result.user}")
                    _loginMutableLiveData.postValue(Resource.success(result.user))
                }
                .addOnFailureListener { e ->
                    Timber.e("login: $e")
                    _loginMutableLiveData.postValue(Resource.error(e.toString()))
                }
                .addOnCompleteListener {
                    onComplete?.let { it() }
                }
        } else {
            _loginMutableLiveData.postValue(Resource.error("please enter inputs correctly"))
        }

    }

    // info ****************************************************************************** {Log out}
    // DONE
    fun logout() {
        Timber.i("Logging out")
        repo.auth.signOut()
    }

}