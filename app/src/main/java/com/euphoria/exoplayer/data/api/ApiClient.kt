package com.euphoria.exoplayer.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.pexels.com/"
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    "QguQQeHbjfTWtWjm3EepmYTrNyXbHz4dTS1pmonIh8iEnPZokaxcaOBX"
                )
                .build()
            chain.proceed(request)
        }
        .build()

    val api: PexelsApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PexelsApi::class.java)
}
