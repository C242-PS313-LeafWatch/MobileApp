package com.example.capstone.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Entity(tableName = "history_table")
@Parcelize
data class HistoryEntity(

    @PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    val id: Int = 0,

    @field:ColumnInfo(name = "imageUri")
    val imageUri: String? = null,

    @field:ColumnInfo(name = "keterangan")
    val keterangan: String? = null,

    @field:ColumnInfo(name = "namaPenyakit")
    val namaPenyakit: String? = null,

    @field:ColumnInfo(name = "tingkatPrediksi")
    val tingkatPrediksi: Double? = null,

    @field:ColumnInfo(name = "predictVideoId")
    val predictVideoId: Int? = null

): Parcelable

@Entity(tableName = "list_video_table")
@Parcelize
data class ListHistoryEntity(

    @PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "uniqueId")
    val uniqueId: Int = 0,

    @field:ColumnInfo(name = "videosId")
    val videosId: Int,

    @field:ColumnInfo(name = "thumbnail")
    val thumbnail: String? = null,

    @field:ColumnInfo(name = "videoUrl")
    val videoUrl: String? = null,

    @field:ColumnInfo(name = "description")
    val description: String? = null,

    @field:ColumnInfo(name = "title")
    val title: String? = null
): Parcelable

@Entity(primaryKeys = ["pId","vId"])
data class PredictVideosCrossRef(
    val pId: Int,
    @ColumnInfo(index = true)
    val vId: Int,
)

data class PredictAndVideos(
    @Embedded
    val predict: HistoryEntity,

    @Relation(
        parentColumn = "predictVideoId",
        entity = ListHistoryEntity::class,
        entityColumn = "videosId",
        associateBy = Junction(
            value = PredictVideosCrossRef::class,
            parentColumn = "pId",
            entityColumn = "vId"
        )
    )
    val videos: List<ListHistoryEntity>
)