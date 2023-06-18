package com.app.dazn.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dazn.data.Analytics
import com.app.dazn.data.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(private val videoRepository: VideoRepository) :
    ViewModel() {
    private val _videoList = MutableLiveData<List<VideoItem>>()
    val videoList: LiveData<List<VideoItem>> = _videoList

    private val _analytics = MutableLiveData<Analytics>()
    val analytics: LiveData<Analytics> = _analytics

    fun loadVideoList(context: Context) {
        viewModelScope.launch {
            val videos = videoRepository.getVideoList(context)
            _videoList.value = videos
        }
    }

    fun getAnalytics() {
        viewModelScope.launch {
            videoRepository.getAnalytics {
                _analytics.value = it
            }
        }
    }

    fun logAnalytics() {
        viewModelScope.launch {
            videoRepository.logAnalytics(
                analytics.value!!.pause_count,
                analytics.value!!.forward_count,
                analytics.value!!.backward_count
            )
        }
    }
}