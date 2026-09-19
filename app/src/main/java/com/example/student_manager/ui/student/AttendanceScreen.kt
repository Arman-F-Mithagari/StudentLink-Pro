package com.example.student_manager.ui.student

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.navigation.NavController
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.student_manager.data.Attendance
import com.example.student_manager.data.Student
import com.example.student_manager.ui.theme.AbsentBrush
import com.example.student_manager.ui.theme.Blue
import com.example.student_manager.ui.theme.BlueSoft
import com.example.student_manager.ui.theme.Coral
import com.example.student_manager.ui.theme.Green
import com.example.student_manager.ui.theme.Mint
import com.example.student_manager.ui.theme.Muted
import com.example.student_manager.ui.theme.Navy
import com.example.student_manager.ui.theme.PageBg
import com.example.student_manager.ui.theme.PresentBrush
import com.example.student_manager.ui.theme.Red
import com.example.student_manager.ui.theme.SaveBrush
import com.example.student_manager.ui.theme.Line
import com.example.student_manager.ui.theme.AvatarGradients
import com.example.student_manager.ui.theme.ToggleIdle
import com.example.student_manager.viewmodel.AttendanceViewModel
import com.example.student_manager.viewmodel.StudentViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AttendanceScreen(
    onNavigate: (String) -> Unit,
    studentViewModel: StudentViewModel = viewModel(),
    attendanceViewModel: AttendanceViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val students by studentViewModel.studentList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        studentViewModel.getStudents()
    }

    var searchText by remember { mutableStateOf("") }

    val attendanceMap = remember { mutableStateMapOf<Long, Boolean>() }

    LaunchedEffect(students) {
        students.forEach { student ->
            student.id?.let { attendanceMap.putIfAbsent(it, true) }
        }
    }

    val displayDate = remember {
        SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault()).format(Date())
    }

    val isPresent: (Student) -> Boolean = { student ->
        student.id?.let { attendanceMap[it] } ?: true
    }

    val filteredStudents = students.filter { student ->
        student.name.contains(searchText, ignoreCase = true) ||
                student.rollNumber.contains(searchText, ignoreCase = true)
    }

    val presentCount = students.count(isPresent)
    val absentCount = students.size - presentCount

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
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
                .offset(x = 70.dp, y = 150.dp)
                .size(160.dp)
                .clip(CircleShape)
                .background(Color(0xFFE6EEFF))
        )

        Column(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFDDE8FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EventAvailable,
                                contentDescription = null,
                                tint = Blue,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Attendance",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy
                            )
                            Text(
                                text = "Mark who is in class today",
                                fontSize = 15.sp,
                                color = Muted
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .shadow(6.dp, CircleShape)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable(role = Role.Button, onClick = onMenuClick),
                            contentAlignment = Alignment.Center
                        ) {
                            OutlinedButton(
                                onClick = {
                                    onNavigate("attendance_history")
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("History")
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "History",
                                tint = Navy
                            )
                        }
                    }
                }

                item {
                    SummaryCard(
                        date = displayDate,
                        total = students.size,
                        present = presentCount,
                        absent = absentCount
                    )
                }

                item {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search by name or roll number", color = Muted) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Muted)
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
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Students (${filteredStudents.size})",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        BulkAction(
                            label = "Mark all present",
                            icon = Icons.Default.Check,
                            color = Green
                        ) {
                            filteredStudents.forEach { s -> s.id?.let { attendanceMap[it] = true } }
                        }
                        BulkAction(
                            label = "Mark all absent",
                            icon = Icons.Default.Close,
                            color = Red
                        ) {
                            filteredStudents.forEach { s -> s.id?.let { attendanceMap[it] = false } }
                        }
                    }
                }

                if (filteredStudents.isEmpty()) {
                    item { EmptyState() }
                } else {
                    items(filteredStudents) { student ->
                        AttendanceStudentCard(
                            student = student,
                            isPresent = isPresent(student),
                            onAttendanceChange = { present ->
                                student.id?.let { attendanceMap[it] = present }
                            }
                        )
                    }
                }
            }

            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                SaveButton(enabled = students.isNotEmpty()) {
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(Date())

                    var saved = 0

                    students.forEach { student ->

                        val id = student.id ?: return@forEach

                        val attendance = Attendance(
                            studentId = id,
                            attendanceDate = date,
                            status = if (isPresent(student)) "Present" else "Absent"
                        )

                        Log.d("ATTENDANCE", "Saving: $attendance")
                        attendanceViewModel.saveAttendance(attendance)
                        saved++
                    }

                    Toast.makeText(
                        context,
                        "Saving attendance for $saved students",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}


@Composable
private fun SummaryCard(
    date: String,
    total: Int,
    present: Int,
    absent: Int
) {
    val fraction = if (total == 0) 0f else present.toFloat() / total
    val percent = (fraction * 100).toInt()
    val shape = RoundedCornerShape(26.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = Blue.copy(alpha = 0.3f),
                spotColor = Blue.copy(alpha = 0.3f)
            )
            .clip(shape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF10298F),
                        Color(0xFF1D4ED8),
                        Color(0xFF2F6DF6)
                    ),
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            )
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = 70.dp)
                .size(200.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.07f))
        )

        Column(modifier = Modifier.padding(20.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = date,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                SummaryStat("Total", total, Color.White, Modifier.weight(1f))
                StatDivider()
                SummaryStat("Present", present, Mint, Modifier.weight(1f).padding(start = 16.dp))
                StatDivider()
                SummaryStat("Absent", absent, Coral, Modifier.weight(1f).padding(start = 16.dp))
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF34D399), Color(0xFF4ADE80))
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "$percent% present",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun SummaryStat(
    label: String,
    value: Int,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = value.toString(),
            color = valueColor,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 15.sp
        )
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(Color.White.copy(alpha = 0.2f))
    )
}


