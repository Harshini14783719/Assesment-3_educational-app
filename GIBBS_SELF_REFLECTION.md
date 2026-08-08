# Assessment 3 Self-Reflection: Gibbs' Reflective Cycle

**Course**: CP3406 Mobile Computing  
**Student Name**: Learner  
**Project**: BrainSpark STEM Education App  
**Word Count Target**: ~1,000 Words  

---

## 1. Description

For Assessment 3 of CP3406, I designed and developed **BrainSpark STEM**, a native Android educational application built using Kotlin and Jetpack Compose with Material Design 3. The application targets secondary and tertiary STEM learners, aiming to enhance scientific memory retention, active recall, and analytical problem-solving through interactive multi-choice quizzes and flashcards. 

The technical architecture adheres to Android clean architecture principles, incorporating MVVM (Model-View-ViewModel), the Repository pattern, Room persistent database storage, and RESTful API network integration via Retrofit. The app features four core screens: a Landing Page with daily facts and streak stats, an Activity Screen with live timer quizzes and active recall flashcards, a Settings Screen for user preferences, and a User Statistics Screen driven by persistent Room database entities.

This technical undertaking was directly informed by my research and presentation in Assessment 2, which investigated ethical mobile software development and the **Australian Computer Society (ACS) Code of Ethics**. Key ethical dimensions integrated into BrainSpark include data privacy and local-first storage, user autonomy without persuasive dark patterns, content safety, accessibility, and transparency regarding network and API usage.

---

## 2. Feelings

Throughout the lifecycle of this project, I experienced a mix of enthusiasm, heightened responsibility, and occasional technical anxiety. Initially, I felt excited to build an educational tool that could genuinely assist STEM students. However, reading the Assessment 2 ethical guidelines made me keenly aware of the subtle ways mobile applications can exploit user attention or mishandle sensitive data. I felt a strong sense of responsibility to ensure that BrainSpark did not resort to coercive gamification or invasive data harvesting.

During the technical implementation phase, particularly while connecting Retrofit to live external APIs and configuring Room database fallbacks, I felt challenged by state management in Jetpack Compose. When network calls occasionally failed or returned unformatted HTML entities, I felt temporary frustration. Overcoming these technical hurdles through reactive `StateFlow` streams and clean repository design brought immense satisfaction and renewed confidence in my software architecture skills.

---

## 3. Evaluation

Reflecting on the development process, several aspects succeeded while certain challenges required iterative problem solving:

### What Went Well:
1. **Ethical Privacy Integration**: By electing to store all user learning metrics locally within a Room database rather than transmitting user data to third-party telemetry servers, the app achieves absolute privacy by design.
2. **Robust Architectural Separation**: Utilizing MVVM alongside the Repository pattern ensured that the Compose UI remained decoupled from business logic and database operations.
3. **Offline Reliability & API Caching**: The repository seamlessly falls back to pre-populated Room database questions whenever network connectivity is unavailable, ensuring an uninterrupted learning experience.
4. **Declarative Compose UI**: Jetpack Compose enabled the rapid development of responsive, modern UI components with clean Material Design 3 aesthetics.

### What Was Challenging:
1. **Managing Asynchronous State & Timers**: Synchronizing the 20-second question countdown timer within `QuizViewModel` alongside user answer submissions required careful coroutine job cancellation to prevent memory leaks or duplicate state updates.
2. **Data Cleansing**: Parsing raw JSON responses from external trivia APIs required handling escaped HTML characters (e.g., `&quot;`, `&#039;`), which initially corrupted UI text until custom string decoding was added in the repository layer.

---

## 4. Analysis

The development of BrainSpark highlighted how technical decisions directly intersect with ethical responsibilities. Examining my choices through the lens of the **ACS Code of Ethics** provides key insights:

### Primacy of the Public Interest & Data Privacy
The ACS code mandates prioritizing the safety, privacy, and well-being of the public. Mobile educational applications often handle data from younger or vulnerable learners. Traditional mobile development frequently relies on third-party analytics SDKs that silently track user behavior. In BrainSpark, I deliberately avoided external tracking SDKs. All score histories, streaks, and preferences are stored exclusively on the user's device using Room DB and `SharedPreferences`. Furthermore, a explicit "Reset Database" option was implemented in the Settings screen, giving users complete ownership and autonomy over their data.

### Enhancement of Quality of Life & Non-Persuasive Design
Assessment 2 explored how "persuasive design" and aggressive notification loops can induce cognitive fatigue and digital addiction. In designing BrainSpark, I aimed to support intrinsic motivation rather than addictive engagement. While a streak counter and daily goal are included to foster discipline, the app avoids predatory mechanics such as artificial wait timers, paywalls, or intrusive push notifications. The user remains in full control of their learning pace.

### Competence & Platform Best Practices
Demonstrating professional competence required adhering to modern Android standards. Implementing Jetpack Compose, state-driven ViewModels, Kotlin Coroutines, and unit tests ensured code quality, maintainability, and stability. Writing unit tests for the repository and ViewModels verified that scoring algorithms and offline fallback mechanisms functioned accurately without relying on GUI execution.

---

## 5. Conclusion

Developing BrainSpark was a transformative learning experience that bridges theoretical ethics with practical mobile engineering. I learned that ethical development is not merely an afterthought or a policy document; it is an active engineering discipline that shapes database schema design, API integration, UI component hierarchy, and user permission flows. 

Mastering Jetpack Compose, Room database persistence, and clean MVVM architecture reinforced the value of modular design. Decoupling data retrieval from UI rendering not only resulted in cleaner code but also made the application resilient against network failures and easy to unit test. Overall, this project demonstrated that mobile applications can be highly engaging and aesthetically pleasing while upholding strict standards of user privacy, autonomy, and professional integrity.

---

## 6. Action Plan

To build upon the insights gained from this project, I have established the following action plan for future mobile development work:

1. **Proactive Accessibility Auditing**: In future projects, I will integrate automated accessibility testing tools (such as Accessibility Scanner and Compose UI Test framework) early in the design phase to verify tap target sizes, screen-reader TalkBack compatibility, and contrast ratios.
2. **Advanced Offline Synchronization**: I plan to expand the Room caching strategy by implementing a background sync worker (`WorkManager`) that fetches and caches fresh learning modules when the device is idle on Wi-Fi.
3. **Continuous Ethical Reviews**: For every new feature request in future software projects, I will perform an ethical impact assessment evaluating data collection, user consent, and potential dark patterns before writing implementation code.
4. **Expanded Unit & Integration Testing**: I will broaden test coverage by incorporating automated Compose UI tests (`androidx.compose.ui.test`) alongside existing model logic tests to verify screen navigation and user interaction flows.
