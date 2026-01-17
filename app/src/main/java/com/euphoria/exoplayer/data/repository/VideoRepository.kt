package com.euphoria.exoplayer.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.euphoria.exoplayer.data.paging.PexelsPagingSource

class VideoRepository {

    fun getVideos() =
        Pager(
            PagingConfig(
                pageSize = 15,
                prefetchDistance = 8,
                enablePlaceholders = true
            )
        ) { PexelsPagingSource() }.flow
}


