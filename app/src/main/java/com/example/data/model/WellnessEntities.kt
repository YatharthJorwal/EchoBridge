package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Explicit data availability state model for wearable and health telemetry.
 */
enum class SensorDataState(val label: String, val description: String) {
    UNAVAILABLE(
        label = "Unavailable",
        description = "No compatible health sensor or wearable connected."
    ),
    CONNECTED(
        label = "Connected",
        description = "Hardware paired. Biometric telemetry stream is not yet available."
    ),
    MONITORING(
        label = "Monitoring",
        description = "Genuine sensor data is actively being received."
    ),
    PERMISSION_REQUIRED(
        label = "Permission Required",
        description = "Required Android health or sensor permission has not been granted."
    )
}

@Entity(tableName = "wellness_logs")
data class WellnessLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayLabel: String,
    val echoScore: Int,
    val perspectiveBalance: Int,
    val openMindedness: Int,
    val contentDiversity: Int,
    val screenTimeMinutes: Int,
    val socialMins: Int,
    val productivityMins: Int,
    val researchMins: Int,
    val entertainmentMins: Int,
    val echoExposurePct: Int,
    val perspectivesCount: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "productivity_sessions")
data class ProductivitySession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val durationMinutes: Int,
    val omsEarned: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "stress_checkins")
data class StressCheckIn(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val stressLevel: Int, // 1 to 5
    val stressLabel: String,
    val note: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class PerspectiveTopic(
    val id: String,
    val title: String,
    val category: String,
    val biasAlert: String,
    val viewA: String,
    val viewB: String,
    val balancedInsight: String,
    val omsBonus: Int,
    var isExplored: Boolean = false
)
