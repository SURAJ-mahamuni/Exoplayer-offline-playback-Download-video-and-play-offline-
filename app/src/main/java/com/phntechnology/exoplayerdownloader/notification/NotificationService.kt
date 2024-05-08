package com.phntechnology.exoplayerdownloader.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.phntechnology.exoplayerdownloader.R
import com.phntechnology.exoplayerdownloader.activity.MainActivity
import com.phntechnology.exoplayerdownloader.broadcasts.MyReceiver
import com.phntechnology.exoplayerdownloader.util.Constants.DOWNLOAD_NOTIFICATION_CHANNEL_ID
import javax.inject.Inject


class NotificationService @Inject constructor(val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createNotification(name: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                DOWNLOAD_NOTIFICATION_CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = channelDescription


            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showBasicNotification(title: String, content: String) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val notification = NotificationCompat.Builder(context, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_message)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(activityPendingIntent)
            .build()
        notificationManager.notify(1, notification)
    }

    fun showActionNotification() {
        val activityIntent = Intent(context, MyReceiver::class.java).apply {
            putExtra("message", "stop")
        }
        val receiverPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val notification = NotificationCompat.Builder(context, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_message)
            .setContentTitle("Phn E-Learning")
            .setContentText("your song is playing...")
            .addAction(0, "Stop", receiverPendingIntent)
            .build()
        notificationManager.notify(1, notification)
    }
}