package com.euphoria.exoplayer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.euphoria.exoplayer.R
import com.euphoria.exoplayer.data.model.UiVideo
import com.euphoria.exoplayer.databinding.ItemVideoBinding

@OptIn(UnstableApi::class)
class VideoAdapter : PagingDataAdapter<UiVideo, VideoAdapter.VideoVH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<UiVideo>() {
            override fun areItemsTheSame(old: UiVideo, new: UiVideo) = old.videoUrl == new.videoUrl
            override fun areContentsTheSame(old: UiVideo, new: UiVideo) = old == new
        }
    }

    inner class VideoVH(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVH {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VideoVH(binding)
    }

    override fun onBindViewHolder(holder: VideoVH, position: Int) {
        val item = getItem(position) ?: return

        with(holder.binding) {
            tvAuthor.text =
                root.context.getString(R.string.video_by, item.authorName)
            playerView.player = null
        }
    }

}
