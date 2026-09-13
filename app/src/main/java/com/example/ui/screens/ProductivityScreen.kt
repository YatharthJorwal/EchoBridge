package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PerspectiveTopic
import com.example.data.model.ProductivitySession
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
fun ProductivityScreen(
    sessions: List<ProductivitySession>,
    topics: List<PerspectiveTopic>,
    isTimerRunning: Boolean,
    timerRemainingSeconds: Int,
    selectedTimerMinutes: Int,
    currentTaskName: String,
    selectedCategory: String,
    onSetMinutes: (Int) -> Unit,
    onSetTaskDetails: (String, String) -> Unit,
    onToggleTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onOpenTopicDetail: (PerspectiveTopic) -> Unit,
    modifier: Modifier = Modifier
) {
    var taskInput by remember(currentTaskName) { mutableStateOf(currentTaskName) }
    val categories = listOf("Study", "Research", "Focus", "Coding", "Writing")

    val minutes = timerRemainingSeconds / 60
    val seconds = timerRemainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "PRODUCTIVITY & COGNITIVE DIVERSITY",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EchoBlueBright,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Deep Focus & Perspective Balancing",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = EchoTextPrimary
            )
        }

        // Active Focus Mode Timer Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("focus_timer_card"),
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FlashOn, contentDescription = null, tint = EchoAmber, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Deep Work Protocol", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                        }

                        // Preset Minutes
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(25, 45, 60).forEach { mins ->
                                val isSelected = selectedTimerMinutes == mins
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) EchoBlue else EchoBgElevated)
                                        .clickable { onSetMinutes(mins) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${mins}m",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else EchoTextSecondary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Timer Countdown Display
                    Text(
                        text = timeFormatted,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isTimerRunning) EchoBlueBright else EchoTextPrimary,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Task Name Input
                    OutlinedTextField(
                        value = taskInput,
                        onValueChange = {
                            taskInput = it
                            onSetTaskDetails(it, selectedCategory)
                        },
                        label = { Text("Active Session Target", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("task_input_field"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EchoBlueBright,
                            unfocusedBorderColor = EchoCardBorder,
                            focusedTextColor = EchoTextPrimary,
                            unfocusedTextColor = EchoTextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Category Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSelected = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) EchoPurple.copy(alpha = 0.25f) else EchoBgElevated)
                                    .border(1.dp, if (isSelected) EchoPurple else EchoCardBorder, RoundedCornerShape(8.dp))
                                    .clickable { onSetTaskDetails(taskInput, cat) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) EchoPurple else EchoTextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Action Buttons (Play / Stop / Reset)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onToggleTimer,
                            modifier = Modifier.weight(1f).height(46.dp).testTag("timer_toggle_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isTimerRunning) EchoAmber else EchoBlue
                            )
                        ) {
                            Icon(
                                imageVector = if (isTimerRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isTimerRunning) "Pause Session" else "Start Deep Work",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = onResetTimer,
                            modifier = Modifier.height(46.dp).testTag("timer_reset_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EchoBgElevated)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = EchoTextSecondary)
                        }
                    }
                }
            }
        }

        // Open-Mindedness Score (OMS) & Perspective Balancing Engine
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("perspective_engine_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(EchoCyan.copy(alpha = 0.4f), EchoEmerald.copy(alpha = 0.4f))
                    )
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CompassCalibration, contentDescription = null, tint = EchoCyan, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Perspective Balancing AI", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(EchoCyan.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "OMS: 88 / 100", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EchoCyan)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Break through algorithmic echo chambers by examining balanced dialectic perspectives across contemporary issues.",
                        fontSize = 11.sp,
                        color = EchoTextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Topic List
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        topics.forEach { topic ->
                            PerspectiveTopicRow(
                                topic = topic,
                                onClick = { onOpenTopicDetail(topic) }
                            )
                        }
                    }
                }
            }
        }

        // Recent Productivity Sessions History
        item {
            Text(
                text = "RECENT FOCUS SESSIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EchoTextMuted,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (sessions.isEmpty()) {
                Text(text = "No focus sessions recorded yet today.", fontSize = 12.sp, color = EchoTextSecondary)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    sessions.take(4).forEach { session ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = EchoBgSurface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EchoEmerald, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = session.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = EchoTextPrimary)
                                        Text(text = "${session.category} · ${session.durationMinutes} minutes", fontSize = 11.sp, color = EchoTextSecondary)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(EchoPurple.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(text = "+${session.omsEarned} OMS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EchoPurple)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PerspectiveTopicRow(
    topic: PerspectiveTopic,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(EchoBgElevated)
            .border(1.dp, if (topic.isExplored) EchoEmerald.copy(alpha = 0.4f) else EchoCardBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = topic.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = EchoTextPrimary)
            Text(text = "${topic.category} · ${topic.biasAlert}", fontSize = 10.sp, color = EchoTextMuted, maxLines = 1)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (topic.isExplored) EchoEmerald.copy(alpha = 0.15f) else EchoBlue.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (topic.isExplored) "Explored ✓" else "+${topic.omsBonus} OMS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (topic.isExplored) EchoEmerald else EchoBlueBright
            )
        }
    }
}
