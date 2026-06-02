package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ReclaimRepository(private val dao: ReclaimDao) {

    val userFlow: Flow<UserEntity?> = dao.getUserFlow()
    val habitsFlow: Flow<List<HabitEntity>> = dao.getHabitsFlow()
    val journalEntriesFlow: Flow<List<JournalEntryEntity>> = dao.getJournalEntriesFlow()
    val focusSessionsFlow: Flow<List<FocusSessionEntity>> = dao.getFocusSessionsFlow()
    val workoutsFlow: Flow<List<WorkoutEntity>> = dao.getWorkoutsFlow()
    val chatMessagesFlow: Flow<List<ChatMessageEntity>> = dao.getChatMessagesFlow()

    suspend fun getUserSync(): UserEntity? = withContext(Dispatchers.IO) {
        dao.getUserSync()
    }

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Seeds the database with high-quality mockup data if empty
    suspend fun seedDatabaseIfNeeded() {
        withContext(Dispatchers.IO) {
            // No default dummy data seeded automatically so the user starts fresh
        }
    }

    suspend fun saveUserProfile(user: UserEntity) = withContext(Dispatchers.IO) {
        dao.insertUser(user)
        FirebaseSyncManager.syncUserToFirestore(user)
    }

    suspend fun insertHabit(habit: HabitEntity) = withContext(Dispatchers.IO) {
        val rowId = dao.insertHabit(habit)
        val habitWithId = habit.copy(id = rowId.toInt())
        FirebaseSyncManager.saveHabitToFirestore(habitWithId)
    }

    suspend fun updateHabit(habit: HabitEntity) = withContext(Dispatchers.IO) {
        dao.updateHabit(habit)
        FirebaseSyncManager.saveHabitToFirestore(habit)
    }

    suspend fun deleteHabit(habit: HabitEntity) = withContext(Dispatchers.IO) {
        dao.deleteHabit(habit)
        FirebaseSyncManager.deleteHabitFromFirestore(habit.id)
    }

    suspend fun addJournalEntry(entry: JournalEntryEntity) = withContext(Dispatchers.IO) {
        val rowId = dao.insertJournal(entry)
        val entryWithId = entry.copy(id = rowId.toInt())
        FirebaseSyncManager.saveJournalToFirestore(entryWithId)
    }

    suspend fun logFocusSession(session: FocusSessionEntity) = withContext(Dispatchers.IO) {
        val rowId = dao.insertFocusSession(session)
        val sessionWithId = session.copy(id = rowId.toInt())
        FirebaseSyncManager.saveFocusSessionToFirestore(sessionWithId)

        // Gain experience points for focus sessions completed
        val current = dao.getUserSync()
        if (current != null && session.completed) {
            val gainedXP = (session.durationSeconds / 60) * 10
            val updatedUser = current.copy(
                points = current.points + gainedXP,
                totalFocusHours = current.totalFocusHours + (session.durationSeconds / 3600f)
            )
            dao.insertUser(updatedUser)
            FirebaseSyncManager.syncUserToFirestore(updatedUser)
        }
    }

    suspend fun logWorkout(workout: WorkoutEntity) = withContext(Dispatchers.IO) {
        val rowId = dao.insertWorkout(workout)
        val workoutWithId = workout.copy(id = rowId.toInt())
        FirebaseSyncManager.saveWorkoutToFirestore(workoutWithId)

        // Add 50 points per workout
        val current = dao.getUserSync()
        if (current != null) {
            val updatedUser = current.copy(points = current.points + 50)
            dao.insertUser(updatedUser)
            FirebaseSyncManager.syncUserToFirestore(updatedUser)
        }
    }

    suspend fun addChatMessage(sender: String, messageText: String, options: String = ""): ChatMessageEntity = withContext(Dispatchers.IO) {
        val msg = ChatMessageEntity(sender = sender, text = messageText, options = options)
        val rowId = dao.insertChatMessage(msg)
        val msgWithId = msg.copy(id = rowId.toInt())
        FirebaseSyncManager.saveChatMessageToFirestore(msgWithId)
        msgWithId
    }

    suspend fun clearChat() = withContext(Dispatchers.IO) {
        dao.clearChat()
        FirebaseSyncManager.clearChatMessagesFromFirestore()
    }

    // Call Gemini API REST Endpoint directly (Option B - Direct REST API)
    suspend fun generateCoachResponse(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            // Graceful fallback response when no real API key is configured in user secrets
            return@withContext getLocalFallbackResponse(prompt)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            
            // Generate full chat context
            val chatHistory = dao.getChatMessagesFlow().firstOrNull() ?: emptyList()
            val contentsArray = JSONArray()

            // System instructions can be added via the generation config
            val systemInstruction = "You are Reclaim AI Coach, an expert digital wellness mentor. Help the user break social media scrolling addiction (Instagram Reels, TikTok, YouTube Shorts). Keep responses highly encouraging, professional, concise, and structured. Encourage physical exercise (like push-ups), reading, or focus time."

            // Append history for multi-turn conversational memory
            for (msg in chatHistory.takeLast(10)) {
                val turn = JSONObject()
                turn.put("role", if (msg.sender == "AI") "model" else "user")
                val parts = JSONArray()
                val partObj = JSONObject()
                partObj.put("text", msg.text)
                parts.put(partObj)
                turn.put("parts", parts)
                contentsArray.put(turn)
            }

            // Append current user turn
            val currentTurn = JSONObject()
            currentTurn.put("role", "user")
            val currentParts = JSONArray()
            val currentPartObj = JSONObject()
            currentPartObj.put("text", prompt)
            currentParts.put(currentPartObj)
            currentTurn.put("parts", currentParts)
            contentsArray.put(currentTurn)

            // Compose final JSON Request Body
            val requestBodyJson = JSONObject()
            requestBodyJson.put("contents", contentsArray)

            // System instruction inside request, as supported in v1beta API
            val systemContent = JSONObject()
            val sysPart = JSONArray().put(JSONObject().put("text", systemInstruction))
            systemContent.put("parts", sysPart)
            requestBodyJson.put("systemInstruction", systemContent)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = requestBodyJson.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            httpClient.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val rootJson = JSONObject(responseBody)
                    val candidates = rootJson.optJSONArray("candidates")
                    val firstCandidate = candidates?.optJSONObject(0)
                    val content = firstCandidate?.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    val text = parts?.optJSONObject(0)?.optString("text")
                    if (!text.isNullOrEmpty()) {
                        return@withContext text
                    }
                }
                return@withContext "Error retrieving instructions. Please verify your internet connection. (HTTP code: ${response.code})"
            }
        } catch (e: Exception) {
            return@withContext "I'm processing your digital detox request offline. Let's redirect that screen energy into standard productivity tasks!"
        }
    }

    private fun getLocalFallbackResponse(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("habit") || q.contains("routines") -> {
                "Hi there! To build strong positive routines, try setting a micro-habit (like doing 5 push-ups every time you open YouTube). Would you like to create a new Fitness challenge?"
            }
            q.contains("scroll") || q.contains("uncontrolled") || q.contains("addict") -> {
                "Scrolling traps exploit dopamine triggers. The next time you open a feed, I'll prompt you with a 15-minute Deep Focus challenge instead. Taking control starts right now!"
            }
            q.contains("breath") || q.contains("calm") || q.contains("anxious") -> {
                "Let's ground ourselves. Put the screen face down, take 4 slow breaths—4s inhale, 4s hold, 4s exhale—and feel details of physical comfort. Reclaiming starts in the breath."
            }
            else -> {
                "You're making great progress towards screen neutrality. Today you completed several focus tasks already. Keep going—physical vitality and focus will serve you beautifully!"
            }
        }
    }
}
