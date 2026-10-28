# 🎓 Student Manager App

A modern Android application designed to simplify student data management and attendance tracking. Built using **Kotlin** and **Jetpack Compose**, the app provides an intuitive interface for managing student information and viewing attendance reports.

## 📱 About the Project

Student Manager is an Android application developed to make student record management easier and more organized. It helps users access student information, monitor attendance, identify attendance defaulters, and generate reports.

The project uses an Android frontend and a Spring Boot backend to support communication between the application and its server.

## ✨ Features

- **Student Management** — Manage and view student records.
- **Attendance Tracking** — Access attendance-related information.
- **Attendance Reports** — View summary, monthly, and daily attendance reports.
- **Defaulter Identification** — Identify students whose attendance falls below a specified threshold.
- **PDF Export** — Export supported reports as PDF files.
- **Excel Export** — Export supported reports as `.xlsx` files.
- **Save to Device** — Save exported reports using Android's system file picker.
- **Modern UI** — Built with Jetpack Compose and Material 3.
- **Backend Integration** — Communicate with a REST API built using Spring Boot.

> Note: Features may vary depending on which modules have been completed and integrated into your current project version.

## 🛠️ Tech Stack

### Android Application
- Kotlin
- Jetpack Compose
- Material 3
- Android Studio
- Retrofit for REST API communication
- Kotlin Coroutines and StateFlow

### Backend
- Java
- Spring Boot
- REST APIs
- Database integration, depending on backend configuration

### Reporting
- Android `PdfDocument` for PDF generation
- XLSX generation using ZIP and XML
- Android Storage Access Framework (SAF) for saving files

## 🏗️ Project Architecture

The application follows a separation-of-concerns approach.

```text
StudentManager/
├── app/
│   └── src/main/
│       ├── java/com/example/student_manager/
│       │   ├── data/
│       │   ├── ui/
│       │   │   └── student/
│       │   ├── utils/
│       │   └── viewmodel/
│       └── AndroidManifest.xml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

The Spring Boot backend is maintained separately or in its configured backend directory.

## ⚙️ Requirements

- Android Studio
- Android SDK compatible with the project's configuration
- JDK compatible with the project's Gradle and Spring Boot versions
- A running Spring Boot backend for features that require server data
- An Android emulator or physical Android device

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR-USERNAME/YOUR-REPOSITORY.git
```

Replace the URL with your actual GitHub repository URL.

### 2. Open the Project

1. Open Android Studio.
2. Select **Open**.
3. Choose the cloned project folder.
4. Allow Gradle to sync.

### 3. Configure the Backend

Start the Spring Boot backend and confirm that its API endpoints are accessible.

For the Android emulator, `10.0.2.2` can be used to access services running on the development computer.

For example, the base URL may look like:

```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/api/"
```

Adjust the URL and port according to your backend configuration. For a physical device, use the development computer's reachable local IP address and ensure both devices can communicate.

### 4. Run the App

1. Select an emulator or connected Android device.
2. Click **Run** in Android Studio.
3. Ensure the backend is running for API-dependent features.

## 📊 Report Export

The reporting module is designed to support exporting report data into two formats:

| Format | File extension | Purpose |
|---|---|---|
| PDF | `.pdf` | Readable, printable reports |
| Excel | `.xlsx` | Tabular data for spreadsheet applications |

Supported report categories:

- Summary Report
- Monthly Attendance Report
- Daily Attendance Report
- Attendance Defaulters Report

The Android system save dialog lets the user choose where to save the generated file.

## 🔌 Backend Integration

The Android application communicates with the backend through REST API endpoints using Retrofit.

An example endpoint for retrieving attendance defaulters is:

```http
GET /api/reports/defaulters
```

The actual endpoints depend on the backend implementation.

## 🔒 Permissions and Security

- Report saving uses Android's Storage Access Framework.
- The system file picker allows users to select the destination for exported files.
- No broad storage permission is required solely for saving files through this workflow.
- Do not commit API credentials, database passwords, signing keys, or other secrets to GitHub.

## 🔮 Future Improvements

- Student profile editing and deletion
- Attendance visualization and analytics
- Search and filter student records
- Improved report formatting and customization
- Authentication and role-based access
- Cloud deployment of the backend
- Automated testing and error handling

## 🎯 Project Objective

The main objective of Student Manager is to provide a simple and organized way to manage student records and attendance information while making reports easier to access, export, and share.

## 👨‍💻 Author

**Arman Farid Mithagari**

Computer Engineering Student

## 📄 License

This project is available for educational and portfolio purposes. Add a `LICENSE` file if you intend to distribute it under a specific open-source license.
