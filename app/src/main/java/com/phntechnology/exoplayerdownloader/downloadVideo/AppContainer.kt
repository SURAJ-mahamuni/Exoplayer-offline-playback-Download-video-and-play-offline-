package com.phntechnology.exoplayerdownloader.downloadVideo

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadManager
import java.io.File
import java.util.concurrent.Executor

@OptIn(markerClass = [UnstableApi::class])
class AppContainer(val context: Context) {

    private var dataSource = DefaultHttpDataSource.Factory()

    var dataBase = StandaloneDatabaseProvider(context)
    private var downloadContentDirectory: File
    var downloadCache: Cache
    var downloadManager: DownloadManager


    init {
        downloadContentDirectory = File(context.getExternalFilesDir(null), "ExoDemoFiles")
        downloadCache = SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), dataBase)
        val downloadExecutor = Executor(Runnable::run)
        downloadManager = DownloadManager(
            context,
            dataBase,
            downloadCache,
            dataSource,
            downloadExecutor
        )
    }

}