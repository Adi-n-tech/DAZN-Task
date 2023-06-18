package com.app.dazn.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.dazn.R
import com.app.dazn.data.Analytics
import com.app.dazn.databinding.FragmentPlayerBinding
import com.app.dazn.viewmodel.VideoViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private lateinit var videoUrl: String
    private lateinit var exoplayer: ExoPlayer
    private lateinit var analytics: Analytics

    //-----
    private val videoViewModel: VideoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //------
        val bundle = this.arguments
        videoUrl = bundle!!.getString("videoUrl", "")
        analytics = Analytics("0", "0", "0")
        // Initialize ExoPlayer
        exoplayer = SimpleExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = exoplayer

        val concatenatedSource = ConcatenatingMediaSource()
        val firstMediaSource = ProgressiveMediaSource.Factory(DefaultHttpDataSourceFactory())
            .createMediaSource(Uri.parse(videoUrl))
        concatenatedSource.addMediaSource(firstMediaSource)
        videoViewModel.videoList.observe(viewLifecycleOwner) { videoList ->
            for (videoItem in videoList) {
                if (videoItem.uri != videoUrl) {
                    val mediaSource = ProgressiveMediaSource.Factory(DefaultHttpDataSourceFactory())
                        .createMediaSource(Uri.parse(videoItem.uri))
                    concatenatedSource.addMediaSource(mediaSource)
                }
            }
            exoplayer.prepare(concatenatedSource)
        }
        exoplayer.playWhenReady = true
        //-----
        exoplayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    // Player is playing
                } else {
                    videoViewModel.analytics.value?.pause_count =
                        videoViewModel.analytics.value?.pause_count?.toInt()?.plus(1).toString()
                    videoViewModel.logAnalytics()
                }
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo, newPosition: Player.PositionInfo, reason: Int
            ) {
                if (reason == Player.DISCONTINUITY_REASON_SEEK) {
                    if (newPosition.windowIndex > oldPosition.windowIndex) {
                        videoViewModel.analytics.value?.forward_count =
                            videoViewModel.analytics.value?.forward_count?.toInt()?.plus(1)
                                .toString()
                        videoViewModel.logAnalytics()
                    } else if (newPosition.windowIndex < oldPosition.windowIndex) {
                        videoViewModel.analytics.value?.backward_count =
                            videoViewModel.analytics.value?.backward_count?.toInt()?.plus(1)
                                .toString()
                        videoViewModel.logAnalytics()
                    }
                }
            }
        })
        //----
        videoViewModel.analytics.observe(viewLifecycleOwner) {
            if (it != null) {
                analytics = it
                binding.pauseCount.text = "Pause Count: ${it.pause_count}"
                binding.forwardCount.text = "Forward Count: ${it.forward_count}"
                binding.backwardCount.text = "Backward Count: ${it.backward_count}"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoplayer.stop()
    }

    override fun onResume() {
        super.onResume()
        exoplayer.stop()
    }
}