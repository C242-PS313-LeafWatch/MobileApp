package com.example.capstone.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.capstone.data.local.entity.HistoryEntity
import com.example.capstone.data.local.entity.ListHistoryEntity
import com.example.capstone.data.local.entity.PredictVideosCrossRef

@Database(entities = [HistoryEntity::class, ListHistoryEntity::class, PredictVideosCrossRef::class], version = 1, exportSchema = false)
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        fun getDatabase(context: Context): HistoryDatabase {
            if (INSTANCE == null) {
                synchronized(HistoryDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, HistoryDatabase::class.java, "history.db").build()
                }
            }
            return INSTANCE as HistoryDatabase
        }
    }
}