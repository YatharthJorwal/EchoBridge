package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.EchoBridgeTopBar
import com.example.ui.components.PerspectiveTopicDialog
import com.example.ui.components.PreorderDialog
import com.example.ui.components.StressCheckInDialog
import com.example.ui.screens.HealthScreen
import com.example.ui.screens.OverviewScreen
import com.example.ui.screens.ProductivityScreen
import com.example.ui.screens.ScreenTimeScreen
import com.example.ui.screens.WearablesScreen
import com.example.ui.theme.EchoBgElevated
import com.example.ui.theme.EchoBgSurface
import com.example.ui.theme.EchoBlue
import com.example.ui.theme.EchoBlueBright
import com.example.ui.theme.EchoBridgeTheme
import com.example.ui.theme.EchoCardBorder
import com.example.ui.theme.EchoPurple
import com.example.ui.theme.EchoTextMuted
import com.example.ui.theme.EchoTextPrimary
import com.example.ui.theme.EchoTextSecondary
import com.example.ui.viewmodel.EchoBridgeViewModel
import com.example.ui.viewmodel.NavigationTab

class MainActivity : ComponentActivity() {

    private val viewModel: EchoBridgeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EchoBridgeTheme {
                EchoBridgeApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun EchoBridgeApp(viewModel: EchoBridgeViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            EchoBridgeTopBar(
                isRingPaired = uiState.isRingPaired,
                onOpenDevices = { viewModel.selectTab(NavigationTab.WEARABLES) },
                onOpenCheckIn = { viewModel.openStressDialog(true) }
            )
        },
        bottomBar = {
            EchoBridgeBottomNavBar(
                selectedTab = uiState.selectedTab,
                onSelectTab = { viewModel.selectTab(it) }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.selectedTab) {
                NavigationTab.OVERVIEW -> {
                    OverviewScreen(
                        todayLog = uiState.todayLog,
                        onNavigateTab = { viewModel.selectTab(it) },
                        onStartBreather = {
                            viewModel.selectTab(NavigationTab.HEALTH)
                            if (!uiState.isBreatherRunning) viewModel.toggleBreather()
                        },
                        onOpenCheckIn = { viewModel.openStressDialog(true) }
                    )
                }
                NavigationTab.SCREEN_TIME -> {
                    ScreenTimeScreen(
                        wellnessLogs = uiState.wellnessLogs,
                        todayLog = uiState.todayLog
                    )
                }
                NavigationTab.PRODUCTIVITY -> {
                    ProductivityScreen(
                        sessions = uiState.productivitySessions,
                        topics = uiState.topics,
                        isTimerRunning = uiState.isTimerRunning,
                        timerRemainingSeconds = uiState.timerRemainingSeconds,
                        selectedTimerMinutes = uiState.selectedTimerMinutes,
                        currentTaskName = uiState.currentTaskName,
                        selectedCategory = uiState.selectedTaskCategory,
                        onSetMinutes = { viewModel.setTimerMinutes(it) },
                        onSetTaskDetails = { name, cat -> viewModel.setTaskDetails(name, cat) },
                        onToggleTimer = { viewModel.toggleFocusTimer() },
                        onResetTimer = { viewModel.resetFocusTimer() },
                        onOpenTopicDetail = { viewModel.openTopicDetail(it) }
                    )
                }
                NavigationTab.HEALTH -> {
                    HealthScreen(
                        sensorDataState = uiState.sensorDataState,
                        todayLog = uiState.todayLog,
                        stressCheckIns = uiState.stressCheckIns,
                        liveBpm = uiState.liveBpm,
                        liveHrv = uiState.liveHrvMs,
                        isBreatherRunning = uiState.isBreatherRunning,
                        breathPhase = uiState.breathPhase,
                        breathSecondsLeft = uiState.breathSecondsLeft,
                        breathCyclesCompleted = uiState.breathCyclesCompleted,
                        onToggleBreather = { viewModel.toggleBreather() },
                        onOpenCheckInDialog = { viewModel.openStressDialog(true) },
                        onNavigateWearables = { viewModel.selectTab(NavigationTab.WEARABLES) }
                    )
                }
                NavigationTab.WEARABLES -> {
                    WearablesScreen(
                        sensorDataState = uiState.sensorDataState,
                        isScanning = uiState.isBtScanning,
                        isRingPaired = uiState.isRingPaired,
                        batteryPct = uiState.ringBatteryPct,
                        liveBpm = uiState.liveBpm,
                        liveHrv = uiState.liveHrvMs,
                        recoveryPct = uiState.recoveryPct,
                        onStartScan = { viewModel.startBluetoothScan() },
                        onTogglePairRing = { viewModel.togglePairEchoRing() },
                        onOpenPreorder = { viewModel.openPreorderDialog(true) }
                    )
                }
            }
        }
    }

    // Stress Check-in Dialog
    if (uiState.showStressCheckInDialog) {
        StressCheckInDialog(
            onDismiss = { viewModel.openStressDialog(false) },
            onSubmit = { level, label, note ->
                viewModel.submitStressCheckIn(level, label, note)
            }
        )
    }

    // Perspective Topic Dialectic Dialog
    uiState.selectedTopicDetail?.let { topic ->
        PerspectiveTopicDialog(
            topic = topic,
            onDismiss = { viewModel.openTopicDetail(null) },
            onAcknowledge = { viewModel.markTopicExplored(it) }
        )
    }

    // EchoRing Pre-Order Dialog
    if (uiState.showPreorderDialog) {
        PreorderDialog(
            onDismiss = { viewModel.openPreorderDialog(false) },
            onConfirmPreorder = { name, email ->
                viewModel.openPreorderDialog(false)
            }
        )
    }
}

@Composable
fun EchoBridgeBottomNavBar(
    selectedTab: NavigationTab,
    onSelectTab: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .border(1.dp, EchoCardBorder.copy(alpha = 0.5f), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        containerColor = EchoBgSurface,
        tonalElevation = 8.dp
    ) {
        NavigationTab.values().forEach { tab ->
            val isSelected = selectedTab == tab
            val icon = when (tab) {
                NavigationTab.OVERVIEW -> Icons.Default.Dashboard
                NavigationTab.SCREEN_TIME -> Icons.Default.HourglassBottom
                NavigationTab.PRODUCTIVITY -> Icons.Default.CompassCalibration
                NavigationTab.HEALTH -> Icons.Default.Favorite
                NavigationTab.WEARABLES -> Icons.Default.Watch
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectTab(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = tab.title
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = EchoBlueBright,
                    selectedTextColor = EchoBlueBright,
                    indicatorColor = EchoBgElevated,
                    unselectedIconColor = EchoTextMuted,
                    unselectedTextColor = EchoTextMuted
                ),
                modifier = Modifier.testTag("nav_${tab.name.lowercase()}")
            )
        }
    }
}
