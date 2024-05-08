package com.phntechnology.exoplayerdownloader.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ExoDownloadingHistory")
data class ExoDownloadingHistory(
    @PrimaryKey val contentId: String,
    val videoName: String,
    val notificationFlag: Boolean,
    val downloadingPercent: Double
    )
