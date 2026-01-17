package com.euphoria.exoplayer.data.api

import com.euphoria.exoplayer.data.model.PexelsVideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApi {
    @GET("videos/popular")
    suspend fun getPopularVideos(
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int
    ): PexelsVideoResponse

}