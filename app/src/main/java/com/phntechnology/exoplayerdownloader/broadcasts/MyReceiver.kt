package com.phntechnology.exoplayerdownloader.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("message")
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}