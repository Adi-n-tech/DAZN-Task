package com.app.dazn.viewmodel

import android.content.Context
import android.util.Log
import com.app.dazn.data.Analytics
import com.app.dazn.data.VideoItem
import com.app.dazn.utils.readJsonFile
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class VideoRepository @Inject constructor() {

    fun getVideoList(context: Context): List<VideoItem> {
        return readJsonFile(context, "video_list.json")
    }

    fun logAnalytics(pauseCount: String, forwardCount: String, backwardCount: String) {
        val pauseCountRef = FirebaseFirestore.getInstance().collection("analytics").document("logs")
        val dataMap = mutableMapOf<String, Any>().apply {
            put("pause_count", pauseCount)
            put("forward_count", forwardCount)
            put("backward_count", backwardCount)
        }
        pauseCountRef.update(dataMap).addOnCompleteListener {}
    }

    fun getAnalytics(callback: (Analytics?) -> Unit) {
        val pauseCountRef = FirebaseFirestore.getInstance().collection("analytics").document("logs")
        pauseCountRef.addSnapshotListener { documentSnapshot, exception ->
            if (exception != null) {
                Log.e("Firestore", "Failed to fetch pause_count: ${exception.message}")
                callback(null)
                return@addSnapshotListener
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val pauseCounts = documentSnapshot.getString("pause_count")
                val forwardCounts = documentSnapshot.getString("forward_count")
                val backwardCounts = documentSnapshot.getString("backward_count")
                val analytics = Analytics(
                    pauseCounts.toString(),
                    forwardCounts.toString(),
                    backwardCounts.toString()
                )
                Log.d("Aditya", pauseCounts + forwardCounts + backwardCounts)
                callback(analytics)
            } else {
                Log.d("Firestore", "pause_count document does not exist")
                callback(null)
            }
        }
    }
}