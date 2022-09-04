package com.george.copticorphanstask.di

import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideFaceBookCallbackManager() = CallbackManager.Factory.create()

    @Provides
    fun provideFacebookLoginManager(): LoginManager = LoginManager.getInstance()

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

}