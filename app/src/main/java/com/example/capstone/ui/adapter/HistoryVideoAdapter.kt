package com.example.capstone.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.data.local.entity.ListHistoryEntity
import com.example.capstone.databinding.NutritionRowItemBinding

class HistoryVideoAdapter(private val videos: List<ListHistoryEntity?>) : RecyclerView.Adapter<HistoryVideoAdapter.VideoViewHolder>() {
    class VideoViewHolder(private val binding: NutritionRowItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(videosItem: ListHistoryEntity) {
            Glide.with(itemView.context)
                .load(videosItem.thumbnail)
                .into(binding.imgPoster)
            binding.tvItemTitle.text = videosItem.title
            binding.tvItemDescription.text = videosItem.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = NutritionRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder((binding))
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        if (video != null) {
            holder.bind(video)
            holder.itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.videoUrl))
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = videos.size
}
