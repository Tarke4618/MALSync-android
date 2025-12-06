# MAL-Sync Android

**MAL-Sync Android** is the native mobile application for automatic anime & manga tracking.

It brings the powerful features of the original browser extension to your phone, offering a seamless, offline-first experience.

## ✨ Features

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
