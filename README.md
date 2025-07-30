# Server Manager - Android App

A production-grade Android application for managing virtual server instances with offline-first architecture, Firebase synchronization, and automated billing system.

## 📱 Features

### Core Functionality
- **Server Provisioning**: Create virtual servers with customizable types and regions
- **State Management**: Complete FSM lifecycle (PENDING → RUNNING → STOPPED → TERMINATED)
- **Offline-First**: Full functionality without internet connectivity
- **Real-time Dashboard**: Live server count and billing overview
- **Background Processing**: Automated server booting and billing calculations
- **Cloud Sync**: Manual synchronization with Firebase Firestore
- **Push Notifications**: Server state change alerts

### User Interface
- **Modern UI**: Material 3 design with Jetpack Compose
- **Intuitive Navigation**: Single-activity architecture with smooth transitions
- **Responsive Design**: Optimized for various screen sizes
- **Dark/Light Theme**: System-aware theme switching

## 🏗️ Architecture

### Tech Stack
- **Language**: Kotlin 100%
- **UI Framework**: Jetpack Compose
- **Architecture Pattern**: MVVM + Clean Architecture
- **Navigation**: Jetpack Navigation Component
- **Database**: Room (SQLite) - Offline-first
- **Remote Storage**: Firebase Firestore
- **Authentication**: Firebase Auth (Email/Password)
- **Background Jobs**: WorkManager
- **Dependency Injection**: Hilt
- **Reactive Programming**: Kotlin Coroutines + Flow

