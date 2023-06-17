package com.app.dazn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.dazn.R
import com.app.dazn.databinding.FragmentHomeBinding
import com.app.dazn.view.adaptor.VideosAdapter
import com.app.dazn.viewmodel.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adaptor: VideosAdapter

    private val videoViewModel by viewModels<VideoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //----
        adaptor = VideosAdapter(
            emptyList(),
            requireContext(),
            ::onVideoItemClick
        )
        binding.videosRecycle.adapter = adaptor

        videoViewModel.loadVideoList(requireContext())
        //------
        videoViewModel.videoList.observe(viewLifecycleOwner, Observer {
            adaptor.updateList(it)
        })
    }

    private fun onVideoItemClick() {
        findNavController().navigate(R.id.action_homeFragment_to_playerFragment)
    }
}