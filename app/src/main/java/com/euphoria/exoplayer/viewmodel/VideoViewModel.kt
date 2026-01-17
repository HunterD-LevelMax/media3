package com.euphoria.exoplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.euphoria.exoplayer.data.model.UiVideo
import com.euphoria.exoplayer.data.repository.VideoRepository
import kotlinx.coroutines.flow.Flow

class VideoViewModel : ViewModel() {

    private val repository = VideoRepository()

    val videos: Flow<PagingData<UiVideo>> =
        repository.getVideos()
            .cachedIn(viewModelScope)
}
