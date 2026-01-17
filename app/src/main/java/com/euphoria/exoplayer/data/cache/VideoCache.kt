package com.euphoria.exoplayer.data.cache

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@UnstableApi
object VideoCache {

    private const val MAX_CACHE_SIZE = 300L * 1024 * 1024 // 300 MB

    private var cache: SimpleCache? = null

    fun getInstance(context: Context): SimpleCache {
        return cache ?: synchronized(this) {
            cache ?: run {
                val cacheDir = File(context.cacheDir, "video_cache")
                val evictor = LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE)
                SimpleCache(cacheDir, evictor).also {
                    cache = it
                }
            }
        }
    }
}