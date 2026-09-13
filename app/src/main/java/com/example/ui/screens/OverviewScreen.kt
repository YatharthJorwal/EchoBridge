package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.ui.viewmodel.NavigationTab

@Composable
fun OverviewScreen(
    todayLog: WellnessLog?,
    onNavigateTab: (NavigationTab) -> Unit,
    onStartBreather: () -> Unit,
    onOpenCheckIn: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            // Section Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WELLNESS OVERVIEW",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EchoBlueBright,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Cognitive Balance & Health",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = EchoTextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(EchoBgElevated)
                        .border(1.dp, EchoEmerald.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(EchoEmerald)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Live Active",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EchoEmerald
                        )
                    }
                }
            }
        }

        // Hero Score & Circular Progress Card
        item {
            EchoScoreCard(log = log)
        }

        // 4-Tile Health & Wellbeing Metrics
        item {
            MetricsGrid(log = log, onNavigateTab = onNavigateTab)
        }

        // Actionable AI Insights Card
        item {
            ActionableInsightsCard(
                log = log,
                onExplorePerspective = { onNavigateTab(NavigationTab.PRODUCTIVITY) }
            )
        }

        // Quick Wellness Actions
        item {
            Text(
                text = "DAILY INTERVENTIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EchoTextMuted,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionPill(
                    icon = Icons.Default.SelfImprovement,
                    title = "4-7-8 Breather",
                    subtitle = "Vagal Calm",
                    color = EchoCyan,
                    modifier = Modifier.weight(1f).testTag("breather_action"),
                    onClick = onStartBreather
                )
                QuickActionPill(
                    icon = Icons.Default.Favorite,
                    title = "Check Vitals",
                    subtitle = "Log BPM",
                    color = EchoPurple,
                    modifier = Modifier.weight(1f).testTag("vitals_action"),
                    onClick = onOpenCheckIn
                )
                QuickActionPill(
                    icon = Icons.Default.Watch,
                    title = "EchoRing",
                    subtitle = "HW Beta",
                    color = EchoBlueBright,
                    modifier = Modifier.weight(1f).testTag("wearables_action"),
                    onClick = { onNavigateTab(NavigationTab.WEARABLES) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun EchoScoreCard(log: WellnessLog) {
    val animatedProgress by animateFloatAsState(
        targetValue = log.echoScore / 100f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "echoScoreProgress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("echo_score_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(EchoBlue.copy(alpha = 0.4f), EchoPurple.copy(alpha = 0.4f))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circular Meter
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(140.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 14.dp.toPx()
                    val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                    val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                    // Track
                    drawArc(
                        color = Color(0xFF161C38),
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Progress
                    drawArc(
                        brush = Brush.linearGradient(listOf(EchoBlue, EchoPurple, EchoCyan)),
                        startAngle = 135f,
                        sweepAngle = 270f * animatedProgress,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${log.echoScore}",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EchoTextPrimary
                    )
                    Text(
                        text = "ECHO SCORE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EchoBlueBright,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3 Perspective Balance Gauges
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PerspectiveBar(
                    label = "Perspective Balance",
                    value = log.perspectiveBalance,
                    color = EchoBlueBright
                )
                PerspectiveBar(
                    label = "Open-Mindedness",
                    value = log.openMindedness,
                    color = EchoEmerald
                )
                PerspectiveBar(
                    label = "Content Diversity",
                    value = log.contentDiversity,
                    color = EchoAmber
                )
            }
        }
    }
}

@Composable
fun PerspectiveBar(
    label: String,
    value: Int,
    color: Color
) {
    val progress by animateFloatAsState(
        targetValue = value / 100f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "pBar"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 12.sp, color = EchoTextSecondary)
            Text(text = "$value%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color(0xFF161C38)
        )
    }
}

@Composable
fun MetricsGrid(
    log: WellnessLog,
    onNavigateTab: (NavigationTab) -> Unit
) {
    val hours = log.screenTimeMinutes / 60
    val mins = log.screenTimeMinutes % 60
    val screenTimeStr = "${hours}h ${mins}m"

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricTile(
                title = "Screen Time",
                value = screenTimeStr,
                badge = "Today",
                badgeColor = EchoBlueBright,
                icon = Icons.Default.Smartphone,
                modifier = Modifier.weight(1f).clickable { onNavigateTab(NavigationTab.SCREEN_TIME) }
            )
            MetricTile(
                title = "Echo Exposure",
                value = "↓ ${log.echoExposurePct}%",
                badge = "Reduced",
                badgeColor = EchoEmerald,
                icon = Icons.Default.TrendingDown,
                modifier = Modifier.weight(1f).clickable { onNavigateTab(NavigationTab.PRODUCTIVITY) }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricTile(
                title = "Perspectives",
                value = "${log.perspectivesCount}",
                badge = "Balanced",
                badgeColor = EchoPurple,
                icon = Icons.Default.Visibility,
                modifier = Modifier.weight(1f).clickable { onNavigateTab(NavigationTab.PRODUCTIVITY) }
            )
            MetricTile(
                title = "Biometrics",
                value = "Ready",
                badge = "Hardware",
                badgeColor = EchoCyan,
                icon = Icons.Default.Favorite,
                modifier = Modifier.weight(1f).clickable { onNavigateTab(NavigationTab.HEALTH) }
            )
        }
    }
}

@Composable
fun MetricTile(
    title: String,
    value: String,
    badge: String,
    badgeColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(EchoCardBorder, EchoCardBorder.copy(alpha = 0.5f))))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = badgeColor,
                    modifier = Modifier.size(18.dp)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeColor.copy(alpha = 0.12f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = badge, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = badgeColor)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = EchoTextPrimary)
            Text(text = title, fontSize = 11.sp, color = EchoTextSecondary)
        }
    }
}

@Composable
fun ActionableInsightsCard(
    log: WellnessLog,
    onExplorePerspective: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = EchoBgElevated),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(EchoPurple.copy(alpha = 0.6f), EchoBlue.copy(alpha = 0.6f))))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = EchoPurple,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ACTIONABLE COGNITIVE INSIGHT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EchoPurple,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your evening information intake shifted toward algorithmic confirmation bias (+14% echo concentration).",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = EchoTextPrimary,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Engaging with 1 counter-perspective on 'AI & Education' will restore your Content Diversity index above 70% and earn +15 OMS.",
                fontSize = 12.sp,
                color = EchoTextSecondary,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(EchoBlue.copy(alpha = 0.15f))
                    .border(1.dp, EchoBlue.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .clickable { onExplorePerspective() }
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CompassCalibration,
                    contentDescription = null,
                    tint = EchoBlueBright,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Launch Perspective Balancing AI →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EchoBlueBright
                )
            }
        }
    }
}

@Composable
fun QuickActionPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = EchoBgSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(color.copy(alpha = 0.3f), EchoCardBorder)))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EchoTextPrimary)
            Text(text = subtitle, fontSize = 9.sp, color = EchoTextMuted)
        }
    }
}
