# MAL-Sync Android 📱

A polished, native Android application for [MAL-Sync](https://malsync.moe/), featuring a premium "AnymeX" inspired UI and support for 5 distinct anime tracking services.

![AnymeX Style](https://raw.githubusercontent.com/RyanYuuki/AnymeX/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

## ✨ Features

- **Premium UI**: "Glassmorphism" design, deep dark mode, and fluid animations (Material 3 + Jetpack Compose).
- **Multi-Service Sync**:
  - ✅ **MyAnimeList** (Primary)
  - ✅ **AniList** (Full Support)
  - ✅ **Kitsu** (JSON:API Integration)
  - ✅ **Simkl** (Watch Progress)
  - ✅ **Shikimori** (User Rates)
- **Discovery**: Built-in search globally across services (powered by MAL).
- **Tracking**: "Browser Mode" to detect and update episodes automatically (Coming Soon).
- **Background Sync**: `WorkManager` powered auto-sync engine.

## 🛠️ Setup & Building

This project was built using a "No-Code" Agentic workflow. To run it:

1.  **Open in Android Studio** (Ladybug or newer recommended).
2.  **Configure API Keys**:
    - Open `android/ui/src/main/java/com/malsync/android/ui/screens/settings/SettingsViewModel.kt`.
    - Replace `MY_MAL_CLIENT_ID`, `MY_ANILIST_ID`, etc., with your actual Client IDs from the respective developer portals.
3.  **Build & Run**: Select `app` configuration and run on your emulator or device.

## 🏗️ Architecture

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **DI**: Hilt
- **Async**: Coroutines & Flow
- **Network**: Retrofit + OkHttp
- **Local DB**: Room

## 🤝 Contributing

This is a community project capable of syncing across the anime verse. Feel free to submit PRs!

**MAL-Sync Android** is the native mobile application for automatic anime & manga tracking.

It brings the powerful features of the original browser extension to your phone, offering a seamless, offline-first experience.

- **Native Experience**: Built entirely with Jetpack Compose for a smooth, modern UI.
- **In-App Browser**: Integrated browser with **Content Detection**. Browse supported sites (e.g., HiAnime, Crunchyroll) and the app will automatically detect what you're watching.
- **Auto-Sync**: Background synchronization with **MyAnimeList**, **AniList**, **Kitsu**, and **Simkl**.
- **Library Management**: View, filter, and update your list offline.
- **Premium Design**: "Midnight" dark theme optimized for mobile.

## 📱 Screenshots

_(Screenshots to be added)_

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose (Material3)
- **Architecture**: Clean Architecture (Multi-module)
- **DI**: Dagger Hilt
- **Local Data**: Room Database (Offline-first)
- **Network**: Retrofit + OkHttp
- **Async**: Coroutines + Flow + WorkManager

## 🚀 Building form Source

### Prerequisites

- JDK 17
- Android Studio (Koala or newer recommended)

### Build Instructions

1.  **Open the Project**:
    Open the `android/` directory in Android Studio.

2.  **Sync Gradle**:
    Allow Android Studio to download dependencies.

3.  **Build Command**:
    You can build the APK from the command line:
    ```bash
    cd android
    ./gradlew assembleDebug
    ```
    The output APK will be located at:  
    `android/app/build/outputs/apk/debug/app-debug.apk`

## 🔑 Authentication

The app uses OAuth 2.0 with Deep Links (`malsync://oauth`) to securely authenticate with tracking providers.

## 📄 License

[MIT](LICENSE)