@Composable
private fun BulkAction(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}


@Composable
fun AttendanceStudentCard(
    student: Student,
    isPresent: Boolean,
    onAttendanceChange: (Boolean) -> Unit
) {
    val stripe by animateColorAsState(
        targetValue = if (isPresent) Green else Red,
        label = "stripe"
    )

    val (avatarStart, avatarEnd) =
        AvatarGradients[(student.name.hashCode() and Int.MAX_VALUE) % AvatarGradients.size]

    val initials = student.name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first() }
        .joinToString("")
        .uppercase()

    val courseIcon =
        if (student.course.contains("comput", ignoreCase = true)) Icons.Default.Laptop
        else Icons.Default.School

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE6ECFA))
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {

            // Status stripe
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(stripe)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(14.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(avatarStart, avatarEnd))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = Color.White,
                            fontSize = 22.sp,
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
                            text = "Roll no. ${student.rollNumber}",
                            color = Muted,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = BlueSoft,
                        modifier = Modifier.widthIn(max = 160.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = courseIcon,
                                contentDescription = null,
                                tint = Blue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = student.course,
                                color = Blue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ToggleButton(
                        label = "Present",
                        icon = Icons.Default.Check,
                        selected = isPresent,
                        selectedBrush = PresentBrush,
                        modifier = Modifier.weight(1f),
                        onClick = { onAttendanceChange(true) }
                    )
                    ToggleButton(
                        label = "Absent",
                        icon = Icons.Default.Close,
                        selected = !isPresent,
                        selectedBrush = AbsentBrush,
                        modifier = Modifier.weight(1f),
                        onClick = { onAttendanceChange(false) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ToggleButton(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    selectedBrush: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val contentColor = if (selected) Color.White else Navy
    val base = modifier
        .height(46.dp)
        .clip(RoundedCornerShape(14.dp))

    Row(
        modifier = (if (selected) base.background(selectedBrush) else base.background(ToggleIdle))
            .clickable(role = Role.Button, onClick = onClick),
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
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SaveButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .alpha(if (enabled) 1f else 0.5f)
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = Blue.copy(alpha = 0.4f),
                spotColor = Blue.copy(alpha = 0.4f)
            )
            .clip(shape)
            .background(SaveBrush)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
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
            text = "Save attendance",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

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

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No students found",
            color = Navy,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Check the spelling or search by roll number instead.",
            color = Muted,
            fontSize = 14.sp
        )
    }
}