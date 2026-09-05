package com.example.student_manager.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.student_manager.data.Student
import com.example.student_manager.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentScreen(
    viewModel: StudentViewModel,
    onBackClick: () -> Unit
) {
//    var id by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var division by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val message by viewModel.addMessage.observeAsState("")

//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Add New Student") }
//            )
//        }
//    )
    Scaffold { paddingValues ->

        LaunchedEffect(message) {
            if (message.contains("successfully", ignoreCase = true)) {
//                id = ""
                rollNumber = ""
                name = ""
                email = ""
                phoneNumber = ""
                course = ""
                semester = ""
                division = ""
                isLoading = false
            } else if (message.isNotEmpty()) {
                isLoading = false
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(paddingValues)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = paddingValues.calculateBottomPadding()
                )
//                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Add New Student",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003B70),
                modifier = Modifier.paddingFromBaseline(top = 50.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ){
                Column(
                    modifier = Modifier.padding(20.dp)
                ){
//                    OutlinedTextField(
//                        value = id,
//                        onValueChange = { id = it },
//                        label = { Text("Student id") },
//                        modifier = Modifier.fillMaxWidth()
//                    )

                    Spacer(modifier = Modifier.height(12.dp))

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
                        label = { Text("Student Name") },
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


//                    OutlinedTextField(
//                        value = semester,
//                        onValueChange = { semester = it },
//                        label = { Text("Student's Current Semester") },
//                        modifier = Modifier.fillMaxWidth()
//                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = division,
                        onValueChange = { division = it },
                        label = { Text("Student Division") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }


            Button(
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank() && course.isNotBlank()) {
                        isLoading = true

                        val student = Student(
                            id = null,
                            rollNumber = rollNumber.trim(),
                            name = name.trim(),
                            email = email.trim(),
                            phoneNumber = phoneNumber.trim(),
                            course = course.trim(),
                            semester = semester.toInt(),
                            division = division.trim()
                        )

                        viewModel.addStudent(student)
                    } else {
                        viewModel.message.value = "Please fill all fields"
                    }
                },
//                modifier = Modifier.fillMaxWidth()
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF003B70)
                )
            ) {
                Text("Save Student")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("Back to Dashboard")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}