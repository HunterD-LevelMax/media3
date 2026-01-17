package com.euphoria.exoplayer.data.cache

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache

object CacheDataSourceFactory {

    @OptIn(UnstableApi::class)
    fun build(context: Context): CacheDataSource.Factory {
        val cache: SimpleCache = VideoCache.getInstance(context)

        val upstreamFactory = DefaultDataSource.Factory(context)

        return CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }
}
