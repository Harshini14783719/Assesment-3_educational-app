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
- **Networking**: Retrofit 2 + Gson for live parsing of Open Science / Trivia REST API data.
- **Database**: Room Database with SQLite for storing quiz history, performance metrics, and pre-populated offline questions.
- **Async Operations**: Kotlin Coroutines + `StateFlow` reactive streams.
- **Navigation**: Type-safe `androidx.navigation.compose` navigation controller with bottom navigation bar.
- **Testing**: Non-GUI JUnit 4 unit tests verifying repository fallback, scoring algorithms, and ViewModel state transitions.


```

Test results and reports will be generated in `app/build/reports/tests/testDebugUnitTest/index.html`.

---

## Ethical & Professional Design Principles

Drawing upon research from Assessment 2 and the **ACS (Australian Computer Society) Code of Ethics**:

1. **Privacy by Design & Data Autonomy**: No unnecessary personally identifiable information (PII) is collected or transmitted. All learning statistics remain locally stored on the device using Room DB.
2. **Safe & Age-Appropriate Content**: Curated science and tech content suitable for learners without exposure to dark patterns, intrusive ads, or predatory microtransactions.
3. **User Autonomy**: Users can reset or purge their historical database data at any time via the Settings screen.
4. **Accessibility**: High-contrast Material 3 colors, clear font hierarchies, and optional haptic/audio feedback.

---

## Deliverables Included in Repository
- `app/`: Complete source code and test files exported from Android Studio.
- `GIBBS_SELF_REFLECTION.md`: 1000-word Self-Reflection essay based on Gibbs' Reflective Cycle.
- `README.md`: System documentation & architectural blueprint.
