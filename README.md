# RideConnect Application

## Introduction

RideConnect is an Android mobile application that connects users with ride-hailing services. The app is designed to provide a seamless experience for both customers and drivers, with features such as real-time ride booking, trip tracking, online payments, and a rating system.

## Technologies Used

- **Frontend:** Android (Kotlin, Jetpack Compose)
- **Backend:** Spring Boot
- **Database:** 
  - PostgreSQL (Main server database)
  - Room Database (Local storage on Android)
- **Maps:** MapBox and GoongMap APIs
- **Architecture:** MVVM (Model-View-ViewModel) with Clean Architecture
- **Dependency Injection:** Dagger Hilt
- **Networking:** Retrofit, OkHttp, WebSocket
- **Asynchronous Programming:** Kotlin Coroutines
- **Image Loading:** Coil
- **Navigation:** Jetpack Navigation Component

## Key Features

### For Customers
- Account registration and login
- Location search with autocomplete functionality
- View nearby drivers on the map
- Book rides and track trips in real-time
- Save favorite addresses
- View trip history
- Online payment
- Rate drivers after trips
- Receive notifications about trip status
- Message drivers

### For Drivers
- Register as a driver
- Update status (online/offline)
- Receive ride requests
- Navigate to pickup and destination points
- View trip information
- Update trip status
- Receive payments
- View ratings and feedback

## Project Structure

The project follows Clean Architecture principles with the following main modules:

```
com.rideconnect/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── entity/
│   │   └── converter/
│   ├── remote/
│   │   ├── api/
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── interceptor/
│   │   └── websocket/
│   ├── mapper/
│   └── repository/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
├── presentation/
│   ├── components/
│   ├── navigation/
│   ├── screens/
│   │   ├── auth/
│   │   ├── customer/
│   │   ├── driver/
│   │   └── shared/
│   └── ui/theme/
├── di/
├── service/
└── util/
    ├── constants/
    ├── extensions/
    ├── location/
    ├── map/
    ├── payment/
    └── preferences/
```

## Installation

1. Clone the repository:
```bash
git clone https://github.com/phamngocpho/rideconnect-app.git
```

2. Clone the backend API repository:
```bash
git clone https://github.com/phamngocpho/rideconnect-api.git
```

3. Open the Android project in Android Studio

4. Create a `local.properties` file in the project root directory and configure your API keys:
   ```
   # API Base URL
   api.base.url=YOUR_API_BASE_URL
   
   # Goong Map API configuration
   goong.api.key=YOUR_GOONG_API_KEY
   goong.maptiles.key=YOUR_GOONG_MAPTILES_KEY
   goong.api.base.url=https://rsapi.goong.io/
   
   # Mapbox configuration
   MAPBOX_ACCESS_TOKEN=YOUR_MAPBOX_ACCESS_TOKEN
   MAPBOX_DOWNLOADS_TOKEN=YOUR_MAPBOX_DOWNLOADS_TOKEN
   ```

5. Build and run the application

## System Requirements

- Android Studio Giraffe (2023.1.1) or higher
- JDK 17
- Android SDK 35
- Gradle 8.8.2
- Device running Android 8.0 (API level 26) or higher

## Backend Requirements

- Java 17
- Spring Boot 3.2+
- PostgreSQL 14+
- Maven or Gradle

## Contributing

1. Fork the repository
2. Create a new feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

If you encounter any issues or have questions about this project, please create an issue in the repository.

## Project Links:
- Android App: [https://github.com/phamngocpho/rideconnect-app](https://github.com/phamngocpho/rideconnect-app)
- Backend API: [https://github.com/phamngocpho/rideconnect-api](https://github.com/phamngocpho/rideconnect-api)
