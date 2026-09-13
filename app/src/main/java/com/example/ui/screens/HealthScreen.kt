package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SensorsOff
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SensorDataState
import com.example.data.model.StressCheckIn
import com.example.data.model.WellnessLog
import com.example.ui.theme.EchoAmber
import com.example.ui.theme.EchoBgElevated
import com.example.ui.theme.EchoBgSurface
import com.example.ui.theme.EchoBlue
import com.example.ui.theme.EchoBlueBright
import com.example.ui.theme.EchoCardBorder
import com.example.ui.theme.EchoCoral
import com.example.ui.theme.EchoCyan
import com.example.ui.theme.EchoEmerald
import com.example.ui.theme.EchoPurple
import com.example.ui.theme.EchoTextMuted
import com.example.ui.theme.EchoTextPrimary
import com.example.ui.theme.EchoTextSecondary
import com.example.ui.viewmodel.BreathPhase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HealthScreen(
    sensorDataState: SensorDataState,
    todayLog: WellnessLog?,
    stressCheckIns: List<StressCheckIn>,
    liveBpm: Int?,
    liveHrv: Int?,
    isBreatherRunning: Boolean,
    breathPhase: BreathPhase,
    breathSecondsLeft: Int,
    breathCyclesCompleted: Int,
    onToggleBreather: () -> Unit,
    onOpenCheckInDialog: () -> Unit,
    onNavigateWearables: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "BIOMETRIC HEALTH & BIOFEEDBACK",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EchoBlueBright,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Sensor Pipeline & Mindfulness",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = EchoTextPrimary
            )
        }

        // Explicit Sensor Availability State Card
        item {
            SensorAvailabilityCard(
                state = sensorDataState,
                onNavigateWearables = onNavigateWearables
            )
        }

        // Hardware-Ready Cardiovascular & HRV Telemetry Card
        item {
            HardwareReadyVitalsCard(
                state = sensorDataState,
                liveBpm = liveBpm,
                liveHrv = liveHrv,
                onNavigateWearables = onNavigateWearables
            )
        }

        // Guided 4-7-8 Diaphragmatic Breather Card
        item {
            GuidedBreatherCard(
                isRunning = isBreatherRunning,
                phase = breathPhase,
                secondsLeft = breathSecondsLeft,
                cycles = breathCyclesCompleted,
                onToggleBreather = onToggleBreather
            )
        }

        // Subjective Stress Check-in Logger & Journal
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stress_checkin_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(EchoCardBorder, EchoCardBorder.copy(alpha = 0.5f)))
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Subjective Stress Journal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = EchoTextPrimary
                            )
                            Text(
                                text = "Self-reported cognitive load & triggers",
                                fontSize = 11.sp,
                                color = EchoTextSecondary
                            )
                        }

                        Button(
                            onClick = onOpenCheckInDialog,
                            colors = ButtonDefaults.buttonColors(containerColor = EchoPurple),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("open_checkin_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Log Stress", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (stressCheckIns.isEmpty()) {
                        Text(
                            text = "No stress self-assessments recorded today. Tap 'Log Stress' to track subjective mental load.",
                            fontSize = 12.sp,
                            color = EchoTextSecondary,
                            lineHeight = 16.sp
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            stressCheckIns.take(4).forEach { item ->
                                StressLogItem(item = item)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SensorAvailabilityCard(
    state: SensorDataState,
    onNavigateWearables: () -> Unit
) {
    val (statusColor, icon, stateTitle) = when (state) {
        SensorDataState.UNAVAILABLE -> Triple(EchoAmber, Icons.Default.SensorsOff, "Sensor Unavailable")
        SensorDataState.CONNECTED -> Triple(EchoCyan, Icons.Default.BluetoothConnected, "Hardware Paired")
        SensorDataState.MONITORING -> Triple(EchoEmerald, Icons.Default.MonitorHeart, "Monitoring Active")
        SensorDataState.PERMISSION_REQUIRED -> Triple(EchoCoral, Icons.Default.WarningAmber, "Permission Required")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("sensor_availability_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(statusColor.copy(alpha = 0.5f), EchoCardBorder))
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = icon, contentDescription = null, tint = statusColor, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Biometric Sensor Status", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = state.label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = statusColor)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = state.description,
                fontSize = 12.sp,
                color = EchoTextSecondary,
                lineHeight = 16.sp
            )

            if (state == SensorDataState.UNAVAILABLE) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onNavigateWearables,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(38.dp)
                ) {
                    Icon(Icons.Default.Watch, contentDescription = null, tint = EchoCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Connect Compatible Wearable →", fontSize = 11.sp, color = EchoCyan, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun HardwareReadyVitalsCard(
    state: SensorDataState,
    liveBpm: Int?,
    liveHrv: Int?,
    onNavigateWearables: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("vitals_waveform_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(EchoCyan.copy(alpha = 0.3f), EchoPurple.copy(alpha = 0.3f)))
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MonitorHeart, contentDescription = null, tint = EchoCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Biometric Telemetry (PPG & HRV)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(EchoPurple.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "Hardware-Ready", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EchoPurple)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Honest Metric Row with explicit state handling
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    val bpmText = if (state == SensorDataState.MONITORING && liveBpm != null) "$liveBpm bpm" else "-- bpm"
                    Text(text = bpmText, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = EchoTextPrimary)
                    Text(
                        text = if (state == SensorDataState.MONITORING) "Continuous Optical PPG" else "No data available",
                        fontSize = 11.sp,
                        color = EchoTextSecondary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    val hrvText = if (state == SensorDataState.MONITORING && liveHrv != null) "$liveHrv ms" else "-- ms"
                    Text(text = hrvText, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = EchoCyan)
                    Text(
                        text = if (state == SensorDataState.MONITORING) "Autonomic HRV (rMSSD)" else "No data available",
                        fontSize = 11.sp,
                        color = EchoTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Hardware interface explainer box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(EchoBgElevated)
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = EchoCyan,
                        modifier = Modifier.size(18.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Hardware Integration Required",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EchoTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Standard Android smartphones lack optical PPG sensors. Real-time cardiovascular telemetry will activate once a compatible EchoRing or approved BLE health monitor is paired and transmitting.",
                            fontSize = 11.sp,
                            color = EchoTextSecondary,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GuidedBreatherCard(
    isRunning: Boolean,
    phase: BreathPhase,
    secondsLeft: Int,
    cycles: Int,
    onToggleBreather: () -> Unit
) {
    val targetScale = when (phase) {
        BreathPhase.INHALE -> 1.4f
        BreathPhase.HOLD -> 1.4f
        BreathPhase.EXHALE -> 0.9f
    }
    val animatedScale by animateFloatAsState(
        targetValue = if (isRunning) targetScale else 1.0f,
        animationSpec = tween(
            durationMillis = if (phase == BreathPhase.HOLD) 500 else (phase.seconds * 1000),
            easing = FastOutSlowInEasing
        ),
        label = "breathScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("guided_breather_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(EchoCyan.copy(alpha = 0.4f), EchoBlue.copy(alpha = 0.4f))))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = EchoCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "4-7-8 Diaphragmatic Breather", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(EchoCyan.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "Cycles: $cycles/3", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EchoCyan)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Pulsing Breathing Orb
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(150.dp)
            ) {
                // Aura Circle
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(animatedScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(EchoCyan.copy(alpha = 0.35f), EchoBlue.copy(alpha = 0.1f), Color.Transparent)
                            )
                        )
                )

                // Inner Core
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(animatedScale * 0.85f)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(EchoCyan, EchoBlue))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isRunning) "${secondsLeft}s" else "Breathe",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (isRunning) phase.label else "Align autonomic pacing & calm the nervous system",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isRunning) EchoCyan else EchoTextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onToggleBreather,
                colors = ButtonDefaults.buttonColors(containerColor = if (isRunning) EchoCoral else EchoCyan),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(44.dp).testTag("breather_toggle_button")
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isRunning) "Stop Breathing Session" else "Start 4-7-8 Calm Cycle",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun StressLogItem(item: StressCheckIn) {
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val timeStr = formatter.format(Date(item.timestamp))

    val color = when (item.stressLevel) {
        1 -> EchoEmerald
        2 -> EchoCyan
        3 -> EchoAmber
        4 -> EchoCoral
        else -> EchoPurple
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(EchoBgElevated)
            .border(1.dp, EchoCardBorder, RoundedCornerShape(10.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = item.stressLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                Text(text = item.note.ifBlank { "Subjective self-assessment" }, fontSize = 11.sp, color = EchoTextMuted)
            }
        }
        Text(text = timeStr, fontSize = 10.sp, color = EchoTextMuted)
    }
}
