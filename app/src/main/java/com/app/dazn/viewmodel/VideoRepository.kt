package com.app.dazn.viewmodel

import android.content.Context
import com.app.dazn.data.VideoItem
import com.app.dazn.utils.readJsonFile
import javax.inject.Inject

class VideoRepository @Inject constructor() {

    fun getVideoList(context: Context): List<VideoItem> {
        return readJsonFile(context, "video_list.json")
    }
}