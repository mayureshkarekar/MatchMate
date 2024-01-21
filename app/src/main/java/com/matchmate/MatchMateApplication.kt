package com.matchmate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MatchMateApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Enabling debug logs.
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.tag("MatchMate")
        } else {
            Timber.uprootAll()
        }
    }
}