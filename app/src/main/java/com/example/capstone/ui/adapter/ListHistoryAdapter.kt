package com.example.capstone.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.data.local.entity.PredictAndVideos
import com.example.capstone.databinding.HistoryRowItemBinding
import com.example.capstone.ui.history.HistoryDetailActivity
import java.text.NumberFormat

class ListHistoryAdapter(private val predictAndVideos: List<PredictAndVideos>): RecyclerView.Adapter<ListHistoryAdapter.HistoryViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder {
        val binding = HistoryRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HistoryViewHolder,
        position: Int
    ) {
        holder.bind(predictAndVideos[position])
    }

    override fun getItemCount(): Int = predictAndVideos.size

    class HistoryViewHolder(private val binding: HistoryRowItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PredictAndVideos) {
            binding.apply {
                predictResult.text = NumberFormat.getPercentInstance().format(data.predict.tingkatPrediksi).trim()
                videoDescription.text = data.predict.keterangan
                videoTitle.text = data.predict.namaPenyakit
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, HistoryDetailActivity::class.java)
                intent.putExtra(HistoryDetailActivity.EXTRA_PREDICTION, data.predict)
                intent.putParcelableArrayListExtra(HistoryDetailActivity.EXTRA_LIST_VIDEO, ArrayList(data.videos))
                Log.d(TAG, "StartIntent")
                itemView.context.startActivity(intent)
            }
        }
    }
    companion object {
        private const val TAG = "ListHistoryAdapter"
    }

}