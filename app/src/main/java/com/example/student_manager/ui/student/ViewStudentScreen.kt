package com.example.student_manager.ui.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.student_manager.data.Student
import com.example.student_manager.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewStudentScreen(
    viewModel: StudentViewModel,
    onEditStudent: (Student) -> Unit,
    onBackClick: () -> Unit
) {
    val students by viewModel.studentList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.getStudents()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("View Students") }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
        ) {
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Dashboard")
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (students.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "No students found",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(students) { student ->
                        StudentItem(
                            student = student,
                            onEdit = { onEditStudent(student) },
                            onDelete = {
                                student.id?.let { id ->
                                    viewModel.deleteStudent(id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudentItem(
    student: Student,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "ID: ${student.id}")
            Text(text = "Roll Number: ${student.rollNumber}")
            Text(
                text = "Name: ${student.name}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Email: ${student.email}")
            Text(text = "Phone Number: ${student.phoneNumber}")
            Text(text = "Course: ${student.course}")
            Text(text = "Semester: ${student.semester}")
            Text(text = "Division: ${student.division}")

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onEdit) {
                    Text("Edit")
                }

                Button(onClick = onDelete) {
                    Text("Delete")
                }
            }
        }
    }
}