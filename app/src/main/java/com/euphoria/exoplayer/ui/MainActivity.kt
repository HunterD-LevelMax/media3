package com.euphoria.exoplayer.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.euphoria.exoplayer.databinding.ActivityMainBinding
import com.euphoria.exoplayer.ui.player.PlayerManager
import com.euphoria.exoplayer.viewmodel.VideoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@UnstableApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: VideoViewModel by viewModels()

    private lateinit var adapter: VideoAdapter
    private lateinit var playerManager: PlayerManager
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var pagerSnapHelper: PagerSnapHelper
    private var lastPlayedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = VideoAdapter()

        playerManager = PlayerManager(this, adapter)
        layoutManager = LinearLayoutManager(this)
        pagerSnapHelper = PagerSnapHelper()

        binding.recyclerView.apply {
            this.layoutManager = this@MainActivity.layoutManager
            this.adapter = this@MainActivity.adapter
            setHasFixedSize(true)
        }

        pagerSnapHelper.attachToRecyclerView(binding.recyclerView)

        lifecycleScope.launch {
            viewModel.videos.collectLatest { pagingData ->
                adapter.submitData(lifecycle, pagingData)
            }
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    playCurrentSnappedItem()
                }
            }
        })

        binding.recyclerView.addOnChildAttachStateChangeListener(
            object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    val holder = binding.recyclerView.getChildViewHolder(view)
                            as? VideoAdapter.VideoVH ?: return


                    if (holder.bindingAdapterPosition == 0) {
                        val video = adapter.peek(0) ?: return
                        playerManager.play(
                            holder.binding.playerView,
                            video.videoUrl,
                            0
                        )
                        lastPlayedPosition = 0

                        binding.recyclerView.removeOnChildAttachStateChangeListener(this)
                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) {}
            }
        )
    }

    private fun playCurrentSnappedItem() {
        val snapView = pagerSnapHelper.findSnapView(layoutManager) ?: return
        val position = layoutManager.getPosition(snapView)

        if (position == RecyclerView.NO_POSITION ||
            position == lastPlayedPosition ||
            position >= adapter.itemCount
        ) return

        val holder = binding.recyclerView
            .findViewHolderForAdapterPosition(position)
                as? VideoAdapter.VideoVH ?: return

        val video = adapter.peek(position) ?: return

        lastPlayedPosition = position

        playerManager.play(
            holder.binding.playerView,
            video.videoUrl,
            position
        )
    }


    override fun onStop() {
        super.onStop()
        playerManager.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager.release()
    }
}