package com.example.capstone.ui.history

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.R
import com.example.capstone.data.local.entity.HistoryEntity
import com.example.capstone.data.local.entity.ListHistoryEntity
import com.example.capstone.databinding.ActivityHistoryDetailBinding
import com.example.capstone.ui.adapter.HistoryVideoAdapter
import java.text.NumberFormat

class HistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.result)
        }
        setupAction()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupAction() {
        val historyPrediction = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_PREDICTION, HistoryEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_PREDICTION)
        }

        val historyVideos = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableArrayListExtra(EXTRA_LIST_VIDEO, ListHistoryEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra(EXTRA_LIST_VIDEO)
        }

        if (historyPrediction != null) {
            binding.apply {
                nameResult.text = historyPrediction.namaPenyakit
                keteranganResult.text = historyPrediction.keterangan
                predictResult.text = NumberFormat.getPercentInstance().format(historyPrediction.tingkatPrediksi).trim()

            }
        }

        if (historyVideos != null) {
            binding.videoSectionLabel.visibility = View.VISIBLE
            setupHistoryVideos(historyVideos)
        } else {
            binding.videoSectionLabel.visibility = View.GONE
        }
    }

    private fun setupHistoryVideos(videoList: List<ListHistoryEntity>) {
        val adapter = HistoryVideoAdapter(videoList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HistoryDetailActivity)
            this.adapter = adapter
        }
    }

    companion object {
        const val EXTRA_PREDICTION = "extra_prediction"
        const val EXTRA_LIST_VIDEO = "extra_list_video"
    }
}