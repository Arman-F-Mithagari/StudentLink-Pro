package com.example.student_manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.student_manager.data.Student
import com.example.student_manager.ui.student.AddStudentScreen
import com.example.student_manager.ui.student.DashboardScreen
import com.example.student_manager.ui.student.EditStudentScreen
import com.example.student_manager.ui.student.LoginScreen
import com.example.student_manager.ui.student.ViewStudentScreen
import com.example.student_manager.ui.theme.Student_ManagerTheme
import com.example.student_manager.viewmodel.StudentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
//            Box {
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            }
            Student_ManagerTheme {
                val studentViewModel: StudentViewModel = viewModel()
                var currentScreen by remember { mutableStateOf("login") }
                var selectedStudent by remember { mutableStateOf<Student?>(null) }

                when (currentScreen) {
                    "login" -> {
                        LoginScreen(
                            onLoginSuccess = {
                                currentScreen = "dashboard"
                            }
                        )
                    }

                    "dashboard" -> {
                        DashboardScreen(
                            onNewStudentClick = {
                                currentScreen = "add"
                            },
                            onViewStudentsClick = {
                                studentViewModel.getStudents()
                                currentScreen = "view"
                            }
                        )
                    }

                    "add" -> {
                        AddStudentScreen(
                            viewModel = studentViewModel,
                            onBackClick = {
                                currentScreen = "dashboard"
                            }
                        )
                    }

                    "view" -> {
                        ViewStudentScreen(
                            viewModel = studentViewModel,
                            onEditStudent = { student ->
                                selectedStudent = student
                                currentScreen = "edit"
                            },
                            onBackClick = {
                                currentScreen = "dashboard"
                            }
                        )
                    }

                    "edit" -> {
                        selectedStudent?.let { student ->
                            EditStudentScreen(
                                viewModel = studentViewModel,
                                student = student,
                                onBackClick = {
                                    studentViewModel.getStudents()
                                    currentScreen = "view"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

