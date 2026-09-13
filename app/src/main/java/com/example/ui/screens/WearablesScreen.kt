package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SensorDataState
import com.example.ui.theme.EchoAmber
import com.example.ui.theme.EchoBgElevated
import com.example.ui.theme.EchoBgSurface
import com.example.ui.theme.EchoBlue
import com.example.ui.theme.EchoBlueBright
import com.example.ui.theme.EchoCardBorder
import com.example.ui.theme.EchoCyan
import com.example.ui.theme.EchoEmerald
import com.example.ui.theme.EchoPurple
import com.example.ui.theme.EchoTextMuted
import com.example.ui.theme.EchoTextPrimary
import com.example.ui.theme.EchoTextSecondary

@Composable
fun WearablesScreen(
    sensorDataState: SensorDataState,
    isScanning: Boolean,
    isRingPaired: Boolean,
    batteryPct: Int?,
    liveBpm: Int?,
    liveHrv: Int?,
    recoveryPct: Int?,
    onStartScan: () -> Unit,
    onTogglePairRing: () -> Unit,
    onOpenPreorder: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanPulse")
    val scanRipple by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "HARDWARE ECOSYSTEM",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EchoPurple,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "EchoRing & Smart Biometrics",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = EchoTextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(EchoAmber.copy(alpha = 0.15f))
                        .border(1.dp, EchoAmber.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Hardware Coming Soon",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EchoAmber
                    )
                }
            }
        }

        // Hero EchoRing Graphic & Status Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("echoring_showcase_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(EchoPurple.copy(alpha = 0.5f), EchoBlue.copy(alpha = 0.5f))
                    )
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Stylized 3D Ring Graphic in Canvas
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(140.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val ringRadius = 46.dp.toPx()
                            val bandWidth = 14.dp.toPx()

                            // Outer ambient ring glow
                            drawCircle(
                                brush = Brush.radialGradient(
                                    listOf(EchoBlue.copy(alpha = 0.3f), EchoPurple.copy(alpha = 0.15f), Color.Transparent),
                                    center = center,
                                    radius = 65.dp.toPx()
                                ),
                                center = center
                            )

                            // Ring Titanium Outer Band
                            drawCircle(
                                brush = Brush.sweepGradient(
                                    listOf(
                                        Color(0xFF2C324E),
                                        Color(0xFF5A6694),
                                        Color(0xFF141724),
                                        Color(0xFF6B7AA8),
                                        Color(0xFF2C324E)
                                    ),
                                    center = center
                                ),
                                radius = ringRadius,
                                style = Stroke(width = bandWidth)
                            )

                            // Inner Optical Sensor Glow Ring
                            drawCircle(
                                brush = Brush.sweepGradient(
                                    listOf(EchoCyan, EchoBlueBright, EchoPurple, EchoCyan),
                                    center = center
                                ),
                                radius = ringRadius - (bandWidth * 0.4f),
                                style = Stroke(width = 3.dp.toPx())
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "EchoRing™",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EchoTextPrimary
                            )
                            Text(
                                text = "Titanium v1.0",
                                fontSize = 10.sp,
                                color = EchoBlueBright
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Continuous Biometric Monitoring",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = EchoTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "The physical extension of EchoBridge. Monitors HRV, real-time stress spikes, sleep recovery, and body response to digital media.",
                        fontSize = 12.sp,
                        color = EchoTextSecondary,
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hardware Specs Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SpecChip(label = "Battery", value = "7 Days")
                        SpecChip(label = "Material", value = "Grade 5 Titanium")
                        SpecChip(label = "Waterproof", value = "IP68 / 50m")
                        SpecChip(label = "Sync", value = "Live BLE")
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Pre-Order / Reserve CTA
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onOpenPreorder,
                            colors = ButtonDefaults.buttonColors(containerColor = EchoPurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(44.dp).testTag("preorder_button")
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Pre-Order (₹7,499) →", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onTogglePairRing,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isRingPaired) EchoEmerald.copy(alpha = 0.2f) else EchoBgElevated
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(44.dp).testTag("simulate_pair_button")
                        ) {
                            Icon(
                                imageVector = if (isRingPaired) Icons.Default.BluetoothConnected else Icons.Default.Bluetooth,
                                contentDescription = null,
                                tint = if (isRingPaired) EchoEmerald else EchoBlueBright,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isRingPaired) "Connected" else "Simulate BLE",
                                fontSize = 12.sp,
                                color = if (isRingPaired) EchoEmerald else EchoTextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Live Synced Telemetry Card (When Paired)
        if (isRingPaired) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = EchoBgElevated),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(EchoCyan.copy(alpha = 0.6f), EchoPurple.copy(alpha = 0.6f))))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EchoCyan, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "EchoRing Prototype Connected", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                            }
                            if (batteryPct != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.BatteryChargingFull, contentDescription = null, tint = EchoCyan, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "$batteryPct%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EchoCyan)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                val bpmStr = if (sensorDataState == SensorDataState.MONITORING && liveBpm != null) "$liveBpm bpm" else "-- bpm"
                                Text(text = bpmStr, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = EchoTextPrimary)
                                Text(text = "Optical PPG Heart Rate", fontSize = 10.sp, color = EchoTextSecondary)
                            }
                            Column {
                                val hrvStr = if (sensorDataState == SensorDataState.MONITORING && liveHrv != null) "$liveHrv ms" else "-- ms"
                                Text(text = hrvStr, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = EchoCyan)
                                Text(text = "Autonomic HRV", fontSize = 10.sp, color = EchoTextSecondary)
                            }
                            Column {
                                val recStr = if (sensorDataState == SensorDataState.MONITORING && recoveryPct != null) "$recoveryPct%" else "Ready"
                                Text(text = recStr, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = EchoEmerald)
                                Text(text = "Telemetry Pipeline", fontSize = 10.sp, color = EchoTextSecondary)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "BLE link established. Optical PPG telemetry protocol is calibrated on hardware revision v2.0.",
                            fontSize = 11.sp,
                            color = EchoTextMuted,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Bluetooth BLE Scanner Module
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ble_scanner_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(EchoCardBorder, EchoCardBorder.copy(alpha = 0.5f))))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bluetooth, contentDescription = null, tint = EchoBlueBright, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Nearby BLE Devices", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                        }

                        Button(
                            onClick = onStartScan,
                            colors = ButtonDefaults.buttonColors(containerColor = EchoBlue.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("scan_ble_button")
                        ) {
                            if (isScanning) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .scale(scanRipple)
                                        .clip(CircleShape)
                                        .background(EchoBlueBright)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Scanning...", fontSize = 11.sp, color = EchoBlueBright, fontWeight = FontWeight.Bold)
                            } else {
                                Text(text = "Scan BLE", fontSize = 11.sp, color = EchoBlueBright, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Discovered Device 1: EchoRing
                    DeviceDiscoveryRow(
                        title = "EchoRing_Titanium_BETA",
                        subtitle = "Signal: -48 dBm · Hardware Prototype",
                        badge = if (isRingPaired) "Paired" else "Discovered",
                        badgeColor = if (isRingPaired) EchoEmerald else EchoBlueBright,
                        onClick = onTogglePairRing
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Discovered Device 2: EchoEar
                    DeviceDiscoveryRow(
                        title = "EchoEar_AI_DEV",
                        subtitle = "Signal: -65 dBm · Roadmap Q4 Concept",
                        badge = "Concept Stage",
                        badgeColor = EchoAmber,
                        onClick = {}
                    )
                }
            }
        }

        // EchoEar Companion Concept Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("echoear_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(EchoCardBorder, EchoCardBorder.copy(alpha = 0.5f))))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(EchoPurple.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Headphones, contentDescription = null, tint = EchoPurple, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "EchoEar™ Vocal AI Earpiece", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(EchoAmber.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(text = "Roadmap", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EchoAmber)
                            }
                        }
                        Text(
                            text = "Monitors vocal biomarkers and tone escalation during calls to prevent conflict & burnout.",
                            fontSize = 11.sp,
                            color = EchoTextSecondary,
                            lineHeight = 15.sp
                        )
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
fun SpecChip(label: String, value: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(EchoBgElevated)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
        Text(text = label, fontSize = 9.sp, color = EchoTextMuted)
    }
}

@Composable
fun DeviceDiscoveryRow(
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(EchoBgElevated)
            .border(1.dp, EchoCardBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
            Text(text = subtitle, fontSize = 10.sp, color = EchoTextMuted)
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(badgeColor.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text = badge, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = badgeColor)
        }
    }
}
