package com.example.capstone.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FileUploadResponse(

	@field:SerializedName("prediction")
	val prediction: Prediction,

	@field:SerializedName("videos")
	val videos: List<VideosItem>,

	@field:SerializedName("status")
	val status: String
)

@Parcelize
data class Prediction(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("nama_penyakit")
	val namaPenyakit: String,

	@field:SerializedName("tingkat_prediksi")
	val tingkatPrediksi: Double? = null
): Parcelable

@Parcelize
data class VideosItem(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("video_url")
	val videoUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String
): Parcelable
