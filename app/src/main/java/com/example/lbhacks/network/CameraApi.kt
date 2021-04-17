package com.example.lbhacks.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "https://gen-flask.herokuapp.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CameraApiService {
    @GET("/problems")
    suspend fun getProblems(): Map<String, String>

    @POST("/problems")
    suspend fun addProblem(): Map<String, String>

    @POST("/checkSolution")
    suspend fun checkSolution(): Map<String, String>
}

object CameraApi {
    val retrofitService: CameraApiService by lazy {
        retrofit.create(CameraApiService::class.java)
    }
}