# Reclaim

<p align="center">
  <img src="assets/logo.png" alt="Reclaim Logo" width="120" />
</p>

<h3 align="center">
Transform Screen Time Into Real-Life Progress
</h3>

<p align="center">
A modern digital wellness platform that helps users overcome social media addiction by replacing unhealthy scrolling habits with fitness, productivity, mindfulness, and personal growth.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Flutter-Latest-blue" />
  <img src="https://img.shields.io/badge/Firebase-Backend-orange" />
  <img src="https://img.shields.io/badge/Gemini-AI-green" />
  <img src="https://img.shields.io/badge/License-MIT-black" />
</p>

---

## Overview

Reclaim is designed to help users reduce addiction to:

- Instagram Reels
- YouTube Shorts
- TikTok
- Facebook Reels
- Infinite Scrolling Apps

Instead of simply blocking access, Reclaim redirects attention toward meaningful activities such as fitness, focus sessions, habit building, mindfulness, and self-improvement.

---

## Key Features

### Digital Wellness

- Screen Time Tracking
- App Usage Analytics
- Phone Unlock Monitoring
- Daily Usage Reports
- Weekly Usage Reports
- Monthly Usage Reports
- Addiction Score
- Dopamine Reset Score
- Life Improvement Index

### Replace Reels Mode

When users attempt to open distracting applications, Reclaim can intervene with:

- Push-Up Challenges
- Squat Challenges
- Walking Missions
- Meditation Exercises
- Breathing Sessions
- Reading Challenges
- Brain Teasers
- Productivity Tasks

### Fitness System

- Home Workouts
- Walking Tracker
- Running Tracker
- Stretching Programs
- Exercise Logging
- Fitness Progress Analytics
- Workout Streaks

### Mental Wellness

- Guided Meditation
- Breathing Exercises
- Mood Tracking
- Gratitude Journal
- Mindfulness Sessions
- Mental Wellness Insights

### Habit Building

- Habit Creation
- Habit Tracking
- Habit Streaks
- Goal Management
- Daily Missions
- Progress Reports

### Productivity Tools

- Pomodoro Timer
- Deep Work Sessions
- Focus Mode
- Task Tracking
- Productivity Analytics

### AI Coach

Powered by Gemini AI:

- Personalized Habit Coaching
- Daily Wellness Plans
- Progress Analysis
- Behavioral Insights
- Motivation Support
- Challenge Recommendations

### Gamification

- XP System
- User Levels
- Coins
- Badges
- Achievements
- Rewards
- Streaks

### Community

- Accountability Partners
- Group Challenges
- Team Competitions
- Leaderboards
- Wellness Communities

### Health Tracking

- Water Intake Tracking
- Sleep Monitoring
- Step Counter
- Wellness Dashboard

---

## Technology Stack

### Mobile Application

```yaml
Framework: Flutter
Language: Dart
Architecture: Clean Architecture
State Management: Riverpod / Bloc
Navigation: GoRouter
```

### Backend

```yaml
Authentication: Firebase Auth
Database: Cloud Firestore
Storage: Firebase Storage
Functions: Cloud Functions
Notifications: Firebase Cloud Messaging
Analytics: Firebase Analytics
Crash Reporting: Firebase Crashlytics
```

### Artificial Intelligence

```yaml
Provider: Gemini API
Modules:
  - AI Coach
  - AI Insights
  - AI Challenges
  - AI Recommendations
```

### Payments

```yaml
Android: Google Play Billing
iOS: Apple In-App Purchases
```

### Admin Dashboard

```yaml
Framework: Next.js
Styling: Tailwind CSS
Database: Firebase
Authentication: Firebase Auth
```

---

## Architecture

```text
┌───────────────────────────┐
│       Flutter App         │
└─────────────┬─────────────┘
              │
              ▼
┌───────────────────────────┐
│ Firebase Authentication   │
└─────────────┬─────────────┘
              │
              ▼
┌───────────────────────────┐
│      Cloud Firestore      │
└─────────────┬─────────────┘
              │
 ┌────────────┼────────────┐
 ▼            ▼            ▼

Storage    Functions     AI Layer
                           │
                           ▼
                     Gemini API
```

---

## Project Structure

```text
reclaim/
│
├── mobile_app/
│   ├── lib/
│   │   ├── core/
│   │   ├── features/
│   │   ├── shared/
│   │   ├── services/
│   │   ├── routes/
│   │   └── main.dart
│   │
│   ├── assets/
│   └── pubspec.yaml
│
├── admin_panel/
│   ├── app/
│   ├── components/
│   ├── services/
│   └── package.json
│
├── cloud_functions/
│
├── firestore/
│   ├── rules
│   └── indexes
│
├── docs/
│
├── tests/
│
├── scripts/
│
├── firebase.json
├── README.md
└── LICENSE
```

---

## Security

```yaml
Authentication:
  - Email/Password
  - Google Sign-In
  - Apple Sign-In

Protection:
  - Firestore Security Rules
  - Secure Token Storage
  - Input Validation
  - Rate Limiting
  - Encrypted Communication
  - Secure Cloud Functions

Compliance:
  - GDPR Ready
  - Privacy First
```

---

## Roadmap

### Version 1.0

- Authentication
- Dashboard
- Habit Tracking
- Fitness Tracking
- Mental Wellness
- AI Coach
- Analytics

### Version 2.0

- Community Features
- Accountability Partners
- Team Challenges
- Advanced Insights

### Version 3.0

- Wearable Integrations
- Smart Interventions
- Advanced AI Wellness Engine
- Personalized Growth Plans

---

## Installation

```bash
git clone https://github.com/yourusername/reclaim.git

cd reclaim/mobile_app

flutter pub get

flutter run
```

---

## Environment Variables

```env
GEMINI_API_KEY=
FIREBASE_API_KEY=
FIREBASE_PROJECT_ID=
FIREBASE_STORAGE_BUCKET=
FCM_SENDER_ID=
```

---

## Contributing

```bash
# Fork Repository

# Create Branch
git checkout -b feature/new-feature

# Commit Changes
git commit -m "Add new feature"

# Push Changes
git push origin feature/new-feature

# Create Pull Request
```

---

## License

```text
MIT License

Copyright (c) 2026 Reclaim

Permission is hereby granted, free of charge,
to any person obtaining a copy of this software
and associated documentation files.
```

---

## Vision

> Reclaim exists to help people convert wasted screen time into meaningful progress through healthier habits, stronger focus, improved fitness, and better mental well-being.6. Run the app on an emulator or physical device
