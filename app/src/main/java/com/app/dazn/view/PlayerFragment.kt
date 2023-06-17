package com.app.dazn.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.app.dazn.R
import com.app.dazn.databinding.FragmentPlayerBinding
import com.app.dazn.viewmodel.VideoViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private lateinit var videoUrl: String
    private lateinit var exoplayer: ExoPlayer
    //-----
    private val videoViewModel: VideoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //------
        val bundle = this.arguments
        videoUrl = bundle!!.getString("videoUrl", "")
        Log.d("Aditya", videoUrl)
        // Initialize ExoPlayer
        exoplayer = SimpleExoPlayer.Builder(requireContext())
            .build()
        binding.playerView.player = exoplayer

        val concatenatedSource = ConcatenatingMediaSource()
        val mediaSource = ProgressiveMediaSource.Factory(DefaultHttpDataSourceFactory())
            .createMediaSource(Uri.parse(videoUrl))
        concatenatedSource.addMediaSource(mediaSource)
        //------
        videoViewModel.videoList.observe(viewLifecycleOwner, Observer { it ->
            it.forEach {
                if (it.uri != videoUrl) {
                    val firstSource: MediaSource =ProgressiveMediaSource.Factory(DefaultHttpDataSourceFactory())
                        .createMediaSource(Uri.parse(videoUrl))
                    concatenatedSource.addMediaSource(firstSource)
                }
            }
        })
        exoplayer.setMediaSource(concatenatedSource)
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