package com.example.capstone.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.capstone.data.local.entity.HistoryEntity
import com.example.capstone.data.local.entity.ListHistoryEntity
import com.example.capstone.data.local.entity.PredictAndVideos
import com.example.capstone.data.local.entity.PredictVideosCrossRef

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(historyEntity: HistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListVideo(listHistoryEntity: List<ListHistoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRef(predictVideosCrossRef: PredictVideosCrossRef)

    @Update
    suspend fun updateHistory(historyEntity: HistoryEntity)

    @Query("SELECT MAX(videosId) FROM list_video_table")
    suspend fun getLastGroup(): Int?

    @Query("SELECT * FROM history_table")
    fun getAllHistory(): LiveData<List<HistoryEntity>>

    @Transaction
    @Query("SELECT * FROM history_table")
    fun getAllPredictAndVideos(): LiveData<List<PredictAndVideos>>


    @Query("DELETE FROM history_table")
    suspend fun deletePredict()

    @Query("DELETE FROM list_video_table")
    suspend fun deleteVideos()

    @Query("DELETE FROM predictvideoscrossref")
    suspend fun deleteCrossRef()

    @Transaction
    suspend fun deleteAll() {
        deletePredict()
        deleteVideos()
        deleteCrossRef()
    }
}