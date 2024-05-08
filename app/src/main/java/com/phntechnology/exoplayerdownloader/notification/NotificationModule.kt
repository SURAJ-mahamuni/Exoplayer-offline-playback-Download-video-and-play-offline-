package com.phntechnology.exoplayerdownloader.notification

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(ActivityComponent::class)
@Module
class NotificationModule @Inject constructor() {

    @Provides
    @Singleton
    fun createNotification(context: Context) : NotificationService {
       return NotificationService(context)
    }
}