### Architecture Layers
```
┌─────────────────────────────────────────────────────────────┐
│                    UI Layer (Compose)                       │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐          │
│  │ Dashboard   │ │ Server List │ │ Provision   │          │
│  │ Screen      │ │ Screen      │ │ Screen      │          │
│  └─────────────┘ └─────────────┘ └─────────────┘          │
│           │              │              │                  │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐          │
│  │ Dashboard   │ │ ServerList  │ │ Provision   │          │
│  │ ViewModel   │ │ ViewModel   │ │ ViewModel   │          │
│  └─────────────┘ └─────────────┘ └─────────────┘          │
└─────────────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────────┐
│                   Domain Layer                              │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐          │
│  │ Provision   │ │ Update      │ │ Calculate   │          │
│  │ Server      │ │ Server      │ │ Billing     │          │
│  │ UseCase     │ │ State       │ │ UseCase     │          │
│  │             │ │ UseCase     │ │             │          │
│  └─────────────┘ └─────────────┘ └─────────────┘          │
│                           │                                │
│  ┌─────────────────────────────────────────────────────┐  │
│  │          Server Repository Interface                │  │
│  └─────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────────┐
│                    Data Layer                               │
│  ┌─────────────┐          │          ┌─────────────┐       │
│  │    Room     │          │          │  Firebase   │       │
│  │  Database   │◄─────────┼─────────►│  Firestore  │       │
│  │ (Local DB)  │          │          │ (Cloud DB)  │       │
│  └─────────────┘          │          └─────────────┘       │
│                           │                                │
│  ┌─────────────────────────────────────────────────────┐  │
│  │        Server Repository Implementation             │  │
│  └─────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Project Structure
```
app/
├── src/main/kotlin/com/example/servermanager/
│   ├── ServerManagerApplication.kt          # Application class
│   ├── MainActivity.kt                      # Single activity
│   │
│   ├── data/                               # Data Layer
│   │   ├── local/                          # Room database
│   │   │   ├── ServerDatabase.kt           # Database configuration
│   │   │   ├── ServerDao.kt                # Database operations
│   │   │   └── entities/ServerEntity.kt    # Database entities
│   │   ├── remote/                         # Firebase integration
│   │   │   ├── FirestoreService.kt         # Cloud operations
│   │   │   └── dto/ServerDto.kt            # Data transfer objects
│   │   └── repository/                     # Repository implementation
│   │       └── ServerRepositoryImpl.kt     # Data source coordination
│   │
│   ├── domain/                             # Domain Layer
│   │   ├── model/                          # Business models
│   │   │   ├── Server.kt                   # Core server model
│   │   │   ├── ServerState.kt              # FSM states
│   │   │   └── ServerType.kt               # Server configurations
│   │   ├── repository/                     # Repository interfaces
│   │   │   └── ServerRepository.kt         # Abstract repository
│   │   └── usecase/                        # Business logic
│   │       ├── ProvisionServerUseCase.kt   # Server creation
│   │       ├── UpdateServerStateUseCase.kt # State transitions
│   │       ├── GetServersUseCase.kt        # Data retrieval
│   │       ├── SyncServersUseCase.kt       # Cloud synchronization
│   │       └── CalculateBillingUseCase.kt  # Billing calculations
│   │
│   ├── ui/                                 # UI Layer
│   │   ├── navigation/                     # Navigation setup
│   │   │   └── ServerNavigation.kt         # Navigation graph
│   │   ├── screens/                        # Compose screens
│   │   │   ├── dashboard/                  # Dashboard feature
│   │   │   ├── serverlist/                 # Server list feature
│   │   │   ├── serverdetail/               # Server details feature
│   │   │   ├── provision/                  # Server provisioning
│   │   │   └── auth/                       # Authentication
│   │   ├── components/                     # Reusable UI components
│   │   │   ├── ServerCard.kt               # Server display card
│   │   │   └── LoadingButton.kt            # Loading state button
│   │   └── theme/                          # Material theming
│   │       ├── Color.kt                    # Color palette
│   │       ├── Theme.kt                    # Theme configuration
│   │       └── Typography.kt               # Text styles
│   │
│   ├── workers/                            # Background Jobs
│   │   ├── BootWorker.kt                   # Server boot simulation
│   │   ├── BillingWorker.kt                # Billing calculations
│   │   └── SyncWorker.kt                   # Cloud synchronization
│   │
│   ├── di/                                 # Dependency Injection
│   │   ├── DatabaseModule.kt               # Database dependencies
│   │   ├── NetworkModule.kt                # Network dependencies
│   │   ├── RepositoryModule.kt             # Repository dependencies
│   │   └── UseCaseModule.kt                # Use case dependencies
│   │
│   └── utils/                              # Utilities
│       ├── Constants.kt                    # App constants
│       ├── Extensions.kt                   # Kotlin extensions
│       └── NotificationHelper.kt           # Notification management
│
└── src/test/                               # Testing
    ├── domain/usecase/                     # Unit tests
    ├── workers/                            # Worker tests
    └── ui/                                 # UI tests
```

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: 8 or higher
- **Android SDK**: API level 24+ (Android 7.0)
- **Firebase Project**: With Authentication and Firestore enabled
- **Device/Emulator**: Android 7.0+ for testing

### Installation Steps

#### 1. Clone Repository
```bash
git clone https://github.com/yourusername/server-manager-android.git
cd server-manager-android
```

#### 2. Firebase Setup
1. **Create Firebase Project**:
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create new project or use existing one
   - Enable **Authentication** (Email/Password provider)
   - Enable **Cloud Firestore** database

2. **Add Android App**:
   - Register app with package name: `com.example.servermanager`
   - Download `google-services.json`
   - Place file in `app/` directory

3. **Configure Firestore Security Rules**:
   ```javascript
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /users/{userId}/servers/{document=**} {
         allow read, write: if request.auth != null && request.auth.uid == userId;
       }
     }
   }
   ```

#### 3. Build Project
```bash
# Clean and build
./gradlew clean build

# Install debug APK
./gradlew installDebug

# Run tests
./gradlew test
```

#### 4. Run Application
1. Connect Android device or start emulator
2. Run from Android Studio or use:
   ```bash
   ./gradlew installDebug
   ```

## 📊 Core Features Deep Dive

### Server Finite State Machine (FSM)
```
PENDING ──BootWorker──▶ RUNNING ──Stop──▶ STOPPED ──Start──▶ RUNNING
   │                       │                 │                  │
   └──────Terminate────────┼─────Terminate───┼──────────────────┘
                           ▼                 ▼
                      TERMINATED ◄──────────┘
