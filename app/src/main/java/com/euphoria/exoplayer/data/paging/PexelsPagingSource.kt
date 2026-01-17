package com.euphoria.exoplayer.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.euphoria.exoplayer.data.api.ApiClient
import com.euphoria.exoplayer.data.model.UiVideo


class PexelsPagingSource : PagingSource<Int, UiVideo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiVideo> {
        return try {
            val page = params.key ?: 1

            val response = ApiClient.api.getPopularVideos(
                perPage = params.loadSize,
                page = page
            )

            val videos = response.videos.mapNotNull { video ->
                val file = video.video_files.firstOrNull { it.file_type == "video/mp4" }
                file?.let {
                    UiVideo(
                        videoUrl = it.link,
                        authorName = video.user.name,
                        thumbnailUrl = video.image
                    )
                }
            }

            LoadResult.Page(
                data = videos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (videos.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UiVideo>): Int? =
        state.anchorPosition
}

