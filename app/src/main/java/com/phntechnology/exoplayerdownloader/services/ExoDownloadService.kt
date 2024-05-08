package com.phntechnology.exoplayerdownloader.services

import android.app.Notification
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.Requirements.RequirementFlags
import androidx.media3.exoplayer.scheduler.Scheduler
import com.phntechnology.exoplayerdownloader.BaseApplication
import com.phntechnology.exoplayerdownloader.R
import com.phntechnology.exoplayerdownloader.util.getDownloadNotificationHelper
import com.phntechnology.exoplayerdownloader.util.toastMsg


@OptIn(markerClass = [UnstableApi::class])
class ExoDownloadService() : DownloadService(
    1,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    "ExoDemo",
    R.string.app_name,
    R.string.app_describe
) {


    private lateinit var notificationHelper: DownloadNotificationHelper
    private lateinit var context: Context


    override fun onCreate() {
        super.onCreate()
        context = this
        notificationHelper = DownloadNotificationHelper(this, "ExoDemo")

    }


    override fun getDownloadManager(): DownloadManager {
        val manager = (application as BaseApplication).appContainer.downloadManager
        //Set the maximum number of parallel downloads
        manager.maxParallelDownloads = 3
        manager.addListener(object : DownloadManager.Listener {
            override fun onDownloadsPausedChanged(
                downloadManager: DownloadManager,
                isPaused: Boolean
            ) {
                if (isPaused) {
                    toastMsg("paused")
                } else {
                    toastMsg("resumed")
                }

            }

            override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
                super.onDownloadRemoved(downloadManager, download)
            }

            override fun onDownloadChanged(
                downloadManager: DownloadManager,
                download: Download,
                finalException: Exception?
            ) {
                super.onDownloadChanged(downloadManager, download, finalException)
            }

        })
        return manager

    }

    override fun getScheduler(): Scheduler? {
        return null
    }

    override fun getForegroundNotification(
        downloads: List<Download>, notMetRequirements: @RequirementFlags Int
    ): Notification {
        return getDownloadNotificationHelper().buildProgressNotification(
            this,
            R.drawable.ic_download,
            null,
            null,
            downloads,
            notMetRequirements
        )
    }


}