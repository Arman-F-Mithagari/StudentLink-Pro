package com.example.student_manager.ui.student

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_manager.data.Student
import com.example.student_manager.ui.theme.AvatarGradients
import com.example.student_manager.ui.theme.Blue
import com.example.student_manager.ui.theme.CardLine
import com.example.student_manager.ui.theme.Green
import com.example.student_manager.ui.theme.Line
import com.example.student_manager.ui.theme.Muted
import com.example.student_manager.ui.theme.Navy
import com.example.student_manager.ui.theme.PageBg
import com.example.student_manager.ui.theme.Purple
import com.example.student_manager.ui.theme.Red
import com.example.student_manager.ui.theme.SaveBrush
import com.example.student_manager.viewmodel.StudentViewModel

private val Courses = listOf(
    "Computer Engineering",
    "Computer Science",
    "Information Technology",
    "Electrical Engineering"
)

private val Semesters = listOf("1", "2", "3", "4", "5", "6", "7", "8")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStudentScreen(
    viewModel: StudentViewModel,
    student: Student,
    onBackClick: () -> Unit
) {
    var rollNumber by remember { mutableStateOf(student.rollNumber ?: "") }
    var name by remember { mutableStateOf(student.name) }
    var email by remember { mutableStateOf(student.email) }
    var phoneNumber by remember { mutableStateOf(student.phoneNumber ?: "") }
    var course by remember { mutableStateOf(student.course) }
    var semester by remember { mutableStateOf(student.semester.toString()) }
    var division by remember { mutableStateOf(student.division ?: "") }
    var localMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }

    val message by viewModel.message.observeAsState("")

    LaunchedEffect(message) {
        if (message.isNotEmpty() && isLoading) {
            localMessage = message
            isLoading = false
        }
    }

    val rollValid = rollNumber.isNotBlank()
    val nameValid = name.isNotBlank()
    val emailValid = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val phoneValid = phoneNumber.isNotBlank()
    val courseValid = course.isNotBlank()
    val semesterValid = semester.toIntOrNull() != null
    val divisionValid = division.isNotBlank()

    val allValid = rollValid && nameValid && emailValid && phoneValid &&
            courseValid && semesterValid && divisionValid

    val (avatarStart, avatarEnd) =
        AvatarGradients[(student.name.hashCode() and Int.MAX_VALUE) % AvatarGradients.size]

    val initials = name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first() }
        .joinToString("")
        .uppercase()
        .ifEmpty { "?" }

    Scaffold(containerColor = PageBg) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {

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
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

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
                                text = "Edit Student",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy
                            )
                            Text(
                                text = "Update the student's details",
                                fontSize = 15.sp,
                                color = Muted
                            )
                        }
                    }

                    val profileShape = RoundedCornerShape(22.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(3.dp, profileShape)
                            .clip(profileShape)
                            .background(Color.White)
                            .border(1.dp, CardLine, profileShape)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(avatarStart, avatarEnd))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initials,
                                color = Color.White,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = name.ifBlank { "Unnamed student" },
                                color = Navy,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Roll no. ${rollNumber.ifBlank { "-" }}",
                                color = Muted,
                                fontSize = 14.sp
                            )
                        }
                    }

                    FormSectionCard(
                        title = "Personal details",
                        icon = Icons.Default.Person,
                        accent = Blue
                    ) {
                        FormField(
                            value = rollNumber,
                            onValueChange = { rollNumber = it },
                            label = "Roll number",
                            icon = Icons.Default.Badge,
                            isError = submitted && !rollValid,
                            errorText = "Required"
                        )
                        FormField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Full name",
                            icon = Icons.Default.Person,
                            isError = submitted && !nameValid,
                            errorText = "Required"
                        )
                        FormField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            icon = Icons.Default.Email,
                            keyboardType = KeyboardType.Email,
                            isError = submitted && !emailValid,
                            errorText = "Enter a valid email"
                        )
                        FormField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = "Phone number",
                            icon = Icons.Default.Phone,
                            keyboardType = KeyboardType.Phone,
                            isError = submitted && !phoneValid,
                            errorText = "Required"
                        )
                    }

                    FormSectionCard(
                        title = "Academic details",
                        icon = Icons.Default.School,
                        accent = Purple
                    ) {
                        DropdownField(
                            label = "Course",
                            icon = Icons.Default.School,
                            options = Courses,
                            selected = course,
                            onSelect = { course = it },
                            isError = submitted && !courseValid,
                            errorText = "Select a course"
                        )
                        DropdownField(
                            label = "Semester",
                            icon = Icons.Default.CalendarMonth,
                            options = Semesters,
                            selected = semester,
                            display = { "Semester $it" },
                            onSelect = { semester = it },
                            isError = submitted && !semesterValid,
                            errorText = "Select a semester"
                        )
                        FormField(
                            value = division,
                            onValueChange = { division = it },
                            label = "Division",
                            icon = Icons.Default.Groups,
                            isError = submitted && !divisionValid,
                            errorText = "Required"
                        )
                    }

                    if (localMessage.isNotEmpty()) {
                        val success = localMessage.contains("successfully", ignoreCase = true)
                        val tone = if (success) Green else Red

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(tone.copy(alpha = 0.1f))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = localMessage,
                                color = tone,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    SubmitButton(loading = isLoading) {
                        submitted = true

                        if (isLoading) return@SubmitButton

                        if (!allValid) {
                            localMessage = "Please fix the highlighted fields"
                            return@SubmitButton
                        }

                        val studentId = student.id
                        if (studentId == null) {
                            localMessage = "Invalid Student ID"
                            return@SubmitButton
                        }

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
                    }
                }
            }
        }
    }
}

@Composable
private fun FormSectionCard(
    title: String,
    icon: ImageVector,
    accent: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(22.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, shape)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, CardLine, shape)
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accent.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = Navy,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            content = content
        )
    }
}

@Composable
private fun formFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    errorContainerColor = Color.White,
    focusedBorderColor = Blue,
    unfocusedBorderColor = Line,
    errorBorderColor = Red,
    focusedLabelColor = Blue,
    unfocusedLabelColor = Muted,
    focusedLeadingIconColor = Blue,
    unfocusedLeadingIconColor = Muted
)

@Composable
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        singleLine = true,
        isError = isError,
        supportingText = if (isError && errorText != null) {
            { Text(errorText) }
        } else null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(16.dp),
        colors = formFieldColors()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    icon: ImageVector,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    display: (String) -> String = { it },
    isError: Boolean = false,
    errorText: String? = null
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = if (selected.isEmpty()) "" else display(selected),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = { Icon(icon, contentDescription = null) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            isError = isError,
            supportingText = if (isError && errorText != null) {
                { Text(errorText) }
            } else null,
            shape = RoundedCornerShape(16.dp),
            colors = formFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(display(option)) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SubmitButton(
    loading: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .alpha(if (loading) 0.75f else 1f)
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = Blue.copy(alpha = 0.4f),
                spotColor = Blue.copy(alpha = 0.4f)
            )
            .clip(shape)
            .background(SaveBrush)
            .clickable(enabled = !loading, role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(38.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(Color.White.copy(alpha = 0.92f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = null,
                tint = Blue,
                modifier = Modifier.size(22.dp)
            )
        }

        Text(
            text = if (loading) "Updating..." else "Update student",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        if (loading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.5.dp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(26.dp)
            )
        }
    }
}