package com.george.copticorphanstask.di

import android.content.Context
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.george.copticorphanstask.R
import com.george.copticorphanstask.firebase.facebook.FirebaseFacebookService
import com.george.copticorphanstask.firebase.facebook.FirebaseFacebookServiceImpl
import com.george.copticorphanstask.firebase.google.FirebaseGoogleService
import com.george.copticorphanstask.firebase.google.FirebaseGoogleServiceImpl
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideGoogleSignInOption(
        @ApplicationContext context: Context
    ) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_clint_id))
        .requestEmail()
        .build()

    @Provides
    @Singleton
    fun provideGoogleSignInClint(
        @ApplicationContext context: Context,
        gso: GoogleSignInOptions
    ) = GoogleSignIn.getClient(context, gso)

    @Provides
    fun provideFaceBookCallbackManager() = CallbackManager.Factory.create()

    @Provides
    fun provideFacebookLoginManager(): LoginManager = LoginManager.getInstance()

    @Provides
    fun provideFirebaseAuth() = Firebase.auth


    @Provides
    fun provideFireBaseFacebookService(
        auth: FirebaseAuth,
        callbackManager: CallbackManager,
        loginManager: LoginManager
    ): FirebaseFacebookService = FirebaseFacebookServiceImpl(auth, callbackManager, loginManager)

    @Provides
    fun provideFireBaseGoogleService(
        auth: FirebaseAuth,
        googleSignInClient: GoogleSignInClient
    ): FirebaseGoogleService = FirebaseGoogleServiceImpl(auth, googleSignInClient)

}