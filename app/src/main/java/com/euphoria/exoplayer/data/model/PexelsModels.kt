package com.euphoria.exoplayer.data.model

data class PexelsVideoResponse(
    val videos: List<PexelsVideo>
)

data class PexelsVideo(
    val id: Int,
    val url: String,
    val user: PexelsUser,
    val video_files: List<VideoFile>,
    val image: String
)


data class VideoFile(
    val link: String,
    val quality: String,
    val file_type: String
)

data class PexelsUser(
    val id: Int,
    val name: String
)

data class UiVideo(
    val videoUrl: String,
    val authorName: String,
    val thumbnailUrl: String? = null
)
