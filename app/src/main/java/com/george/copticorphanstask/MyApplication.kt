package com.george.copticorphanstask

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var mApplication: Application
    }

    override fun onCreate() {
        super.onCreate()
        mApplication = this
        setupTimber()
    }

    /**
     * # [Timber] for debugging logging
     */
    private fun setupTimber() {
        // initialize timber in application class
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}