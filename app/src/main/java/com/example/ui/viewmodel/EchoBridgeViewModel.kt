package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.EchoBridgeDatabase
import com.example.data.model.PerspectiveTopic
import com.example.data.model.ProductivitySession
import com.example.data.model.SensorDataState
import com.example.data.model.StressCheckIn
import com.example.data.model.WellnessLog
import com.example.data.repository.EchoBridgeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class NavigationTab(val title: String) {
    OVERVIEW("Overview"),
    SCREEN_TIME("Screen Time"),
    PRODUCTIVITY("Focus & OMS"),
    HEALTH("Health & Calm"),
    WEARABLES("EchoRing & BT")
}

enum class BreathPhase(val label: String, val seconds: Int) {
    INHALE("Inhale gently...", 4),
    HOLD("Hold breath...", 7),
    EXHALE("Exhale slowly...", 8)
}

data class EchoBridgeUiState(
    val selectedTab: NavigationTab = NavigationTab.OVERVIEW,
    val wellnessLogs: List<WellnessLog> = emptyList(),
    val todayLog: WellnessLog? = null,
    val productivitySessions: List<ProductivitySession> = emptyList(),
    val stressCheckIns: List<StressCheckIn> = emptyList(),
    val topics: List<PerspectiveTopic> = emptyList(),
    
    // Focus Timer
    val isTimerRunning: Boolean = false,
    val timerRemainingSeconds: Int = 25 * 60,
    val selectedTimerMinutes: Int = 25,
    val currentTaskName: String = "Dialectic Inquiry & Focus",
    val selectedTaskCategory: String = "Research",

    // Guided Breather Pacer
    val isBreatherRunning: Boolean = false,
    val breathPhase: BreathPhase = BreathPhase.INHALE,
    val breathSecondsLeft: Int = 4,
    val breathCyclesCompleted: Int = 0,

    // Wearables & Biometric Sensor Data Availability
    val sensorDataState: SensorDataState = SensorDataState.UNAVAILABLE,
    val isBtScanning: Boolean = false,
    val isRingPaired: Boolean = false,
    val showPreorderDialog: Boolean = false,
    val ringBatteryPct: Int? = null,
    val liveBpm: Int? = null,
    val liveHrvMs: Int? = null,
    val recoveryPct: Int? = null,

    // Dialogs & Sheets
    val showStressCheckInDialog: Boolean = false,
    val selectedTopicDetail: PerspectiveTopic? = null,
    val toastMessage: String? = null
)

class EchoBridgeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EchoBridgeRepository
    private var timerJob: Job? = null
    private var breathJob: Job? = null
    private var scanJob: Job? = null

    private val _uiState = MutableStateFlow(EchoBridgeUiState())
    val uiState: StateFlow<EchoBridgeUiState>

    private data class DbData(
        val logs: List<WellnessLog>,
        val latest: WellnessLog?,
        val sessions: List<ProductivitySession>,
        val checkins: List<StressCheckIn>,
        val topics: List<PerspectiveTopic>
    )

    init {
        val db = EchoBridgeDatabase.getDatabase(application)
        repository = EchoBridgeRepository(db.echoBridgeDao())

        viewModelScope.launch {
            repository.checkAndSeedDatabase()
        }

        val dbDataFlow = combine(
            repository.allWellnessLogs,
            repository.latestWellnessLog,
            repository.allProductivitySessions,
            repository.allStressCheckIns,
            repository.perspectiveTopics
        ) { logs, latest, sessions, checkins, topics ->
            DbData(logs, latest, sessions, checkins, topics)
        }

        uiState = combine(_uiState, dbDataFlow) { state, dbData ->
            state.copy(
                wellnessLogs = dbData.logs,
                todayLog = dbData.latest ?: dbData.logs.lastOrNull(),
                productivitySessions = dbData.sessions,
                stressCheckIns = dbData.checkins,
                topics = dbData.topics
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EchoBridgeUiState()
        )
    }

    fun selectTab(tab: NavigationTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    // --- Focus Timer Controls ---
    fun setTimerMinutes(minutes: Int) {
        if (!_uiState.value.isTimerRunning) {
            _uiState.update {
                it.copy(
                    selectedTimerMinutes = minutes,
                    timerRemainingSeconds = minutes * 60
                )
            }
        }
    }

    fun setTaskDetails(taskName: String, category: String) {
        _uiState.update {
            it.copy(
                currentTaskName = taskName,
                selectedTaskCategory = category
            )
        }
    }

    fun toggleFocusTimer() {
        val isRunning = _uiState.value.isTimerRunning
        if (isRunning) {
            timerJob?.cancel()
            _uiState.update { it.copy(isTimerRunning = false) }
        } else {
            _uiState.update { it.copy(isTimerRunning = true) }
            timerJob = viewModelScope.launch {
                while (_uiState.value.timerRemainingSeconds > 0) {
                    delay(1000)
                    _uiState.update { it.copy(timerRemainingSeconds = it.timerRemainingSeconds - 1) }
                }
                // Completed
                val duration = _uiState.value.selectedTimerMinutes
                val task = _uiState.value.currentTaskName
                val cat = _uiState.value.selectedTaskCategory
                repository.recordFocusSession(task, cat, duration, duration / 2)
                _uiState.update {
                    it.copy(
                        isTimerRunning = false,
                        timerRemainingSeconds = it.selectedTimerMinutes * 60,
                        toastMessage = "Focus session '$task' finished! +${duration / 2} OMS earned."
                    )
                }
            }
        }
    }

    fun resetFocusTimer() {
        timerJob?.cancel()
        _uiState.update {
            it.copy(
                isTimerRunning = false,
                timerRemainingSeconds = it.selectedTimerMinutes * 60
            )
        }
    }

    // --- Guided 4-7-8 Breather Pacer ---
    fun toggleBreather() {
        val isRunning = _uiState.value.isBreatherRunning
        if (isRunning) {
            breathJob?.cancel()
            _uiState.update { it.copy(isBreatherRunning = false) }
        } else {
            _uiState.update {
                it.copy(
                    isBreatherRunning = true,
                    breathPhase = BreathPhase.INHALE,
                    breathSecondsLeft = BreathPhase.INHALE.seconds,
                    breathCyclesCompleted = 0
                )
            }
            breathJob = viewModelScope.launch {
                while (_uiState.value.isBreatherRunning) {
                    val currentPhase = _uiState.value.breathPhase
                    var seconds = currentPhase.seconds
                    while (seconds > 0 && _uiState.value.isBreatherRunning) {
                        _uiState.update { it.copy(breathSecondsLeft = seconds) }
                        delay(1000)
                        seconds--
                    }
                    if (!_uiState.value.isBreatherRunning) break

                    val nextPhase = when (currentPhase) {
                        BreathPhase.INHALE -> BreathPhase.HOLD
                        BreathPhase.HOLD -> BreathPhase.EXHALE
                        BreathPhase.EXHALE -> {
                            val newCycles = _uiState.value.breathCyclesCompleted + 1
                            if (newCycles >= 3) {
                                repository.recordBreathingSessionCompleted()
                                _uiState.update {
                                    it.copy(
                                        isBreatherRunning = false,
                                        breathCyclesCompleted = newCycles,
                                        toastMessage = "Breathing exercise complete. Stress level lowered to Calm."
                                    )
                                }
                                break
                            }
                            _uiState.update { it.copy(breathCyclesCompleted = newCycles) }
                            BreathPhase.INHALE
                        }
                    }
                    _uiState.update { it.copy(breathPhase = nextPhase, breathSecondsLeft = nextPhase.seconds) }
                }
            }
        }
    }

    // --- Stress Check-In ---
    fun openStressDialog(open: Boolean) {
        _uiState.update { it.copy(showStressCheckInDialog = open) }
    }

    fun submitStressCheckIn(level: Int, label: String, note: String) {
        viewModelScope.launch {
            repository.logStressCheckIn(level, label, note)
            _uiState.update {
                it.copy(
                    showStressCheckInDialog = false,
                    toastMessage = "Stress assessment recorded: $label."
                )
            }
        }
    }

    // --- Perspective Balancing / Echo Topics ---
    fun openTopicDetail(topic: PerspectiveTopic?) {
        _uiState.update { it.copy(selectedTopicDetail = topic) }
    }

    fun markTopicExplored(topic: PerspectiveTopic) {
        repository.markTopicExplored(topic.id)
        _uiState.update {
            it.copy(
                selectedTopicDetail = null,
                toastMessage = "Explored '${topic.title}'! +${topic.omsBonus} Open-Mindedness Score points."
            )
        }
    }

    // --- Bluetooth & Wearable Pairing Flow ---
    fun startBluetoothScan() {
        if (_uiState.value.isBtScanning) return
        _uiState.update { it.copy(isBtScanning = true) }
        scanJob?.cancel()
        scanJob = viewModelScope.launch {
            delay(2000) // Realistic BLE discovery duration
            _uiState.update {
                it.copy(
                    isBtScanning = false,
                    toastMessage = "Discovered: EchoRing Smart Wearable [Pilot Prototype]"
                )
            }
        }
    }

    fun togglePairEchoRing() {
        val currentlyPaired = _uiState.value.isRingPaired
        if (!currentlyPaired) {
            // Pair device: state moves to CONNECTED, but sensor telemetry stream is pending firmware
            _uiState.update {
                it.copy(
                    isRingPaired = true,
                    sensorDataState = SensorDataState.CONNECTED,
                    ringBatteryPct = 88,
                    liveBpm = null,
                    liveHrvMs = null,
                    recoveryPct = null,
                    toastMessage = "Paired with EchoRing Prototype. Hardware connected; telemetry stream pending firmware."
                )
            }
        } else {
            // Disconnect device: state moves to UNAVAILABLE
            _uiState.update {
                it.copy(
                    isRingPaired = false,
                    sensorDataState = SensorDataState.UNAVAILABLE,
                    ringBatteryPct = null,
                    liveBpm = null,
                    liveHrvMs = null,
                    recoveryPct = null,
                    toastMessage = "EchoRing disconnected. Biometric sensor data is unavailable."
                )
            }
        }
    }

    fun setSensorState(state: SensorDataState) {
        _uiState.update { it.copy(sensorDataState = state) }
    }

    fun openPreorderDialog(open: Boolean) {
        _uiState.update { it.copy(showPreorderDialog = open) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
