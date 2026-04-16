# Queue Manager 🕒

A high-fidelity, modern Android application for virtual queuing. Built with **Jetpack Compose** and **Firebase**, this app allows customers to join queues remotely and businesses to manage customer flow seamlessly.

## 📱 Visual Overview

````carousel
![Onboarding](file:///home/abdou/AndroidStudioProjects/QueueManger/screenshots/Onboarding.png)
<!-- slide -->
![Login](file:///home/abdou/AndroidStudioProjects/QueueManger/screenshots/Log%20In.png)
<!-- slide -->
![Sign Up](file:///home/abdou/AndroidStudioProjects/QueueManger/screenshots/Sign%20Up.png)
<!-- slide -->
![Profile](file:///home/abdou/AndroidStudioProjects/QueueManger/screenshots/Profile.png)
````

## ✨ Key Features

- **Dynamic Role Switching**: Choose between **Customer** and **Business Owner** during sign-up.
- **Modern Authentication**: Secure Login/Sign-up with real-time validation and password recovery.
- **Intelligent Onboarding**: Guided setup for businesses to define their services and capacity.
- **Teal-based Design System**: A premium UI experience using a custom "Teal & Slate" theme with smooth micro-animations.
- **Profile Management**: Update personal info, manage notification preferences, and change passwords.
- **Cross-Platform Sync**: Real-time queue updates powered by Firebase Firestore.

## 🏗 Project Architecture

The project follows the **Clean Architecture** principles to ensure scalability and maintainability.

### Folder Structure

```text
app/src/main/java/com/queuemanager/app/
├── data/                # Data storage and networking logic
│   └── repository/      # Implementations of domain repositories
├── di/                  # Dependency Injection modules (Hilt)
├── domain/              # Business logic (Pure Kotlin)
│   ├── model/           # App-wide data models
│   └── repository/      # Repository interfaces (The source of truth)
└── ui/                  # Presentation layer (Jetpack Compose)
    ├── auth/            # Login, Sign-up, Forgot Password, Welcome
    ├── customer/        # Customer-specific screens (Home, Reservations)
    ├── navigation/      # NavGraph and Bottom Navigation logic
    ├── onboarding/      # Business owner setup wizard
    ├── owner/           # Business-specific dashboards
    ├── profile/         # User profile and settings
    └── theme/           # Design System (Color, Typography, Components)
```

## 🛠 Tech Stack

- **UI**: Jetpack Compose
- **Language**: Kotlin
- **Dependency Injection**: Hilt (Dagger)
- **Database/Auth**: Firebase (Auth, Firestore)
- **Navigation**: Compose Navigation
- **Asynchronous**: Coroutines & Flow

## 🚀 Getting Started

1. **Clone the repository**:
   ```bash
   git clone git@github.com:LebgaaAbderrahmane/projet_mobile_S8.git
   ```
2. **Setup Firebase**:
   - Create a project on the [Firebase Console](https://console.firebase.google.com/).
   - Download the `google-services.json` file.
   - Place it in the `app/` directory.
3. **Build the project**:
   - Open with Android Studio (Koala or later recommended).
   - Ensure you are using **JDK 17**.
   - Sync Gradle and Run.

---
*Developed by Lebgaa Abderrahmane as part of the Mobile Project S8.*
