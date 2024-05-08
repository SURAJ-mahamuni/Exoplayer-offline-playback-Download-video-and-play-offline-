package com.phntechnology.exoplayerdownloader

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.phntechnology.exoplayerdownloader.downloadVideo.AppContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    lateinit var appContainer: AppContainer
    var ID = "ExoDemo"
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(ID, "ExoDemo", NotificationManager.IMPORTANCE_HIGH )
            channel.description = getString(R.string.app_describe)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}