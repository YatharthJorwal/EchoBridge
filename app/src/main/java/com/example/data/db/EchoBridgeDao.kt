package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ProductivitySession
import com.example.data.model.StressCheckIn
import com.example.data.model.WellnessLog
import kotlinx.coroutines.flow.Flow

@Dao
interface EchoBridgeDao {

    // Wellness Daily Logs
    @Query("SELECT * FROM wellness_logs ORDER BY timestamp ASC")
    fun getAllWellnessLogs(): Flow<List<WellnessLog>>

    @Query("SELECT * FROM wellness_logs ORDER BY timestamp DESC LIMIT 1")
    fun getLatestWellnessLog(): Flow<WellnessLog?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWellnessLog(log: WellnessLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWellnessLogs(logs: List<WellnessLog>)

    @Update
    suspend fun updateWellnessLog(log: WellnessLog)

    // Productivity Sessions
    @Query("SELECT * FROM productivity_sessions ORDER BY timestamp DESC")
    fun getAllProductivitySessions(): Flow<List<ProductivitySession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductivitySession(session: ProductivitySession): Long

    // Stress Check-Ins
    @Query("SELECT * FROM stress_checkins ORDER BY timestamp DESC")
    fun getAllStressCheckIns(): Flow<List<StressCheckIn>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStressCheckIn(checkIn: StressCheckIn): Long

    @Query("SELECT COUNT(*) FROM wellness_logs")
    suspend fun getWellnessLogCount(): Int
}
