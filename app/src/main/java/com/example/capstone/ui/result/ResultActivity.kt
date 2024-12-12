package com.example.capstone.ui.result

import android.net.Uri
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
import com.example.capstone.data.remote.response.Prediction
import com.example.capstone.data.remote.response.VideosItem
import com.example.capstone.databinding.ActivityResultBinding
import com.example.capstone.ui.adapter.LinkVideoAdapter
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
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
        val dataImage = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_IMAGE_URI, Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_IMAGE_URI)
        }

        val dataPrediction = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_PREDICTION, Prediction::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_PREDICTION)
        }

        val dataVideos = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableArrayListExtra(EXTRA_LIST_VIDEO, VideosItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra(EXTRA_LIST_VIDEO)
        }

        if (dataImage != null) {
            if (dataImage == String) {
                null
            } else {
                binding.imgResult.setImageURI(dataImage)
            }

        }
        if (dataPrediction != null) {
            binding.apply {
                nameResult.text = dataPrediction.namaPenyakit
                keteranganResult.text = dataPrediction.keterangan
                predictResult.text = NumberFormat.getPercentInstance().format(dataPrediction.tingkatPrediksi).trim()
            }
        }

        if (dataVideos != null) {
            binding.videoSectionLabel.visibility = View.VISIBLE
            setupVideos(dataVideos)
        } else {
            binding.videoSectionLabel.visibility = View.GONE
        }
    }


    private fun setupVideos(videoList: List<VideosItem?>?) {
        if (videoList.isNullOrEmpty()) return

        val adapter = LinkVideoAdapter(videoList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ResultActivity)
            this.adapter = adapter
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_PREDICTION = "extra_prediction"
        const val EXTRA_LIST_VIDEO = "extra_list_video"
    }
}