package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ReclaimApp
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReclaimViewModel(
    application: Application,
    private val repository: ReclaimRepository
) : AndroidViewModel(application) {

    // --- Database-Backed Flows ---
    val userState: StateFlow<UserEntity?> = repository.userFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val habitsState: StateFlow<List<HabitEntity>> = repository.habitsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val journalEntriesState: StateFlow<List<JournalEntryEntity>> = repository.journalEntriesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val focusSessionsState: StateFlow<List<FocusSessionEntity>> = repository.focusSessionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workoutsState: StateFlow<List<WorkoutEntity>> = repository.workoutsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessagesState: StateFlow<List<ChatMessageEntity>> = repository.chatMessagesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    // --- Local Transient UI States ---
    private val _currentRoute = MutableStateFlow("auth_welcome")
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    private val _selectedTab = MutableStateFlow("dashboard") // "dashboard", "habits", "focus", "coach", "profile"
    val selectedTab: StateFlow<String> = _selectedTab.asStateFlow()

    // Authentication States
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()


    // Focus Timer Engine
    private val _timerSecondsRemaining = MutableStateFlow(1500) // 25:00 default
    val timerSecondsRemaining: StateFlow<Int> = _timerSecondsRemaining.asStateFlow()

    private val _timerState = MutableStateFlow("idle") // "idle", "running", "paused"
    val timerState: StateFlow<String> = _timerState.asStateFlow()

    private val _timerActiveTask = MutableStateFlow("Website Refactoring")
    val timerActiveTask: StateFlow<String> = _timerActiveTask.asStateFlow()

    private var timerJob: Job? = null


    // AI Coach UI State
    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()


    // Onboarding Setup State
    val onboardingGoal = MutableStateFlow("Reduce Screen Time")
    val onboardingChecks = MutableStateFlow(45)
    val onboardingName = MutableStateFlow("")
    val onboardingEmail = MutableStateFlow("")


    // Analytics Tab Segment state (Day, Week, Month)
    private val _analyticsSegment = MutableStateFlow("Week")
    val analyticsSegment: StateFlow<String> = _analyticsSegment.asStateFlow()

    init {
        viewModelScope.launch {
            // Seed base database if first boot
            repository.seedDatabaseIfNeeded()
            
            if (com.example.data.FirebaseSyncManager.isFirebaseActive.value &&
                com.example.data.FirebaseSyncManager.isUserLoggedIn()) {
                // Hydrate local SQLite from Firestore if they are logged in with Firebase
                val app = getApplication<ReclaimApp>()
                com.example.data.FirebaseSyncManager.downloadRemoteDataToLocal(app.database.reclaimDao())
            }
            
            // Check if user is logged in
            val user = repository.getUserSync()
            if (user != null && user.isLoggedIn) {
                _currentRoute.value = if (user.onboardingComplete) "main_dashboard" else "onboarding_welcome"
            } else {
                _currentRoute.value = "auth_welcome"
            }
        }
    }

    // --- Navigation ---
    fun navigateTo(route: String) {
        _currentRoute.value = route
    }

    fun selectTab(tab: String) {
        _selectedTab.value = tab
    }

    fun setAnalyticsSegment(segment: String) {
        _analyticsSegment.value = segment
    }


    // --- AUTH ACTIONS (Direct firebase auth + room local validation) ---
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authError.value = null
            if (email.isBlank() || password.isBlank()) {
                _authError.value = "Email and Password cannot be empty."
                return@launch
            }
            if (password.length < 6) {
                _authError.value = "Password must be at least 6 characters."
                return@launch
            }

            val app = getApplication<ReclaimApp>()
            val dao = app.database.reclaimDao()

            if (com.example.data.FirebaseSyncManager.isFirebaseActive.value) {
                val (success, errorMsg) = com.example.data.FirebaseSyncManager.loginWithFirebase(email, password)
                if (!success) {
                    _authError.value = errorMsg ?: "Login failed."
                    return@launch
                }
                // Hydrate local DB from Firestore
                com.example.data.FirebaseSyncManager.downloadRemoteDataToLocal(dao)
            } else {
                // Local SQLite fallback mode
                val existingLocalUser = dao.getUserSync()
                val targetUser = existingLocalUser?.copy(
                    email = email,
                    name = existingLocalUser.name.ifBlank { email.substringBefore("@").replaceFirstChar { it.uppercase() } },
                    isLoggedIn = true
                ) ?: UserEntity(
                    id = 1,
                    email = email,
                    name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    isLoggedIn = true,
                    onboardingComplete = false
                )
                dao.insertUser(targetUser)
            }

            val updatedUser = dao.getUserSync() ?: UserEntity(id = 1)
            _currentRoute.value = if (updatedUser.onboardingComplete) "main_dashboard" else "onboarding_welcome"
        }
    }

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authError.value = null
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                _authError.value = "All fields are required."
                return@launch
            }
            if (password.length < 6) {
                _authError.value = "Password must be at least 6 characters."
                return@launch
            }

            val app = getApplication<ReclaimApp>()
            val dao = app.database.reclaimDao()

            if (com.example.data.FirebaseSyncManager.isFirebaseActive.value) {
                val (success, errorMsg) = com.example.data.FirebaseSyncManager.signUpWithFirebase(name, email, password)
                if (!success) {
                    _authError.value = errorMsg ?: "Sign up failed."
                    return@launch
                }
                
                val brandNewUser = UserEntity(
                    id = 1,
                    email = email,
                    name = name,
                    isLoggedIn = true,
                    onboardingComplete = false,
                    streakDays = 1,
                    focusScore = 15,
                    points = 100,
                    level = 1,
                    isPremium = false
                )
                dao.insertUser(brandNewUser)
                com.example.data.FirebaseSyncManager.syncUserToFirestore(brandNewUser)
            } else {
                // Local SQLite fallback
                val brandNewUser = UserEntity(
                    id = 1,
                    email = email,
                    name = name,
                    isLoggedIn = true,
                    onboardingComplete = false,
                    streakDays = 1,
                    focusScore = 15,
                    points = 100,
                    level = 1,
                    isPremium = false
                )
                dao.insertUser(brandNewUser)
            }
            
            _currentRoute.value = "onboarding_welcome"
        }
    }

    fun logOut() {
        viewModelScope.launch {
            com.example.data.FirebaseSyncManager.logout()
            
            val app = getApplication<ReclaimApp>()
            val dao = app.database.reclaimDao()
            val user = userState.value
            if (user != null) {
                dao.insertUser(user.copy(isLoggedIn = false))
            }
            stopTimer()
            _currentRoute.value = "auth_welcome"
        }
    }

    fun resetPassword(email: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (email.isBlank()) {
                callback(false)
                return@launch
            }

            if (com.example.data.FirebaseSyncManager.isFirebaseActive.value) {
                try {
                    val authInstance = com.example.data.FirebaseSyncManager.getFirebaseAuth()
                    if (authInstance != null) {
                        authInstance.sendPasswordResetEmail(email).await()
                        callback(true)
                    } else {
                        callback(false)
                    }
                } catch (e: Exception) {
                    callback(false)
                }
            } else {
                callback(true)
            }
        }
    }


    // --- ONBOARDING ACTIONS ---
    fun startOnboarding() {
        navigateTo("onboarding_goal")
    }

    fun saveOnboardingGoal(goal: String) {
        onboardingGoal.value = goal
        navigateTo("onboarding_phone")
    }

    fun saveOnboardingChecks(checks: Int) {
        onboardingChecks.value = checks
        navigateTo("onboarding_details")
    }

    fun saveOnboardingDetails(name: String, email: String) {
        onboardingName.value = name
        onboardingEmail.value = email
        navigateTo("onboarding_personalizing")
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            val existingUser = userState.value ?: UserEntity(id = 1)
            val updatedUser = existingUser.copy(
                name = onboardingName.value.ifBlank { existingUser.name.ifBlank { "Marcus Thorne" } },
                email = onboardingEmail.value.ifBlank { existingUser.email.ifBlank { "marcus.thorne@reclaim.io" } },
                phoneChecksDay = onboardingChecks.value,
                selectedFocusGoal = onboardingGoal.value,
                onboardingComplete = true,
                onboardingStep = 5,
                isLoggedIn = true
            )
            repository.saveUserProfile(updatedUser)
            navigateTo("main_dashboard")
        }
    }


    // --- HABITS ACTIONS ---
    fun addHabit(title: String, schedule: String, category: String) {
        viewModelScope.launch {
            repository.insertHabit(
                HabitEntity(title = title, schedule = schedule, category = category, completedToday = false)
            )
        }
    }

    fun toggleHabitCompleted(habit: HabitEntity) {
        viewModelScope.launch {
            val isCheckedNew = !habit.completedToday
            val updatedHabit = habit.copy(
                completedToday = isCheckedNew,
                streak = if (isCheckedNew) habit.streak + 1 else maxOf(0, habit.streak - 1)
            )
            repository.updateHabit(updatedHabit)

            // Adjust user score points
            val user = userState.value
            if (user != null) {
                val newPoints = if (isCheckedNew) user.points + 25 else maxOf(0, user.points - 25)
                repository.saveUserProfile(user.copy(points = newPoints))
            }
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }


    // --- EXERCISES & WORKOUTS (Physical Vitality) ---
    fun logWorkout(title: String, durationMinutes: Int, intensity: String) {
        viewModelScope.launch {
            repository.logWorkout(
                WorkoutEntity(title = title, durationMinutes = durationMinutes, intensity = intensity)
            )
            
            // Increment steps as standard physical tracker simulation
            val current = userState.value
            if (current != null) {
                val stepGain = durationMinutes * 125
                repository.saveUserProfile(
                    current.copy(currentSteps = current.currentSteps + stepGain)
                )
            }
        }
    }


    // --- WELLNESS / REFLECTIONS ---
    fun logMoodAndReflection(mood: String, reflection: String) {
        viewModelScope.launch {
            repository.addJournalEntry(
                JournalEntryEntity(mood = mood, reflection = reflection)
            )

            // Prompt users with 15 XP points per daily mindfulness reflection
            val user = userState.value
            if (user != null) {
                repository.saveUserProfile(user.copy(points = user.points + 15))
            }
        }
    }


    // --- FOCUS TIMER RUNTIME ---
    fun startTimer() {
        if (_timerState.value == "running") return
        _timerState.value = "running"
        timerJob = viewModelScope.launch {
            while (_timerSecondsRemaining.value > 0) {
                delay(1000)
                _timerSecondsRemaining.value--
            }
            onTimerFinished()
        }
    }

    fun pauseTimer() {
        _timerState.value = "paused"
        timerJob?.cancel()
    }

    fun stopTimer() {
        _timerState.value = "idle"
        timerJob?.cancel()
        _timerSecondsRemaining.value = 1500 // RESET to 25:00
    }

    fun setTimerDuration(mins: Int) {
        stopTimer()
        _timerSecondsRemaining.value = mins * 60
    }

    fun changeActiveTask(task: String) {
        _timerActiveTask.value = task
    }

    private fun onTimerFinished() {
        viewModelScope.launch {
            val totalSecondsOfTask = 1500 // matches start duration
            repository.logFocusSession(
                FocusSessionEntity(
                    taskTitle = _timerActiveTask.value,
                    durationSeconds = totalSecondsOfTask,
                    completed = true
                )
            )
            stopTimer()
        }
    }


    // --- AI COACH CONVERSATIONAL SERVICES ---
    fun sendUserMessage(txt: String) {
        if (txt.isBlank()) return
        viewModelScope.launch {
            // Log user text message
            repository.addChatMessage(sender = "USER", messageText = txt)
            _isAiLoading.value = true

            // Send to Gemini
            val aiResponse = repository.generateCoachResponse(txt)
            _isAiLoading.value = false

            // Log model reply
            repository.addChatMessage(sender = "AI", messageText = aiResponse)
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
            // Add initial greeting matching mockup
            repository.addChatMessage(
                sender = "AI",
                messageText = "Hi Alex. I noticed you've spent 40% more time on social media apps today compared to your daily average.\n\nTo help you regain your focus, why don't we try a 15-minute digital detox? I've blocked distracting notifications until 4:00 PM so you can finish your deep work session. You're doing great.",
                options = "Sounds good,Remind me later"
            )
        }
    }


    // --- INTERACTIVE GAMIFIED ELEMENTS (Redeem Rewards) ---
    fun redeemReward(cost: Int): Boolean {
        val user = userState.value ?: return false
        if (user.points >= cost) {
            viewModelScope.launch {
                repository.saveUserProfile(user.copy(points = user.points - cost))
            }
            return true
        }
        return false
    }

    fun simulateUnlockAppScrollOverride() {
        // Continue to Instagram triggers a screen-time subtraction or streak drop to gamify!
        viewModelScope.launch {
            val user = userState.value
            if (user != null) {
                // Drop 50 points as penalty for bypassing
                repository.saveUserProfile(
                    user.copy(
                        points = maxOf(0, user.points - 50),
                        focusScore = maxOf(30, user.focusScore - 5)
                    )
                )
            }
        }
    }

    fun updateProgression(points: Int?, streak: Int?) {
        viewModelScope.launch {
            val user = userState.value
            if (user != null) {
                repository.saveUserProfile(
                    user.copy(
                        points = points ?: user.points,
                        streakDays = streak ?: user.streakDays
                    )
                )
            }
        }
    }

    fun drinkWater() {
        viewModelScope.launch {
            val user = userState.value
            if (user != null) {
                repository.saveUserProfile(user.copy(waterCups = user.waterCups + 1, points = user.points + 5))
            }
        }
    }

    fun togglePremium() {
        viewModelScope.launch {
            val user = userState.value
            if (user != null) {
                repository.saveUserProfile(user.copy(isPremium = !user.isPremium))
            }
        }
    }
}

class ReclaimViewModelFactory(
    private val application: Application,
    private val repository: ReclaimRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReclaimViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReclaimViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
