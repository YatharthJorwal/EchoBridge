package com.example.data.repository

import com.example.data.db.EchoBridgeDao
import com.example.data.model.PerspectiveTopic
import com.example.data.model.ProductivitySession
import com.example.data.model.StressCheckIn
import com.example.data.model.WellnessLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EchoBridgeRepository(private val dao: EchoBridgeDao) {

    val allWellnessLogs: Flow<List<WellnessLog>> = dao.getAllWellnessLogs()
    val latestWellnessLog: Flow<WellnessLog?> = dao.getLatestWellnessLog()
    val allProductivitySessions: Flow<List<ProductivitySession>> = dao.getAllProductivitySessions()
    val allStressCheckIns: Flow<List<StressCheckIn>> = dao.getAllStressCheckIns()

    private val _perspectiveTopics = MutableStateFlow(getInitialTopics())
    val perspectiveTopics: StateFlow<List<PerspectiveTopic>> = _perspectiveTopics.asStateFlow()

    suspend fun checkAndSeedDatabase() {
        if (dao.getWellnessLogCount() == 0) {
            val now = System.currentTimeMillis()
            val dayMs = 24L * 60 * 60 * 1000

            val initialLogs = listOf(
                WellnessLog(
                    dayLabel = "Mon",
                    echoScore = 68,
                    perspectiveBalance = 58,
                    openMindedness = 72,
                    contentDiversity = 48,
                    screenTimeMinutes = 380, // 6h 20m
                    socialMins = 210,
                    productivityMins = 80,
                    researchMins = 45,
                    entertainmentMins = 45,
                    echoExposurePct = 34,
                    perspectivesCount = 11,
                    timestamp = now - 6 * dayMs
                ),
                WellnessLog(
                    dayLabel = "Tue",
                    echoScore = 72,
                    perspectiveBalance = 63,
                    openMindedness = 77,
                    contentDiversity = 52,
                    screenTimeMinutes = 340, // 5h 40m
                    socialMins = 180,
                    productivityMins = 95,
                    researchMins = 35,
                    entertainmentMins = 30,
                    echoExposurePct = 29,
                    perspectivesCount = 14,
                    timestamp = now - 5 * dayMs
                ),
                WellnessLog(
                    dayLabel = "Wed",
                    echoScore = 74,
                    perspectiveBalance = 66,
                    openMindedness = 81,
                    contentDiversity = 55,
                    screenTimeMinutes = 310, // 5h 10m
                    socialMins = 160,
                    productivityMins = 90,
                    researchMins = 30,
                    entertainmentMins = 30,
                    echoExposurePct = 25,
                    perspectivesCount = 16,
                    timestamp = now - 4 * dayMs
                ),
                WellnessLog(
                    dayLabel = "Thu",
                    echoScore = 77,
                    perspectiveBalance = 70,
                    openMindedness = 83,
                    contentDiversity = 57,
                    screenTimeMinutes = 295, // 4h 55m
                    socialMins = 140,
                    productivityMins = 100,
                    researchMins = 30,
                    entertainmentMins = 25,
                    echoExposurePct = 22,
                    perspectivesCount = 18,
                    timestamp = now - 3 * dayMs
                ),
                WellnessLog(
                    dayLabel = "Fri",
                    echoScore = 79,
                    perspectiveBalance = 73,
                    openMindedness = 85,
                    contentDiversity = 59,
                    screenTimeMinutes = 280, // 4h 40m
                    socialMins = 130,
                    productivityMins = 105,
                    researchMins = 25,
                    entertainmentMins = 20,
                    echoExposurePct = 20,
                    perspectivesCount = 19,
                    timestamp = now - 2 * dayMs
                ),
                WellnessLog(
                    dayLabel = "Sat",
                    echoScore = 80,
                    perspectiveBalance = 74,
                    openMindedness = 86,
                    contentDiversity = 60,
                    screenTimeMinutes = 275, // 4h 35m
                    socialMins = 120,
                    productivityMins = 110,
                    researchMins = 25,
                    entertainmentMins = 20,
                    echoExposurePct = 19,
                    perspectivesCount = 20,
                    timestamp = now - 1 * dayMs
                ),
                WellnessLog(
                    dayLabel = "Today",
                    echoScore = 82,
                    perspectiveBalance = 76,
                    openMindedness = 88,
                    contentDiversity = 61,
                    screenTimeMinutes = 268, // 4h 28m
                    socialMins = 112,
                    productivityMins = 96,
                    researchMins = 38,
                    entertainmentMins = 22,
                    echoExposurePct = 18,
                    perspectivesCount = 21,
                    timestamp = now
                )
            )
            dao.insertWellnessLogs(initialLogs)

            // Initial Productivity Sessions
            dao.insertProductivitySession(
                ProductivitySession(
                    title = "Dialectic Inquiry & Deep Research",
                    category = "Study",
                    durationMinutes = 45,
                    omsEarned = 15,
                    timestamp = now - 3 * 3600 * 1000
                )
            )
            dao.insertProductivitySession(
                ProductivitySession(
                    title = "Echo Chamber Literature Review",
                    category = "Research",
                    durationMinutes = 35,
                    omsEarned = 12,
                    timestamp = now - 7 * 3600 * 1000
                )
            )
            dao.insertProductivitySession(
                ProductivitySession(
                    title = "Algorithmic Diet Audit",
                    category = "Focus",
                    durationMinutes = 25,
                    omsEarned = 10,
                    timestamp = now - 22 * 3600 * 1000
                )
            )

            // Initial Stress Check-In
            dao.insertStressCheckIn(
                StressCheckIn(
                    stressLevel = 2,
                    stressLabel = "Balanced",
                    note = "Post-breathing exercise, relaxed cognitive state",
                    timestamp = now - 2 * 3600 * 1000
                )
            )
        }
    }

    suspend fun recordFocusSession(title: String, category: String, durationMinutes: Int, omsBonus: Int) {
        val session = ProductivitySession(
            title = title,
            category = category,
            durationMinutes = durationMinutes,
            omsEarned = omsBonus
        )
        dao.insertProductivitySession(session)
    }

    suspend fun logStressCheckIn(level: Int, label: String, note: String) {
        val checkIn = StressCheckIn(
            stressLevel = level,
            stressLabel = label,
            note = note
        )
        dao.insertStressCheckIn(checkIn)
    }

    suspend fun recordBreathingSessionCompleted() {
        val checkIn = StressCheckIn(
            stressLevel = 1,
            stressLabel = "Calm & Centered",
            note = "Completed 4-7-8 Guided Breathing Cycle"
        )
        dao.insertStressCheckIn(checkIn)
    }

    fun markTopicExplored(topicId: String) {
        _perspectiveTopics.value = _perspectiveTopics.value.map { topic ->
            if (topic.id == topicId) topic.copy(isExplored = true) else topic
        }
    }

    private fun getInitialTopics(): List<PerspectiveTopic> {
        return listOf(
            PerspectiveTopic(
                id = "topic_1",
                title = "Algorithmic Social Feeds & Polarization",
                category = "Social Dynamics",
                biasAlert = "High confirmation bias detected across feed loops.",
                viewA = "Engagement algorithms connect communities and surface personal interests efficiently.",
                viewB = "Outrage-driven algorithms incentivize tribal division and reduce exposure to nuance.",
                balancedInsight = "Personalization must be balanced with deliberate exposure to credible contrasting sources.",
                omsBonus = 12
            ),
            PerspectiveTopic(
                id = "topic_2",
                title = "Screen Time: Self-Regulation vs Platform Design",
                category = "Digital Wellbeing",
                biasAlert = "Over-emphasis on personal guilt without addressing dark design patterns.",
                viewA = "Individuals bear ultimate responsibility for setting boundaries with digital devices.",
                viewB = "Infinite scroll and notifications are engineered for behavioral addiction, requiring structural guardrails.",
                balancedInsight = "Effective digital health pairs individual mindfulness with intentional app limits and ethical tech design.",
                omsBonus = 10
            ),
            PerspectiveTopic(
                id = "topic_3",
                title = "AI in Academic & Creative Work",
                category = "Productivity",
                biasAlert = "Binary view of AI as either total replacement or pure shortcut.",
                viewA = "Generative AI democratizes learning, sparks brainstorming, and accelerates research.",
                viewB = "Over-reliance risks weakening critical inquiry, organic memory retention, and authentic voice.",
                balancedInsight = "AI is most valuable as a thought-partner for dialectic inquiry rather than an automated surrogate.",
                omsBonus = 15
            ),
            PerspectiveTopic(
                id = "topic_4",
                title = "Biometric Wearables in Mental Health",
                category = "Health Tech",
                biasAlert = "Potential for orthosomnia / biofeedback hyper-vigilance.",
                viewA = "Continuous HRV and stress tracking provides objective physical awareness before conscious burnout.",
                viewB = "Obsessing over real-time bio-metrics can inadvertently increase anxiety and health fixation.",
                balancedInsight = "Wearable data is a compass for gentle pacing, not a scoreboard to judge oneself by.",
                omsBonus = 8
            )
        )
    }
}