```

**State Descriptions**:
- **PENDING**: Server is being provisioned and booting up
- **RUNNING**: Server is active and incurring billing charges
- **STOPPED**: Server is halted but can be restarted
- **TERMINATED**: Server is permanently destroyed

**Atomic Transitions**:
- All state changes use `@Transaction` annotations
- Mutex protection prevents race conditions
- Invalid transitions are rejected with error messages

### Background Workers

#### BootWorker
- **Purpose**: Simulates server boot process
- **Trigger**: Automatically when server is provisioned
- **Duration**: 5-15 seconds random delay
- **Result**: Transitions server from PENDING → RUNNING

#### BillingWorker
- **Purpose**: Calculates billing for running servers
- **Schedule**: Every 15 minutes (configurable)
- **Logic**: `billing = runtime_seconds × hourly_rate`
- **Scope**: Only servers in RUNNING state

#### SyncWorker
- **Purpose**: Synchronizes local data with Firebase
- **Trigger**: Manual from dashboard UI
- **Strategy**: Last-Write-Wins (LWW) conflict resolution
- **Direction**: One-way (Room → Firestore)

### Offline-First Architecture
- **Local Database**: Room SQLite database
- **Source of Truth**: All operations work on local data first
- **Sync Strategy**: Manual synchronization when network available
- **Conflict Resolution**: Timestamp-based LWW strategy

## 🧪 Testing

### Test Coverage
- **Unit Tests**: Domain logic and use cases
- **Integration Tests**: Repository and database operations  
- **Worker Tests**: Background job execution
- **UI Tests**: End-to-end user flows

### Running Tests
```bash
# Unit tests
./gradlew testDebugUnitTest

# Instrumented tests (requires device/emulator)
./gradlew connectedDebugAndroidTest

# Test coverage report
./gradlew testDebugUnitTestCoverage
```

### Key Test Cases
1. **FSM Transitions**: Valid/invalid state changes
2. **Billing Calculations**: Accurate cost computation
3. **Offline Functionality**: Operations without network
4. **Worker Execution**: Background job reliability
5. **UI Interactions**: Provision and management flows

## 🔧 Configuration

### Environment Variables
Create `local.properties` file in project root:
```properties
# Android SDK location
sdk.dir=/path/to/android/sdk

