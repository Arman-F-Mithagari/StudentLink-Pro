package com.example.student_manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
//import com.example.student_manager.ui.components.AppScaffold
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.activity.compose.BackHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.student_manager.data.Student
import com.example.student_manager.ui.student.AddStudentScreen
import com.example.student_manager.ui.student.AppScaffold
import com.example.student_manager.ui.student.DashboardScreen
import com.example.student_manager.ui.student.EditStudentScreen
import com.example.student_manager.ui.student.LoginScreen
import com.example.student_manager.ui.student.ViewStudentScreen
import com.example.student_manager.ui.student.StudentDetailsScreen
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
//                var selectedStudent by remember { mutableStateOf<Student?>(null) }
                BackHandler (
                    enabled = currentScreen != "login" && currentScreen != "dashboard"
                ){
                    when(currentScreen){
                        "student_details" -> {
                            currentScreen = "view"
                        }
                        "edit" -> {
                            currentScreen = "view"
                        }
                        "add" -> {
                            currentScreen = "dashboard"
                        }
                        "view" -> {
                            currentScreen = "dashboard"
                        }
                    }
                }

                when (currentScreen) {
                    "login" -> {
                        LoginScreen(
                            onLoginSuccess = {
                                currentScreen = "dashboard"
                            }
                        )
                    }

                    "dashboard" -> {
                        AppScaffold(
                            currentScreen = currentScreen,
                            onNavigate = { currentScreen = it }
                        ) {
                            DashboardScreen(
                                viewModel = studentViewModel,
                                onNewStudentClick = {
                                    currentScreen = "add"
                                },
                                onViewStudentsClick = {
                                    studentViewModel.getStudents()
                                    currentScreen = "view"
                                }
                            )
                        }
                    }

                    "add" -> {
                        AppScaffold(
                            currentScreen = "add",
                            onNavigate = { screen ->
                                currentScreen = screen
                            }
                        ) {
                            AddStudentScreen(
                                viewModel = studentViewModel,
                                onBackClick = {
                                    currentScreen = "dashboard"
                                }
                            )
                        }
                    }

                    "view" -> {
                        AppScaffold(
                            currentScreen = "view",
                            onNavigate = {
                                screen -> currentScreen = screen
                            }
                        ) {
                            ViewStudentScreen(
                                viewModel = studentViewModel,
                                onViewDetails = { student ->
                                    selectedStudent = student
                                    currentScreen = "student_details"
                                },
                                onEditStudent = { student ->
                                    selectedStudent = student
                                    currentScreen = "edit"
                                },
                                onBackClick = {
                                    currentScreen = "dashboard"
                                }
                            )
                        }
                    }

                    "edit" -> {
                        AppScaffold(
                            currentScreen = "edit",
                            onNavigate = {
                                screen -> currentScreen = screen
                            }
                        ) {
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

                    "student_details" -> {
                        selectedStudent?.let { student ->
                            StudentDetailsScreen(
                                student = student,
                                onBackClick = {
                                    currentScreen = "view"
                                },
                                onEditClick = {
                                    selectedStudent = it
                                    currentScreen = "edit"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

