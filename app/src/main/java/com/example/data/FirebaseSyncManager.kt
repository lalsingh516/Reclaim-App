package com.example.data

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirebaseSyncManager {
    private const val TAG = "FirebaseSyncManager"

    private var isFirebaseInitialized = false
    private var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null
        private set

    private val _isFirebaseActive = MutableStateFlow(false)
    val isFirebaseActive = _isFirebaseActive.asStateFlow()

    fun initialize(context: Context) {
        if (isFirebaseInitialized) return

        var apiKey = BuildConfig.FIREBASE_API_KEY
        var projectId = BuildConfig.FIREBASE_PROJECT_ID
        var appId = BuildConfig.FIREBASE_APP_ID

        // Fallback to user's direct production credentials if placeholders
        if (apiKey.isBlank() || apiKey == "MY_FIREBASE_API_KEY" || apiKey == "FIREBASE_API_KEY") {
            apiKey = "AIzaSyBpGUC_cauuKHneb-OZouKc6Bsmo8hVjkY"
        }
        if (projectId.isBlank() || projectId == "MY_FIREBASE_PROJECT_ID" || projectId == "FIREBASE_PROJECT_ID") {
            projectId = "reclaimapp-eeb52"
        }
        if (appId.isBlank() || appId == "MY_FIREBASE_APP_ID" || appId == "FIREBASE_APP_ID") {
            appId = "1:366043003014:android:100db20913934b75b8f907"
        }

        try {
            val options = FirebaseOptions.Builder()
                .setApiKey(apiKey)
                .setProjectId(projectId)
                .setApplicationId(appId)
                .build()

            val apps = FirebaseApp.getApps(context)
            val app = if (apps.isEmpty()) {
                FirebaseApp.initializeApp(context, options)
            } else {
                apps[0]
            }

            auth = FirebaseAuth.getInstance(app)
            firestore = FirebaseFirestore.getInstance(app)
            isFirebaseInitialized = true
            _isFirebaseActive.value = true
            Log.i(TAG, "Firebase successfully connected!")
        } catch (e: Throwable) {
            Log.e(TAG, "Error initializing programmable Firebase connection", e)
            _isFirebaseActive.value = false
            auth = null
            firestore = null
        }
    }

    fun isUserLoggedIn(): Boolean {
        return try {
            auth?.currentUser != null
        } catch (e: Throwable) {
            Log.e(TAG, "Error checking isUserLoggedIn", e)
            false
        }
    }

    fun getCurrentUid(): String? {
        return try {
            auth?.currentUser?.uid
        } catch (e: Throwable) {
            Log.e(TAG, "Error getting current user uid", e)
            null
        }
    }

    fun getFirebaseAuth(): FirebaseAuth? {
        return auth
    }

    suspend fun signUpWithFirebase(name: String, email: String, password: String): Pair<Boolean, String?> = withContext(Dispatchers.IO) {
        val authInstance = auth
        val firestoreInstance = firestore
        if (authInstance == null || firestoreInstance == null) {
            return@withContext Pair(false, "Firebase is running locally. Connect with actual Firebase settings in user secrets to register.")
        }

        try {
            val result = authInstance.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                val profile = hashMapOf(
                    "uid" to user.uid,
                    "name" to name,
                    "email" to email,
                    "onboardingComplete" to false,
                    "streakDays" to 1,
                    "focusScore" to 15,
                    "points" to 100,
                    "level" to 1,
                    "isPremium" to false
                )
                firestoreInstance.collection("users").document(user.uid).set(profile).await()
                return@withContext Pair(true, null)
            }
            Pair(false, "Unknown Firebase sign up error.")
        } catch (e: Exception) {
            Log.e(TAG, "Sign up error", e)
            Pair(false, e.localizedMessage ?: "Sign up failed via Firebase.")
        }
    }

    suspend fun loginWithFirebase(email: String, password: String): Pair<Boolean, String?> = withContext(Dispatchers.IO) {
        val authInstance = auth
        if (authInstance == null) {
            return@withContext Pair(false, "Firebase is running locally. Connect with actual Firebase settings in user secrets.")
        }

        try {
            authInstance.signInWithEmailAndPassword(email, password).await()
            Pair(true, null)
        } catch (e: Exception) {
            Log.e(TAG, "Login error", e)
            Pair(false, e.localizedMessage ?: "Login failed via Firebase")
        }
    }

    fun logout() {
        try {
            auth?.signOut()
        } catch (e: Throwable) {
            Log.e(TAG, "Error signing out", e)
        }
    }

    // --- Firestore Real-Time Upstream Sync Helpers ---
    suspend fun syncUserToFirestore(user: UserEntity) = withContext(Dispatchers.IO) {
        val firestoreInstance = firestore ?: return@withContext
        val uid = getCurrentUid() ?: return@withContext
        try {
            val userMap = hashMapOf(
                "uid" to uid,
                "email" to user.email,
                "name" to user.name,
                "onboardingComplete" to user.onboardingComplete,
                "onboardingStep" to user.onboardingStep,
                "streakDays" to user.streakDays,
                "focusScore" to user.focusScore,
                "totalFocusHours" to user.totalFocusHours,
                "points" to user.points,
                "level" to user.level,
                "isPremium" to user.isPremium,
                "phoneChecksDay" to user.phoneChecksDay,
                "selectedFocusGoal" to user.selectedFocusGoal,
                "waterCups" to user.waterCups,
                "sleepHours" to user.sleepHours,
                "currentSteps" to user.currentSteps
            )
            firestoreInstance.collection("users").document(uid).set(userMap).await()
        } catch (e: Exception) {
            Log.w(TAG, "Could not back up User Profile to Firestore: ${e.message}")
        }
    }

    suspend fun saveHabitToFirestore(habit: HabitEntity) = withContext(Dispatchers.IO) {
        val firestoreInstance = firestore ?: return@withContext
        val uid = getCurrentUid() ?: return@withContext
        try {
            val habitMap = hashMapOf(
                "id" to habit.id,
                "title" to habit.title,
                "schedule" to habit.schedule,
                "category" to habit.category,
                "completedToday" to habit.completedToday,
                "streak" to habit.streak
            )
            firestoreInstance.collection("users").document(uid)
                .collection("habits").document(habit.id.toString()).set(habitMap).await()
        } catch (e: Exception) {
            Log.w(TAG, "Could not sync Habit to Firestore: ${e.message}")
        }
    }

    suspend fun deleteHabitFromFirestore(habitId: Int) = withContext(Dispatchers.IO) {
        val firestoreInstance = firestore ?: return@withContext
        val uid = getCurrentUid() ?: return@withContext
        try {
            firestoreInstance.collection("users").document(uid)
                .collection("habits").document(habitId.toString()).delete().await()
        } catch (e: Exception) {
            Log.w(TAG, "Could not sync delete Habit from Firestore: ${e.message}")
        }
    }

    suspend fun saveJournalToFirestore(entry: JournalEntryEntity) = withContext(Dispatchers.IO) {
        val firestoreInstance = firestore ?: return@withContext
        val uid = getCurrentUid() ?: return@withContext
        try {
            val map = hashMapOf(
                "id" to entry.id,
                "date" to entry.date,
                "mood" to entry.mood,
                "reflection" to entry.reflection
            )
            firestoreInstance.collection("users").document(uid)
                .collection("journal_entries").document(entry.id.toString()).set(map).await()
        } catch (e: Exception) {
            Log.w(TAG, "Could not sync Journal Entry to Firestore: ${e.message}")
        }
    }

    suspend fun saveWorkoutToFirestore(workout: WorkoutEntity) = withContext(Dispatchers.IO) {
        val firestoreInstance = firestore ?: return@withContext
        val uid = getCurrentUid() ?: return@withContext
        try {
            val map = hashMapOf(
                "id" to workout.id,
                "title" to workout.title,
                "durationMinutes" to workout.durationMinutes,
                "intensity" to workout.intensity,
                "timestamp" to workout.timestamp
            )
            firestoreInstance.collection("users").document(uid)
                .collection("workouts").document(workout.id.toString()).set(map).await()
        } catch (e: Exception) {
            Log.w(TAG, "Could not sync Workout to Firestore: ${e.message}")
        }
    }

    suspend fun saveFocusSessionToFirestore(session: FocusSessionEntity) = withContext(Dispatchers.IO) {
        val firestoreInstance = firestore ?: return@withContext
        val uid = getCurrentUid() ?: return@withContext
        try {
            val map = hashMapOf(
                "id" to session.id,
                "taskTitle" to session.taskTitle,
                "durationSeconds" to session.durationSeconds,
                "completed" to session.completed,
                "timestamp" to session.timestamp
            )
            firestoreInstance.collection("users").document(uid)
                .collection("focus_sessions").document(session.id.toString()).set(map).await()
        } catch (e: Exception) {
            Log.w(TAG, "Could not sync Focus Session to Firestore: ${e.message}")
        }
    }

    suspend fun saveChatMessageToFirestore(msg: ChatMessageEntity) = withContext(Dispatchers.IO) {
        val firestoreInstance = firestore ?: return@withContext
        val uid = getCurrentUid() ?: return@withContext
        try {
            val map = hashMapOf(
                "id" to msg.id,
                "sender" to msg.sender,
                "text" to msg.text,
                "timestamp" to msg.timestamp,
                "options" to msg.options
            )
            firestoreInstance.collection("users").document(uid)
                .collection("chat_messages").document(msg.id.toString()).set(map).await()
        } catch (e: Exception) {
            Log.w(TAG, "Could not sync message to Firestore: ${e.message}")
        }
    }

    suspend fun clearChatMessagesFromFirestore() = withContext(Dispatchers.IO) {
        val firestoreInstance = firestore ?: return@withContext
        val uid = getCurrentUid() ?: return@withContext
        try {
            val snapshot = firestoreInstance.collection("users").document(uid).collection("chat_messages").get().await()
            for (doc in snapshot.documents) {
                doc.reference.delete()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Could not sync clear chat to Firestore: ${e.message}")
        }
    }

    // --- Firestore Downstream Sync (Fetch and Hydrate Room DB) ---
    suspend fun downloadRemoteDataToLocal(dao: ReclaimDao) = withContext(Dispatchers.IO) {
        val firestoreInstance = firestore ?: return@withContext
        val uid = getCurrentUid() ?: return@withContext
        try {
            // 1. Fetch User Data
            val userDoc = firestoreInstance.collection("users").document(uid).get().await()
            if (userDoc.exists()) {
                val email = userDoc.getString("email") ?: ""
                val name = userDoc.getString("name") ?: ""
                val onboardingComplete = userDoc.getBoolean("onboardingComplete") ?: false
                val onboardingStep = userDoc.getLong("onboardingStep")?.toInt() ?: 1
                val streakDays = userDoc.getLong("streakDays")?.toInt() ?: 12
                val focusScore = userDoc.getLong("focusScore")?.toInt() ?: 84
                val totalFocusHours = userDoc.getDouble("totalFocusHours")?.toFloat() ?: 142f
                val points = userDoc.getLong("points")?.toInt() ?: 2850
                val level = userDoc.getLong("level")?.toInt() ?: 12
                val isPremium = userDoc.getBoolean("isPremium") ?: false
                val phoneChecksDay = userDoc.getLong("phoneChecksDay")?.toInt() ?: 45
                val selectedFocusGoal = userDoc.getString("selectedFocusGoal") ?: "Reduce Screen Time"
                val waterCups = userDoc.getLong("waterCups")?.toInt() ?: 4
                val sleepHours = userDoc.getDouble("sleepHours")?.toFloat() ?: 7.5f
                val currentSteps = userDoc.getLong("currentSteps")?.toInt() ?: 8432

                dao.insertUser(
                    UserEntity(
                        id = 1,
                        email = email,
                        name = name,
                        isLoggedIn = true,
                        onboardingComplete = onboardingComplete,
                        onboardingStep = onboardingStep,
                        streakDays = streakDays,
                        focusScore = focusScore,
                        totalFocusHours = totalFocusHours,
                        points = points,
                        level = level,
                        isPremium = isPremium,
                        phoneChecksDay = phoneChecksDay,
                        selectedFocusGoal = selectedFocusGoal,
                        waterCups = waterCups,
                        sleepHours = sleepHours,
                        currentSteps = currentSteps
                    )
                )
            } else {
                // If profile doesn't exist, seed local user representing the authenticated email
                val authEmail = auth?.currentUser?.email ?: "user@reclaim.io"
                val defaultUsername = authEmail.substringBefore("@").replaceFirstChar { it.uppercase() }
                val defaultNewUser = UserEntity(
                    id = 1,
                    email = authEmail,
                    name = defaultUsername,
                    isLoggedIn = true,
                    onboardingComplete = false,
                    streakDays = 1,
                    focusScore = 15,
                    points = 100,
                    level = 1
                )
                dao.insertUser(defaultNewUser)
                syncUserToFirestore(defaultNewUser)
            }

            // 2. Hydrate Habits
            val habitsSnap = firestoreInstance.collection("users").document(uid).collection("habits").get().await()
            for (doc in habitsSnap.documents) {
                val title = doc.getString("title") ?: continue
                val schedule = doc.getString("schedule") ?: "All Day"
                val category = doc.getString("category") ?: "Focus session"
                val completedToday = doc.getBoolean("completedToday") ?: false
                val streak = doc.getLong("streak")?.toInt() ?: 0
                val habitIdStr = doc.id
                val habitId = habitIdStr.toIntOrNull() ?: 0

                dao.insertHabit(
                    HabitEntity(
                        id = habitId,
                        title = title,
                        schedule = schedule,
                        category = category,
                        completedToday = completedToday,
                        streak = streak
                    )
                )
            }

            // 3. Hydrate Journal Entries
            val journalSnap = firestoreInstance.collection("users").document(uid).collection("journal_entries").get().await()
            for (doc in journalSnap.documents) {
                val date = doc.getLong("date") ?: System.currentTimeMillis()
                val mood = doc.getString("mood") ?: "Calm"
                val reflection = doc.getString("reflection") ?: ""
                val entryId = doc.id.toIntOrNull() ?: 0

                dao.insertJournal(
                    JournalEntryEntity(
                        id = entryId,
                        date = date,
                        mood = mood,
                        reflection = reflection
                    )
                )
            }

            // 4. Hydrate Workouts
            val workoutSnap = firestoreInstance.collection("users").document(uid).collection("workouts").get().await()
            for (doc in workoutSnap.documents) {
                val title = doc.getString("title") ?: continue
                val durationMinutes = doc.getLong("durationMinutes")?.toInt() ?: 20
                val intensity = doc.getString("intensity") ?: "Low Intensity"
                val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                val workoutId = doc.id.toIntOrNull() ?: 0

                dao.insertWorkout(
                    WorkoutEntity(
                        id = workoutId,
                        title = title,
                        durationMinutes = durationMinutes,
                        intensity = intensity,
                        timestamp = timestamp
                    )
                )
            }

            // 5. Hydrate Focus Sessions
            val focusSnap = firestoreInstance.collection("users").document(uid).collection("focus_sessions").get().await()
            for (doc in focusSnap.documents) {
                val taskTitle = doc.getString("taskTitle") ?: "Deep Work"
                val durationSeconds = doc.getLong("durationSeconds")?.toInt() ?: 1500
                val completed = doc.getBoolean("completed") ?: true
                val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                val sessionId = doc.id.toIntOrNull() ?: 0

                dao.insertFocusSession(
                    FocusSessionEntity(
                        id = sessionId,
                        taskTitle = taskTitle,
                        durationSeconds = durationSeconds,
                        completed = completed,
                        timestamp = timestamp
                    )
                )
            }

            // 6. Hydrate Chat History
            val chatSnap = firestoreInstance.collection("users").document(uid).collection("chat_messages").get().await()
            if (!chatSnap.isEmpty) {
                dao.clearChat()
                for (doc in chatSnap.documents) {
                    val sender = doc.getString("sender") ?: continue
                    val text = doc.getString("text") ?: ""
                    val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                    val options = doc.getString("options") ?: ""
                    val msgId = doc.id.toIntOrNull() ?: 0

                    dao.insertChatMessage(
                        ChatMessageEntity(
                            id = msgId,
                            sender = sender,
                            text = text,
                            timestamp = timestamp,
                            options = options
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error hydrating data from Firestore: ${e.message}", e)
        }
    }
}
