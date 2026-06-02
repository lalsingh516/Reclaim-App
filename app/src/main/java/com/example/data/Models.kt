package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val email: String = "",
    val name: String = "",
    val isLoggedIn: Boolean = false,
    val onboardingComplete: Boolean = false,
    val onboardingStep: Int = 1,
    val streakDays: Int = 12,
    val focusScore: Int = 84,
    val totalFocusHours: Float = 142f,
    val points: Int = 2850,
    val level: Int = 12,
    val isPremium: Boolean = false,
    val phoneChecksDay: Int = 45,
    val selectedFocusGoal: String = "Reduce Screen Time",
    val waterCups: Int = 4,
    val sleepHours: Float = 7.5f,
    val currentSteps: Int = 8432
)

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val schedule: String, // e.g. "Morning", "Night", "Evening", "All Day"
    val category: String, // e.g. "Focus session", "Avoidance", "10 mins"
    val completedToday: Boolean = false,
    val streak: Int = 12
)

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val mood: String = "Calm", // Focused, Calm, Stressed
    val reflection: String = ""
)

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taskTitle: String,
    val durationSeconds: Int,
    val completed: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val durationMinutes: Int,
    val intensity: String, // Low Intensity, Med Intensity, High Intensity
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String, // "AI" or "USER"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val options: String = "" // comma-separated options for quick replies
)
