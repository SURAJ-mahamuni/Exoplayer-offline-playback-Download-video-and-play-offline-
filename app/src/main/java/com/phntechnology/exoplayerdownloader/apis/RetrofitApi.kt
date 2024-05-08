package com.phntechnology.exoplayerdownloader.apis

import com.phntechnology.exoplayerdownloader.model.DemoModel
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitApi {
    @GET("end_point")
    suspend fun getData(): Response<DemoModel>

}