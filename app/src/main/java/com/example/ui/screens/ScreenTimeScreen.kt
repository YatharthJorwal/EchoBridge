package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun ScreenTimeScreen(
    wellnessLogs: List<WellnessLog>,
    todayLog: WellnessLog?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var dailyTargetHours by remember { mutableFloatStateOf(4.0f) }
    var doomscrollAlertEnabled by remember { mutableStateOf(true) }
    var bedtimeWinddownEnabled by remember { mutableStateOf(true) }

    val log = todayLog ?: WellnessLog(
        dayLabel = "Today",
        echoScore = 82,
        perspectiveBalance = 76,
        openMindedness = 88,
        contentDiversity = 61,
        screenTimeMinutes = 268,
        socialMins = 112,
        productivityMins = 96,
        researchMins = 38,
        entertainmentMins = 22,
        echoExposurePct = 18,
        perspectivesCount = 21
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "DIGITAL WELLBEING & USAGE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EchoBlueBright,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Screen Time & Mindful Pacing",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = EchoTextPrimary
            )
        }

        // Digital Wellbeing Linking Banner
        item {
            DigitalWellbeingLinkBanner(context = context)
        }

        // Weekly Screen Time Bar Chart Card
        item {
            WeeklyScreenTimeChartCard(logs = wellnessLogs)
        }

        // Usage Category Breakdown
        item {
            CategoryBreakdownCard(log = log)
        }

        // Screen Time Target & Mindful Guardrails
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("screen_time_controls_card"),
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
                            Icon(
                                imageVector = Icons.Default.HourglassBottom,
                                contentDescription = null,
                                tint = EchoAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Daily Target Limit",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = EchoTextPrimary
                            )
                        }
                        Text(
                            text = "${dailyTargetHours.toInt()}h ${((dailyTargetHours % 1) * 60).toInt()}m",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EchoAmber
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Slider(
                        value = dailyTargetHours,
                        onValueChange = { dailyTargetHours = it },
                        valueRange = 1.5f..8.0f,
                        steps = 12,
                        colors = SliderDefaults.colors(
                            thumbColor = EchoAmber,
                            activeTrackColor = EchoAmber,
                            inactiveTrackColor = Color(0xFF1E2648)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Guardrail Toggles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = EchoCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Doomscrolling Alert", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = EchoTextPrimary)
                            }
                            Text(text = "Gentle nudge after 20m repetitive feed browsing", fontSize = 11.sp, color = EchoTextMuted)
                        }
                        Switch(
                            checked = doomscrollAlertEnabled,
                            onCheckedChange = { doomscrollAlertEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = EchoCyan, checkedTrackColor = EchoCyan.copy(alpha = 0.3f))
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Bedtime, contentDescription = null, tint = EchoPurple, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Bedtime Cognitive Shield", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = EchoTextPrimary)
                            }
                            Text(text = "Dampen polarized feeds after 10:00 PM for deep sleep", fontSize = 11.sp, color = EchoTextMuted)
                        }
                        Switch(
                            checked = bedtimeWinddownEnabled,
                            onCheckedChange = { bedtimeWinddownEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = EchoPurple, checkedTrackColor = EchoPurple.copy(alpha = 0.3f))
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
fun DigitalWellbeingLinkBanner(context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = EchoBgElevated),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(EchoEmerald.copy(alpha = 0.5f), EchoBlue.copy(alpha = 0.5f))))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(EchoEmerald.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = EchoEmerald,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Linked to Digital Wellbeing",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = EchoTextPrimary
                        )
                        Text(
                            text = "Android System Service Sync",
                            fontSize = 10.sp,
                            color = EchoEmerald
                        )
                    }
                }

                Button(
                    onClick = {
                        openDigitalWellbeingSettings(context)
                    },
                    modifier = Modifier.testTag("open_digital_wellbeing_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = EchoBlue.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Manage OS", fontSize = 11.sp, color = EchoBlueBright, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.Launch, contentDescription = null, tint = EchoBlueBright, modifier = Modifier.size(12.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "EchoBridge analyzes your informational health on-device, combining screen duration with echo chamber bias metrics for actionable balance.",
                fontSize = 11.sp,
                color = EchoTextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}

fun openDigitalWellbeingSettings(context: Context) {
    try {
        // Direct intent to Digital Wellbeing or Usage Access
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        try {
            val fallbackIntent = Intent(Settings.ACTION_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(fallbackIntent)
        } catch (_: Exception) {}
    }
}

@Composable
fun WeeklyScreenTimeChartCard(logs: List<WellnessLog>) {
    val displayLogs = if (logs.isEmpty()) {
        listOf(
            WellnessLog(dayLabel = "Mon", echoScore = 68, perspectiveBalance = 58, openMindedness = 72, contentDiversity = 48, screenTimeMinutes = 380, socialMins = 210, productivityMins = 80, researchMins = 45, entertainmentMins = 45, echoExposurePct = 34, perspectivesCount = 11),
            WellnessLog(dayLabel = "Tue", echoScore = 72, perspectiveBalance = 63, openMindedness = 77, contentDiversity = 52, screenTimeMinutes = 340, socialMins = 180, productivityMins = 95, researchMins = 35, entertainmentMins = 30, echoExposurePct = 29, perspectivesCount = 14),
            WellnessLog(dayLabel = "Wed", echoScore = 74, perspectiveBalance = 66, openMindedness = 81, contentDiversity = 55, screenTimeMinutes = 310, socialMins = 160, productivityMins = 90, researchMins = 30, entertainmentMins = 30, echoExposurePct = 25, perspectivesCount = 16),
            WellnessLog(dayLabel = "Thu", echoScore = 77, perspectiveBalance = 70, openMindedness = 83, contentDiversity = 57, screenTimeMinutes = 295, socialMins = 140, productivityMins = 100, researchMins = 30, entertainmentMins = 25, echoExposurePct = 22, perspectivesCount = 18),
            WellnessLog(dayLabel = "Fri", echoScore = 79, perspectiveBalance = 73, openMindedness = 85, contentDiversity = 59, screenTimeMinutes = 280, socialMins = 130, productivityMins = 105, researchMins = 25, entertainmentMins = 20, echoExposurePct = 20, perspectivesCount = 19),
            WellnessLog(dayLabel = "Sat", echoScore = 80, perspectiveBalance = 74, openMindedness = 86, contentDiversity = 60, screenTimeMinutes = 275, socialMins = 120, productivityMins = 110, researchMins = 25, entertainmentMins = 20, echoExposurePct = 19, perspectivesCount = 20),
            WellnessLog(dayLabel = "Today", echoScore = 82, perspectiveBalance = 76, openMindedness = 88, contentDiversity = 61, screenTimeMinutes = 268, socialMins = 112, productivityMins = 96, researchMins = 38, entertainmentMins = 22, echoExposurePct = 18, perspectivesCount = 21)
        )
    } else logs.takeLast(7)

    val maxMinutes = 420f // 7 hours max baseline

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("weekly_chart_card"),
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
                Column {
                    Text(text = "7-Day Usage Trend", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                    Text(text = "Daily Screen Time Distribution", fontSize = 11.sp, color = EchoTextSecondary)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(EchoEmerald.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "-32m vs last week", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EchoEmerald)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Bar Chart Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val count = displayLogs.size
                    val spacing = size.width / count
                    val barWidth = 24.dp.toPx()

                    displayLogs.forEachIndexed { index, item ->
                        val barHeight = (item.screenTimeMinutes / maxMinutes) * (size.height - 24.dp.toPx())
                        val x = index * spacing + (spacing - barWidth) / 2
                        val y = size.height - 20.dp.toPx() - barHeight

                        val isToday = index == count - 1
                        val brush = if (isToday) {
                            Brush.verticalGradient(listOf(EchoBlueBright, EchoPurple))
                        } else {
                            Brush.verticalGradient(listOf(Color(0xFF2C3A6E), Color(0xFF161F42)))
                        }

                        drawRoundRect(
                            brush = brush,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                        )
                    }
                }
            }

            // Labels under bars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                displayLogs.forEachIndexed { index, item ->
                    val isToday = index == displayLogs.size - 1
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = item.dayLabel,
                            fontSize = 10.sp,
                            fontWeight = if (isToday) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (isToday) EchoBlueBright else EchoTextMuted
                        )
                        Text(
                            text = "${item.screenTimeMinutes / 60}h",
                            fontSize = 9.sp,
                            color = if (isToday) EchoTextPrimary else EchoTextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryBreakdownCard(log: WellnessLog) {
    val total = (log.socialMins + log.productivityMins + log.researchMins + log.entertainmentMins).coerceAtLeast(1)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("category_breakdown_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(EchoCardBorder, EchoCardBorder.copy(alpha = 0.5f))))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = "Content & App Categories", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
            Text(text = "Distribution across cognitive activity zones", fontSize = 11.sp, color = EchoTextSecondary)

            Spacer(modifier = Modifier.height(14.dp))

            CategoryRow(
                name = "Social & Feed Loops (Echo Risk)",
                minutes = log.socialMins,
                pct = (log.socialMins * 100) / total,
                color = EchoCoral
            )
            Spacer(modifier = Modifier.height(10.dp))
            CategoryRow(
                name = "Focus & Learning Modules",
                minutes = log.productivityMins,
                pct = (log.productivityMins * 100) / total,
                color = EchoEmerald
            )
            Spacer(modifier = Modifier.height(10.dp))
            CategoryRow(
                name = "Research & Contrasting News",
                minutes = log.researchMins,
                pct = (log.researchMins * 100) / total,
                color = EchoBlueBright
            )
            Spacer(modifier = Modifier.height(10.dp))
            CategoryRow(
                name = "Entertainment & Passive Media",
                minutes = log.entertainmentMins,
                pct = (log.entertainmentMins * 100) / total,
                color = EchoAmber
            )
        }
    }
}

@Composable
fun CategoryRow(
    name: String,
    minutes: Int,
    pct: Int,
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = name, fontSize = 12.sp, color = EchoTextPrimary)
            Text(text = "${minutes}m ($pct%)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { pct / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color(0xFF161C38)
        )
    }
}
