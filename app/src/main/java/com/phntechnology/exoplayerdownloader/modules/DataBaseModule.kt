package com.phntechnology.exoplayerdownloader.modules

import android.content.Context
import androidx.room.Room
import com.phntechnology.exoplayerdownloader.database.ExoDownloadDao
import com.phntechnology.exoplayerdownloader.database.ExoDownloadDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideChannelDao(roomDatabase: ExoDownloadDataBase): ExoDownloadDao {
        return roomDatabase.getExoDownloadDao()
    }


    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext appContext: Context): ExoDownloadDataBase {
        return Room.databaseBuilder(
            appContext,
            ExoDownloadDataBase::class.java,
            "exoplayer_download"
        ).build()
    }
}