package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ProductivitySession
import com.example.data.model.StressCheckIn
import com.example.data.model.WellnessLog

@Database(
    entities = [WellnessLog::class, ProductivitySession::class, StressCheckIn::class],
    version = 2,
    exportSchema = false
)
abstract class EchoBridgeDatabase : RoomDatabase() {

    abstract fun echoBridgeDao(): EchoBridgeDao

    companion object {
        @Volatile
        private var INSTANCE: EchoBridgeDatabase? = null

        fun getDatabase(context: Context): EchoBridgeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EchoBridgeDatabase::class.java,
                    "echobridge_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
