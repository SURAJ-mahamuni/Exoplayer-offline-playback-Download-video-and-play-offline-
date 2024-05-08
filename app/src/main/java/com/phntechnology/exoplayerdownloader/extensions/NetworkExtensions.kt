package com.phntechnology.exoplayerdownloader.extensions

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.phntechnology.exoplayerdownloader.util.NetworkUtils

object NetworkExtensions {
    fun Fragment.networkConnectivity(online: () -> Unit, offline: () -> Unit) {
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            online()
        } else {
            offline()
        }
    }

    fun Activity.networkConnectivity(online: () -> Unit, offline: () -> Unit) {
        if (NetworkUtils.isInternetAvailable(this)) {
            online()
        } else {
            offline()
        }
    }

    fun networkConnectivity(context: Context, online: () -> Unit, offline: () -> Unit) {
        if (NetworkUtils.isInternetAvailable(context)) {
            online()
        } else {
            offline()
        }
    }
}