# Firebase configuration (optional override)
firebase.project.id=your-project-id
```

### Build Variants
```kotlin
android {
    buildTypes {
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
}
```

### Customization Options
- **Server Types**: Modify `ServerType` enum for different configurations
- **Billing Rates**: Adjust hourly rates in server type definitions
- **Worker Intervals**: Change billing frequency in `ServerManagerApplication`
- **Regions**: Update region list in provisioning screen
- **Notification Styles**: Customize in `NotificationHelper`

## 📱 Usage Guide

### Getting Started
1. **Launch App**: Open Server Manager from device
2. **Sign In**: Use email/password authentication
3. **Dashboard**: View server overview and billing summary

### Provisioning Servers
1. **Tap FAB**: Floating action button on dashboard
2. **Select Type**: Choose server configuration (Micro, Small, Medium, Large)
3. **Choose Region**: Pick deployment region
4. **Optional Name**: Add custom server name
5. **Provision**: Submit and wait for boot process

### Managing Servers
1. **View List**: Access from dashboard "View All Servers"
2. **Server Details**: Tap any server for detailed view
3. **State Control**: Use action buttons to start/stop/terminate
4. **Monitor Billing**: Track costs in real-time

### Synchronization
1. **Manual Sync**: Pull down on dashboard or tap sync icon
2. **Status Feedback**: Toast/snackbar confirms sync result
3. **Conflict Resolution**: Latest changes win automatically

## 🔐 Security Considerations

### Data Protection
- **Local Encryption**: Room database uses SQLCipher (optional)
- **Network Security**: HTTPS only for Firebase communication
- **Authentication**: Secure Firebase Auth with email verification
- **Access Control**: Firestore rules restrict user data access

### Privacy
- **Data Minimization**: Only necessary data collected
- **User Consent**: Clear permission requests
- **Data Retention**: Configurable cleanup policies
- **Audit Logging**: Track access and modifications

## 🐛 Troubleshooting

### Common Issues

#### Firebase Connection Problems
```
Error: FirebaseApp with name [DEFAULT] doesn't exist
```
**Solution**: Ensure `google-services.json` is in `app/` directory

#### Worker Not Executing
```
BootWorker stuck in ENQUEUED state
```
**Solution**: Check WorkManager constraints and battery optimization settings

#### Sync Failures
```
Permission denied on Firestore operation
```
**Solution**: Verify security rules and user authentication status

#### Build Errors
```
Duplicate class found in modules
```
**Solution**: Clean project and check dependency conflicts

### Debug Tools
- **Logcat Filtering**: Use tag `ServerManager` for app logs
- **Database Inspector**: View Room database in Android Studio
- **WorkManager Inspector**: Monitor background jobs
- **Firebase Console**: Check Firestore data and auth users

### Performance Monitoring
- **Memory Usage**: Monitor with Android Studio Profiler
- **Battery Impact**: Check WorkManager job frequency
- **Network Usage**: Optimize sync operations
- **App Size**: Use APK Analyzer for size optimization

## 📚 Additional Resources

### Documentation
- [Architecture Guide](docs/architecture.md)
- [API Reference](docs/api.md)
- [Testing Guide](docs/testing.md)
- [Deployment Guide](docs/deployment.md)

### External Links
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Firebase Android Setup](https://firebase.google.com/docs/android/setup)
- [WorkManager Guide](https://developer.android.com/topic/libraries/architecture/workmanager)

## 🤝 Contributing

### Development Workflow
1. **Fork Repository**: Create personal fork on GitHub
2. **Feature Branch**: Create branch for new features
3. **Code Standards**: Follow Android Kotlin style guide
4. **Testing**: Add tests for new functionality
5. **Pull Request**: Submit PR with detailed description

### Code Style
- **Kotlin Coding Conventions**: Follow official guidelines
- **Architecture Patterns**: Maintain MVVM + Clean Architecture
- **Documentation**: Add KDoc for public APIs
- **Testing**: Aim for 80%+ code coverage

### Issue Reporting
- **Bug Reports**: Use GitHub issue templates
- **Feature Requests**: Provide detailed use cases
- **Security Issues**: Report privately via email

## 📄 License

```
MIT License

Copyright (c) 2024 Server Manager

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 👥 Team

**Maintainers**:
- Primary Developer: [@yourusername](https://github.com/yourusername)
- Architecture Lead: [@architect](https://github.com/architect)
- UI/UX Designer: [@designer](https://github.com/designer)

**Contributors**:
- View [all contributors](https://github.com/yourusername/server-manager-android/contributors)

## 📈 Roadmap

### Version 2.0 (Planned)
- [ ] Real-time WebSocket updates
- [ ] Multi-user collaboration
- [ ] Enhanced billing reports
- [ ] Server performance monitoring
- [ ] Automated backup scheduling

### Version 1.1 (Current)
- [x] Basic server management
- [x] Offline-first architecture  
- [x] Background billing calculations
- [x] Firebase synchronization
- [x] Material 3 UI design

## 📞 Support

### Getting Help
- **Documentation**: Check this README and `/docs` folder
- **GitHub Issues**: Search existing issues or create new one
- **Stack Overflow**: Tag questions with `android-servermanager`
- **Discord Community**: Join our [Discord server](https://discord.gg/servermanager)

### Commercial Support
For enterprise support and custom development:
- **Email**: support@servermanager.com
- **Website**: [www.servermanager.com](https://www.servermanager.com)

---

**Built with ❤️ using Android Jetpack Compose and Firebase**

*Last updated: January 2024*
