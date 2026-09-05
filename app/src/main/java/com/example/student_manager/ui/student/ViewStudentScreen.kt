package com.example.student_manager.ui.student

//import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.student_manager.data.Student
import com.example.student_manager.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewStudentScreen(
    viewModel: StudentViewModel,
    onEditStudent: (Student) -> Unit,
//    icon: ImageVector,
    onBackClick: () -> Unit
) {
    val students by viewModel.studentList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.getStudents()
    }

    Scaffold { paddingValues ->

        var searchText by remember { mutableStateOf("") }

        val filteredStudents = students.filter {
            it.name.contains(searchText, ignoreCase = true) ||
                    it.email.contains(searchText, ignoreCase = true)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 14.dp,
                    end = 14.dp,
                    bottom = paddingValues.calculateBottomPadding()
                )
                .padding(12.dp)
        ) {
            Text(
                text = "View Students",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.paddingFromBaseline(top = 10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackClick,
                shape = RoundedCornerShape(50.dp),
//                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Back to Dashboard",
                    fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = {Text(
                    text = "Search students...",
                    fontWeight = FontWeight.Medium)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredStudents.isEmpty() && searchText.isNotBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No student found for \"$searchText\"",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredStudents) { student ->
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

//@Composable
//fun StudentItem(
//    student: Student,
//    onEdit: () -> Unit,
//    onDelete: () -> Unit
//) {
//    Card(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(text = "ID: ${student.id}")
//            Text(text = "Roll Number: ${student.rollNumber}")
//            Text(
//                text = "Name: ${student.name}",
//                style = MaterialTheme.typography.titleMedium
//            )
//            Text(text = "Email: ${student.email}")
//            Text(text = "Phone Number: ${student.phoneNumber}")
//            Text(text = "Course: ${student.course}")
//            Text(text = "Semester: ${student.semester}")
//            Text(text = "Division: ${student.division}")
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Button(onClick = onEdit) {
//                    Text("Edit")
//                }
//
//                Button(onClick = onDelete) {
//                    Text("Delete")
//                }
//            }
//        }
//    }
//}

@Composable
fun StudentItem(
    student: Student,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            1.dp,
            Color(0xFFCAF0F8)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
//        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "STUDENT",
                    style = MaterialTheme.typography.labelSmall
                )

                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            if (student.semester >= 7)
                                "ALUMNI"
                            else
                                "ACTIVE"
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = student.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Email")
            Text(student.email)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Course")
            Text(student.course)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Roll No: ${student.rollNumber}")
            Text("Semester: ${student.semester}")
            Text("Division: ${student.division}")

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = onEdit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0F4C81),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit")
                }

                OutlinedButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBA181B),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                    Text("Delete")
                }
            }
        }
    }
}