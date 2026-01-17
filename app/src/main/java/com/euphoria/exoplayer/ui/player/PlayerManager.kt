package com.euphoria.exoplayer.ui.player

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import com.euphoria.exoplayer.R
import com.euphoria.exoplayer.data.cache.CacheDataSourceFactory
import com.euphoria.exoplayer.ui.VideoAdapter

@UnstableApi
class PlayerManager(
    context: Context,
    private val adapter: VideoAdapter
) {

    private val mediaSourceFactory = ProgressiveMediaSource.Factory(
        CacheDataSourceFactory.build(context)
    )

    private val playerPool = List(PLAYER_POOL_SIZE) {
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .apply {
                repeatMode = Player.REPEAT_MODE_ONE
                volume = 1f
            }
    }

    private var currentPoolIndex = 0
    private var currentView: PlayerView? = null
    private var currentUrl: String? = null
    private var currentPosition = -1

    private val currentPlayer: ExoPlayer get() = playerPool[currentPoolIndex]

    fun play(
        view: PlayerView,
        url: String,
        position: Int
    ) {
        if (position >= adapter.itemCount) return
        if (url == currentUrl && position == currentPosition) return

        currentUrl = url
        currentPosition = position

        currentView?.player = null

        currentPoolIndex = (currentPoolIndex + 1) % PLAYER_POOL_SIZE
        val player = playerPool[currentPoolIndex]

        resetPlayer(player)

        val mediaItem = MediaItem.fromUri(url)
        player.setMediaSource(mediaSourceFactory.createMediaSource(mediaItem))

        player.prepare()
        player.playWhenReady = true

        view.player = player
        currentView = view

        preloadNext(position + 1)
        preloadNext(position + 2)
    }

    private fun preloadNext(position: Int) {
        val item = adapter.peek(position) ?: return
        val distance = position - currentPosition
        if (distance !in 1..2) return

        val preloadIndex = (currentPoolIndex + distance) % PLAYER_POOL_SIZE
        val preloadPlayer = playerPool[preloadIndex]

        preloadPlayer.playWhenReady = false
        preloadPlayer.stop()
        preloadPlayer.clearMediaItems()
        preloadPlayer.volume = 0f

        val mediaItem = MediaItem.fromUri(item.videoUrl)
        val mediaSource = mediaSourceFactory.createMediaSource(mediaItem)

        resetPlayer(preloadPlayer)
        preloadPlayer.setMediaSource(mediaSource)
        preloadPlayer.prepare()

        preloadPlayer.volume = 0f
    }

    fun pause() {
        currentPlayer.playWhenReady = false
    }

    private fun resetPlayer(player: ExoPlayer) {
        player.playWhenReady = false
        player.stop()
        player.clearMediaItems()
        player.seekTo(0)
        player.volume = 0f
        player.repeatMode = Player.REPEAT_MODE_ONE
    }

    fun release() {
        currentView?.player = null
        playerPool.forEach { it.release() }
        currentView = null
        currentUrl = null
        currentPosition = -1
    }

    companion object {
        private const val PLAYER_POOL_SIZE = 3
    }
}