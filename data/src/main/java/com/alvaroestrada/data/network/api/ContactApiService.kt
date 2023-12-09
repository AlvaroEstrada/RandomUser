package com.alvaroestrada.data.network.api

import com.alvaroestrada.data.models.ResponseRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ContactApiService {
    @GET("api/")
    suspend fun getContacts(@Query("page") page: Int, @Query("results") results: Int): Response<ResponseRemote>
}