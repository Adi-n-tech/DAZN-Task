package com.app.dazn.view.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.dazn.R
import com.app.dazn.data.VideoItem
import com.app.dazn.databinding.ItemVideoBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class VideosAdapter(
    private var list: List<VideoItem>,
    private val context: Context,
    private val onVideoItemClick: (videoUrl: String) -> Unit
) :
    RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemGridBinding =
            ItemVideoBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemGridBinding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoUrl = list[position].uri
        //-----
        val requestOptions = RequestOptions()
        Glide.with(context)
            .load(videoUrl)
            .apply(requestOptions)
            .placeholder(R.drawable.video_thumbnail)
            .thumbnail(Glide.with(context).load(videoUrl))
            .into(holder.binding.thumbnail)
        //-----
        holder.binding.thumbnail.setOnClickListener {
            onVideoItemClick(videoUrl)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

    fun updateList(newList: List<VideoItem>) {
        list = newList
        notifyDataSetChanged()
    }
}