package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.ReclaimRepository

class ReclaimApp : Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "reclaim_database"
        )
        .fallbackToDestructiveMigration() // Facilitates rapid iterative model updates securely
        .build()
    }

    val repository: ReclaimRepository by lazy {
        ReclaimRepository(database.reclaimDao())
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize programmatic Firebase setup
        com.example.data.FirebaseSyncManager.initialize(applicationContext)
    }
}
