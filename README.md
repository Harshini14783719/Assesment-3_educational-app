# CP3406 Assessment 3: Education App - BrainSpark STEM

## Project Overview
**BrainSpark STEM** is an interactive, ethically designed mobile educational application developed for secondary and tertiary STEM (Science, Technology, Engineering, & Mathematics) learners. Designed as part of the **CP3406 Mobile Computing** course at **James Cook University Australia**, BrainSpark combines real-time trivia API content with persistent offline Room database caching, active recall flashcards, and interactive STEM quizzes to support cognitive memory retention and analytical problem solving.

---

## Core Screens & Features

| Screen | Description & Technical Implementation |
| :--- | :--- |
| **Landing Page** | Entry point featuring daily STEM facts, streak tracker, average score gauge, quick quiz launch shortcuts, and modern Material 3 gradient cards. |
| **Activity Screen** | Multi-mode learning hub containing: <br>• **STEM Quiz Mode**: Animated countdown timer, live score calculation, API integration, instant feedback, and answer explanations.<br>• **Active Recall Flashcards**: Interactive flip-cards for scientific definition memorization. |
| **Settings Screen** | Allows users to toggle sound effects, haptic vibration, select question difficulty (Easy/Medium/Hard), manage sound, haptic feedback, difficulty, theme preferences, and database records. |
| **User Statistics Screen** | Comprehensive learning analytics dashboard displaying historical attempt records stored in Room DB, accuracy percentages, high scores, and category breakdowns. |

---

## Architecture & Technical Stack

The app follows **Android Clean Architecture Principles** using **MVVM (Model-View-ViewModel)**, **Repository Pattern**, and **Jetpack Compose Material Design 3**.

```
com.jcu.educationapp/
├── data/
│   ├── local/              # Room Database (AppDatabase, DAOs, Entities)
│   ├── remote/             # Retrofit API Service & DTOs
│   └── repository/         # Repository implementation with offline fallback
├── ui/
│   ├── navigation/         # Jetpack Compose Navigation Graph & Routes
│   ├── screens/            # 4 Core Jetpack Compose Screens
│   ├── components/         # Reusable M3 Composable widgets
│   └── theme/              # Material 3 Color Palette, Typography & Theme
├── viewmodel/              # StateFlow ViewModels & ViewModelFactory
└── util/                   # SoundManager & PreferencesManager
```

- **UI Framework**: Jetpack Compose with Material Design 3 (`androidx.compose.material3`).
- **Networking**: Retrofit 2 + Gson for retrieving live quiz questions from Open Trivia DB and daily facts from the UselessFacts API.
- **Database**: Room Database with SQLite for storing quiz history, performance metrics, and pre-populated offline questions.
- **Async Operations**: Kotlin Coroutines + `StateFlow` reactive streams.
- **Navigation**: Type-safe `androidx.navigation.compose` navigation controller with bottom navigation bar.
- **Testing**: Non-GUI JUnit 4 unit tests verifying repository fallback, scoring algorithms, and ViewModel state transitions.


```

Test results and reports will be generated in `app/build/reports/tests/testDebugUnitTest/index.html`.

---

## Ethical & Professional Design Principles

Drawing upon research from Assessment 2 and the **ACS (Australian Computer Society) Code of Ethics**:

1. **Privacy by Design & Data Autonomy**:The app does not require accounts or collect names, emails, passwords, or other personally identifiable information. Quiz statistics are stored locally using Room, and no user behaviour telemetry is collected.
2. **Safe & Age-Appropriate Content**: The app is designed for educational STEM revision and avoids intrusive advertising, predatory monetisation, and pressure-based engagement patterns.
3. **User Autonomy**: Users can reset or purge their historical database data at any time via the Settings screen.
4. **Accessibility**: High-contrast Material 3 colors, clear font hierarchies, and optional haptic/audio feedback.
5. **User Wellbeing**: Notifications were deliberately not implemented because frequent reminders could distract or pressure students and encourage unnecessary engagement.

---

## Deliverables Included in Repository
- `app/`: Complete source code and test files exported from Android Studio.
- `README.md`: System documentation & architectural blueprint.
