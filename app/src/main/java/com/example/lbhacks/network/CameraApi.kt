package com.example.lbhacks.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

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

    @Multipart
    @POST("/im_size")
    suspend fun sendImageTest(
        @Part file: MultipartBody.Part
    ): Map<String, Array<Int>>
}

object CameraApi {
    val retrofitService: CameraApiService by lazy {
        retrofit.create(CameraApiService::class.java)
    }
}