# SteamApp - Game Store Android Application

A feature-rich Android application that simulates a Steam-like digital game store platform. Users can authenticate, browse games, make purchases, write reviews, and manage their game library.

## 📱 Features

- **User Authentication**
  - User registration and login system
  - Secure account management
  - Session persistence

- **Game Store**
  - Browse available games
  - Search and filter functionality
  - Game details and descriptions
  - Purchase games directly from the app

- **User Account Management**
  - View account details
  - Manage purchased games
  - Update profile information

- **Purchase History**
  - View all past purchases
  - Track transaction history
  - Receipt management

- **Review System**
  - Write and read game reviews
  - Rate games
  - Community feedback

- **Admin Panel**
  - Manage games (add/edit/delete)
  - Manage users
  - Monitor store activity
  - View platform statistics

## 🛠 Tech Stack

- **Language**: Java
- **Platform**: Android (API 29+)
- **Target SDK**: Android 14 (API 36)
- **UI Framework**: AndroidX Material Design
- **Database**: SQLite (via DatabaseHelper)
- **Build System**: Gradle with Kotlin DSL

## 📋 Project Structure

```
SteamApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml       # App manifest configuration
│   │   │   ├── java/com/example/steamapp/
│   │   │   │   ├── Activities/
│   │   │   │   │   ├── LoginActivity.java
│   │   │   │   │   ├── RegisterActivity.java
│   │   │   │   │   └── MainActivity.java
│   │   │   │   ├── Fragments/
│   │   │   │   │   ├── StoreFragment.java
│   │   │   │   │   ├── AccountFragment.java
│   │   │   │   │   ├── HistoryFragment.java
│   │   │   │   │   └── AdminFragment.java
│   │   │   │   ├── Models/
│   │   │   │   │   ├── Game.java
│   │   │   │   │   ├── Purchase.java
│   │   │   │   │   └── Review.java
│   │   │   │   ├── Adapters/
│   │   │   │   │   ├── GameAdapter.java
│   │   │   │   │   ├── PurchaseAdapter.java
│   │   │   │   │   ├── ReviewAdapter.java
│   │   │   │   │   ├── AdminGameAdapter.java
│   │   │   │   │   └── AdminUserAdapter.java
│   │   │   │   └── DatabaseHelper.java   # SQLite database operations
│   │   │   └── res/                      # Resources (layouts, strings, etc.)
│   │   ├── test/                         # Unit tests
│   │   └── androidTest/                  # Android instrumentation tests
│   └── build.gradle.kts                  # App-level build configuration
├── build.gradle.kts                      # Project-level build configuration
├── settings.gradle.kts                   # Settings and module configuration
└── gradle/                               # Gradle wrapper and configuration
```

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest version recommended)
- JDK 11 or higher
- Android SDK 36 (API level 36)
- Minimum Android version 10 (API 29)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/SteamApp.git
   cd SteamApp
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the SteamApp directory
   - Wait for Gradle sync to complete

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run on emulator or device**
   - Connect an Android device or start an emulator
   - Click "Run" or press `Shift + F10` in Android Studio
   - The app will launch with the LoginActivity

## 📲 Usage

### User Registration & Login
1. Launch the app
2. New users can tap "Register" to create an account
3. Existing users can login with their credentials
4. Upon successful login, navigate to MainActivity

### Browsing the Store
- **Store Fragment**: Browse available games with descriptions and prices
- **Game Details**: Tap a game to view more information
- **Purchase**: Add games to your library

### Managing Your Account
- **Account Fragment**: View profile, purchased games, and account settings
- **History Fragment**: Check your purchase history and receipts

### Writing Reviews
- Go to a game's detail page
- Write a review and rate the game (1-5 stars)
- Submit to share with the community

### Admin Features
- Access admin panel (for admin accounts)
- Manage games: add new titles, edit details, remove games
- Monitor user activity
- View platform analytics

## 🔧 Configuration

### Gradle Properties
Key dependencies are configured in `gradle/libs.versions.toml`:
- AndroidX AppCompat
- Material Design components
- ConstraintLayout for UI
- JUnit for testing
- Espresso for UI testing

### Build Variants
- **Debug**: Development build with debugging enabled
- **Release**: Optimized production build with ProGuard obfuscation

## 🗄 Database

The app uses SQLite for local data persistence via `DatabaseHelper`:
- User account information
- Game catalog and metadata
- Purchase records
- User reviews and ratings

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Android Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

## 📦 Building for Release

```bash
./gradlew assembleRelease
```

The signed APK will be available in `app/build/outputs/apk/release/`

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

**Your Name/Username**
- GitHub: [@yourusername](https://github.com/yourusername)

## 📞 Support

For support, email your.email@example.com or open an issue on GitHub.

## 🎮 Future Enhancements

- [ ] Cloud sync for user accounts
- [ ] Wishlist feature
- [ ] Game recommendations
- [ ] Multiplayer achievements
- [ ] In-app messaging between users
- [ ] Push notifications for sales
- [ ] Dark mode support
- [ ] Localization for multiple languages

---

**Happy coding! 🚀**
