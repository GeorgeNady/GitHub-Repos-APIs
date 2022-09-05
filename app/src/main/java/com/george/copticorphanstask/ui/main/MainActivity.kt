package com.george.copticorphanstask.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.george.copticorphanstask.databinding.ActivityMainBinding
import com.george.copticorphanstask.ui.auth.AuthActivity
import com.george.copticorphanstask.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            btnLogout.setOnClickListener {
                authViewModel.logout()
                startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                finish()
            }
        }
    }

}