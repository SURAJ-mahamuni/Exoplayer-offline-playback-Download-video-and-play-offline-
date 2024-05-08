package com.phntechnology.exoplayerdownloader.downloadVideo

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import com.phntechnology.exoplayerdownloader.services.ExoDownloadService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

@OptIn(markerClass = [UnstableApi::class])
class ExoDownloadManager @Inject constructor(@ApplicationContext private val context: Context) {

    var contentId: String? = null

    fun startDownload(contentUri: String) {
        contentId = UUID(0L, 1000L).toString()

        Log.e("id", contentId ?: "")

        val downloadRequest =
            DownloadRequest.Builder(contentId ?: "", Uri.parse(contentUri)).build()


        DownloadService.sendAddDownload(
            context,
            ExoDownloadService::class.java,
            downloadRequest,
            /* foreground= */ false
        )
    }

    fun getProgress() {

    }

    fun removeDownload() {
        DownloadService.sendRemoveDownload(
            context,
            ExoDownloadService::class.java,
            contentId ?: "",
            /* foreground= */ false
        )
    }

    fun pauseAllDownloading() {
        DownloadService.sendPauseDownloads(
            context,
            ExoDownloadService::class.java,
            /* foreground= */ false
        )
    }

    fun resumeAllDownloading() {
        DownloadService.sendPauseDownloads(
            context,
            ExoDownloadService::class.java,
            /* foreground= */ false
        )
    }

}