package com.george.copticorphanstask.ui.auth.fragments

import androidx.lifecycle.ViewModel
import com.george.copticorphanstask.repository.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepo
) : ViewModel() {



}