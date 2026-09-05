package com.example.student_manager.ui.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.student_manager.data.Student
import com.example.student_manager.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStudentScreen(
    viewModel: StudentViewModel,
    student: Student,
    onBackClick: () -> Unit
) {
//    var id by remember { mutableStateOf(student.id) }
    var rollNumber by remember { mutableStateOf(student.rollNumber ?: "") }
    var name by remember { mutableStateOf(student.name) }
    var email by remember { mutableStateOf(student.email) }
    var phoneNumber by remember { mutableStateOf(student.phoneNumber ?: "") }
    var course by remember { mutableStateOf(student.course) }
    var semester by remember { mutableStateOf(student.semester.toString()) }
    var division by remember { mutableStateOf(student.division ?: "") }
    var localMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val message by viewModel.message.observeAsState("")

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            localMessage = message
            isLoading = false
        }
    }

//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Edit Student") }
//            )
//        }
//    )
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(paddingValues)
                .padding(
                    start = 14.dp,
                    end = 14.dp,
                    bottom = paddingValues.calculateBottomPadding()
                )
//                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Edit Students",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003B70),
                modifier = Modifier.paddingFromBaseline(top = 50.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ){
                Column( modifier = Modifier.padding(20.dp)
                ){
//                    OutlinedTextField(
//                value = id,
//                onValueChange = { id = it },
//                label = { Text("Student id") },
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                            value = rollNumber,
                            onValueChange = { rollNumber = it },
                            label = { Text("Student Roll Number") },
                            modifier = Modifier
                                .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Student Roll Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))


                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Student Email") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Student Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

//            OutlinedTextField(
//                value = course,
//                onValueChange = { course = it },
//                label = { Text("Course") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
                    val courses = listOf(
                        "Computer Engineering",
                        "Computer Science",
                        "Information Technology",
                        "Electrical Engineering"
                    )

                    var expanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {

                        OutlinedTextField(
                            value = course,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Course") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {

                            courses.forEach { item ->

                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        course = item
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

//            OutlinedTextField(
//                value = semester,
//                onValueChange = { semester = it },
//                label = { Text("Student's Current Semester") },
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(12.dp))
                    val semesters = listOf(
                        "1", "2", "3", "4",
                        "5", "6", "7", "8"
                    )

                    var semesterExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = semesterExpanded,
                        onExpandedChange = { semesterExpanded = !semesterExpanded }
                    ) {

                        OutlinedTextField(
                            value = semester,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Semester") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = semesterExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = semesterExpanded,
                            onDismissRequest = { semesterExpanded = false }
                        ) {

                            semesters.forEach { sem ->

                                DropdownMenuItem(
                                    text = {
                                        Text("Semester $sem")
                                    },
                                    onClick = {
                                        semester = sem
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = division,
                        onValueChange = { division = it },
                        label = { Text("Student Division") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
//

            Button(
                onClick = {
                    if (
                        rollNumber.isNotBlank() &&
                        name.isNotBlank() &&
                        email.isNotBlank() &&
                        phoneNumber.isNotBlank() &&
                        course.isNotBlank() &&
                        semester.isNotBlank() &&
                        division.isNotBlank()
                        ) {

                        student.id?.let { studentId ->

                            isLoading = true
                            localMessage = ""

                            viewModel.updateStudent(
                               studentId,
                                Student(
                                    id = studentId,
                                    rollNumber = rollNumber.trim(),
                                    name = name.trim(),
                                    email = email.trim(),
                                    phoneNumber = phoneNumber.trim(),
                                    course = course.trim(),
                                    semester = semester.toInt(),
                                    division = division.trim()
                                )
                            )

                        } ?: run {
                            localMessage = "Invalid Student ID"
                        }
                    } else {
                        localMessage = "Please fill all fields"
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 4.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF003B70)
                )
            ) {
                Text("Update Student")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("Back")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (localMessage.isNotEmpty()) {
                Text(
                    text = localMessage,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}