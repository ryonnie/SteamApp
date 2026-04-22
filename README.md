# SteamApp

A mobile Steam-inspired application built for Android. This app allows users to browse a game store, manage their account, purchase games using a virtual wallet, and leave reviews. It also includes a comprehensive Admin panel for managing users and store listings.

## 🚀 Features

### For Users
- **User Authentication**: Secure Login and Registration system with a "Remember Me" feature.
- **Game Store**: Browse a variety of games with search functionality.
- **Game Details**: View detailed information about games, including pricing and user reviews.
- **Purchase System**: Buy games using a virtual Steam Wallet. Successful purchases generate unique CD keys.
- **Review System**: Rate games and write reviews to share your experience.
- **Purchase History**: Keep track of all your bought games and their CD keys.
- **Account Management**: Update your profile picture and view your current wallet balance.

### For Admins
- **User Management**: View all registered users, update their wallet balances, or delete accounts.
- **Store Management**: Manage game listings and apply discounts to specific titles.
- **Admin Dashboard**: A dedicated interface to oversee the entire platform's activity.

## 🛠️ Tech Stack
- **Language**: Java
- **UI Framework**: Android XML / Material Components
- **Database**: SQLite (via `DatabaseHelper`)
- **Architecture**: Fragments & Activity-based navigation
- **Components**: RecyclerView, BottomNavigationView, Activity Result API (for image picking)


## ⚙️ Installation & Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/SteamApp.git
   ```
2. Open the project in **Android Studio**.
3. Sync the project with Gradle files.
4. Run the app on an emulator or a physical Android device.

## 📁 Project Structure
- `AccountFragment.java`: Manages profile settings and wallet view.
- `StoreFragment.java`: The main hub for browsing and searching games.
- `HistoryFragment.java`: Displays the user's past purchases.
- `AdminFragment.java`: Entry point for administrative tasks.
- `DatabaseHelper.java`: Handles all CRUD operations for users, games, purchases, and reviews.

## 📝 License
This project is licensed under the MIT License.
=======

>>>>>>> 31a5fe7ccab87271e678ffd26fe2fcd164abeffa
