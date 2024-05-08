package com.phntechnology.exoplayerdownloader.helper

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class AuthInterceptor @Inject constructor(@ApplicationContext val appContext: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // we can write log url here
        val request = chain.request().newBuilder()
        request.addHeader("Accept", "application/json")
        // add token here with the header
        return chain.proceed(request.build())
    }

}