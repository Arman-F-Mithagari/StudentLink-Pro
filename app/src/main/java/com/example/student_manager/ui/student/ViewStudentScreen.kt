package com.example.student_manager.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_manager.data.Student
import com.example.student_manager.ui.theme.Blue
import com.example.student_manager.ui.theme.BlueSoft
import com.example.student_manager.ui.theme.CardLine
import com.example.student_manager.ui.theme.Green
import com.example.student_manager.ui.theme.Line
import com.example.student_manager.ui.theme.Muted
import com.example.student_manager.ui.theme.AvatarGradients
import com.example.student_manager.ui.theme.Navy
import com.example.student_manager.ui.theme.Orange
import com.example.student_manager.ui.theme.PageBg
import com.example.student_manager.ui.theme.Red
import com.example.student_manager.ui.theme.SaveBrush
import com.example.student_manager.viewmodel.StudentViewModel

// Colors (PageBg, Navy, Muted, Blue, BlueSoft, Line, CardLine, Green, Red, Orange,
// SaveBrush, AvatarGradients) come from the shared palette.

// ---- Screen --------------------------------------------------------------

@Composable
fun ViewStudentScreen(
    viewModel: StudentViewModel,
    onViewDetails: (Student) -> Unit,
    onEditStudent: (Student) -> Unit,
    onBackClick: () -> Unit
) {
    val students by viewModel.studentList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.getStudents()
    }

    var searchText by remember { mutableStateOf("") }
    var studentToDelete by remember { mutableStateOf<Student?>(null) }

    val filteredStudents = students.filter {
        it.name.contains(searchText, ignoreCase = true) ||
                it.email.contains(searchText, ignoreCase = true) ||
                it.rollNumber.contains(searchText, ignoreCase = true)
    }

    // Delete confirmation
    studentToDelete?.let { student ->
        AlertDialog(
            onDismissRequest = { studentToDelete = null },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = "Delete student?",
                    color = Navy,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "${student.name} will be removed permanently.",
                    color = Muted
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        student.id?.let { viewModel.deleteStudent(it) }
                        studentToDelete = null
                    }
                ) {
                    Text("Delete", color = Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { studentToDelete = null }) {
                    Text("Cancel", color = Muted)
                }
            }
        )
    }

    Scaffold(containerColor = PageBg) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {

            // Soft background shapes
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .offset(x = (-90).dp, y = (-130).dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDCE6FF))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 70.dp, y = 220.dp)
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE6EEFF))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(6.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable(role = Role.Button, onClick = onBackClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Navy
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Students",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy
                        )
                        Text(
                            text = "${students.size} enrolled",
                            fontSize = 15.sp,
                            color = Muted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search by name, email or roll number", color = Muted) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Muted)
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Clear search",
                                    tint = Muted
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Blue,
                        unfocusedBorderColor = Line
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (filteredStudents.isEmpty()) {
                    EmptyStudents(
                        title = if (searchText.isNotBlank()) "No student found" else "No students yet",
                        message = if (searchText.isNotBlank())
                            "Nothing matches \"$searchText\". Try a different name or roll number."
                        else
                            "Add your first student from the dashboard."
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(filteredStudents) { student ->
                            StudentItem(
                                student = student,
                                onEdit = { onEditStudent(student) },
                                onDelete = { studentToDelete = student },
                                onViewDetails = { selected -> onViewDetails(selected) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---- Student card --------------------------------------------------------

@Composable
fun StudentItem(
    student: Student,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewDetails: (Student) -> Unit
) {
    val shape = RoundedCornerShape(22.dp)

    val (avatarStart, avatarEnd) =
        AvatarGradients[(student.name.hashCode() and Int.MAX_VALUE) % AvatarGradients.size]

    val initials = student.name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first() }
        .joinToString("")
        .uppercase()

    val isActive = student.semester < 7

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, shape)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, CardLine, shape)
            .clickable(role = Role.Button) { onViewDetails(student) }
            .padding(16.dp)
    ) {

        // Avatar + name + email
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(avatarStart, avatarEnd))),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    color = Navy,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = student.email,
                    color = Muted,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Course + status
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CourseChip(
                course = student.course,
                modifier = Modifier.weight(1f, fill = false)
            )
            StatusChip(active = isActive)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Details
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(PageBg)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            InfoItem("Roll no.", student.rollNumber, Modifier.weight(1f))
            InfoItem("Semester", student.semester.toString(), Modifier.weight(1f))
            InfoItem("Division", student.division, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Actions
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CardAction(
                label = "Edit",
                icon = Icons.Default.Edit,
                contentColor = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SaveBrush),
                onClick = onEdit
            )
            CardAction(
                label = "Delete",
                icon = Icons.Default.Delete,
                contentColor = Red,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Red.copy(alpha = 0.1f)),
                onClick = onDelete
            )
        }
    }
}

// ---- Pieces --------------------------------------------------------------

@Composable
private fun CourseChip(course: String, modifier: Modifier = Modifier) {
    val icon =
        if (course.contains("comput", ignoreCase = true)) Icons.Default.Laptop
        else Icons.Default.School

    Surface(
        shape = RoundedCornerShape(50),
        color = BlueSoft,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Blue,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = course,
                color = Blue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StatusChip(active: Boolean) {
    val tone = if (active) Green else Orange

    Surface(
        shape = RoundedCornerShape(50),
        color = tone.copy(alpha = 0.12f)
    ) {
        Text(
            text = if (active) "Active" else "Alumni",
            color = tone,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
        )
    }
}

@Composable
private fun InfoItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = Muted,
            fontSize = 12.sp
        )
        Text(
            text = value,
            color = Navy,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CardAction(
    label: String,
    icon: ImageVector,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.clickable(role = Role.Button, onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = contentColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun EmptyStudents(title: String, message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = Navy,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = message,
            color = Muted,
            fontSize = 14.sp
        )
    }
}