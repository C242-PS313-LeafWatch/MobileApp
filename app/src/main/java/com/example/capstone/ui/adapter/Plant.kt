package com.example.capstone.ui.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Plant(
    val name: String,
    val description: String,
    val photo: Int
): Parcelable
