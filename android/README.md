# MAL-Sync Android

The official native Android application for MAL-Sync.

**MAL-Sync Android** brings the powerful automatic episode tracking and library management features of the browser extension to your mobile device. Built with modern Android technologies, it offers a seamless, offline-first experience for tracking your anime and manga progress.

## ✨ Features

- **Native Experience**: Built entirely with Jetpack Compose for a smooth, modern UI.
- **In-App Browser**: integrated browser with content detection. Automatically detects when you are watching anime on supported sites (HiAnime, Crunchyroll, etc.) and offers to sync your progress.
- **Auto-Sync**: Background synchronization with MyAnimeList, AniList, Kitsu, and Simkl.
- **Library Management**: View, filter, and update your anime list offline. Changes are synced when you're back online.
- **Cross-Service Support**: Sync seamlessly between multiple services.
- **Premium Design**: "Midnight" dark theme optimized for viewing comfort.

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose (Material3)
- **Architecture**: Clean Architecture (MVVM/MVI) with Multi-module structure:
  - `:app` - Application entry point and DI rooting.
  - `:ui` - Composable screens and ViewModels.
  - `:domain` - Use cases, repository interfaces, and pure business logic.
  - `:data` - Repository implementations, API services (Retrofit), and Local DB (Room).
  - `:core` - Common utilities and base classes.
- **Dependency Injection**: Hilt
- **Async/Background**: Coroutines, Flow, WorkManager
- **Network**: Retrofit, OkHttp
- **Local Storage**: Room Database
- **Image Loading**: Coil

## 🚀 Getting Started

### Prerequisites

- Android Studio Koala or newer.
- JDK 17.

### Building the Project

1.  Clone the repository:
    ```bash
    git clone https://github.com/MALSync/MALSync.git
    cd MALSync/android
    ```
2.  Open the `android` directory in Android Studio.
3.  Sync Gradle files.
4.  Run the `app` configuration on an Emulator or Physical Device.

## 🔑 Authentication

The app uses OAuth 2.0 to authenticate with tracking services.

- **Redirect Scheme**: `malsync://oauth`
- The app intercepts this scheme to handle secure token exchange.

## 🤝 Contributing

Contributions are welcome! Please ensure you follow the clean architecture principles established in the codebase.

- **UI changes** go in the `:ui` module.
- **Business logic** goes in `:domain` or `:data`.

## 📦 Deployment (Cloud Build)

This repository includes a **GitHub Actions** workflow to automatically build and release the APK.

### How to Create a Release

1.  **Commit your changes**.
2.  **Create a Tag** matching `v*` (e.g., `v1.0.0`):
    ```bash
    git tag v1.0.0
    git push origin v1.0.0
    ```
3.  **Wait**: GitHub Actions will automatically:

    - Build the APK.
    - Create a new Release on GitHub.
    - Upload `app-debug.apk` to the assets.

4.  **Download**: Go to the **Releases** section of your repository to find the APK.

## 📄 License

Same as the parent project - [MIT](../LICENSE).
