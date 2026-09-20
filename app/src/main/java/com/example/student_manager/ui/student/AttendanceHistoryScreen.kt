package com.example.student_manager.ui.student

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.student_manager.data.Attendance
import com.example.student_manager.ui.theme.AvatarGradients
import com.example.student_manager.ui.theme.AbsentBrush
import com.example.student_manager.ui.theme.Blue
import com.example.student_manager.ui.theme.BlueSoft
import com.example.student_manager.ui.theme.CardLine
import com.example.student_manager.ui.theme.Coral
import com.example.student_manager.ui.theme.Green
import com.example.student_manager.ui.theme.Line
import com.example.student_manager.ui.theme.Mint
import com.example.student_manager.ui.theme.Muted
import com.example.student_manager.ui.theme.Navy
import com.example.student_manager.ui.theme.Orange
import com.example.student_manager.ui.theme.PageBg
import com.example.student_manager.ui.theme.PresentBrush
import com.example.student_manager.ui.theme.Red
import com.example.student_manager.ui.theme.SaveBrush
import com.example.student_manager.ui.theme.ToggleIdle
import com.example.student_manager.viewmodel.AttendanceViewModel
import com.example.student_manager.viewmodel.StudentViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceHistoryScreen(
    attendanceViewModel: AttendanceViewModel = viewModel(),
    onNavigate: (String) -> Unit,
    studentViewModel: StudentViewModel = viewModel()
) {
    val context = LocalContext.current

    val attendanceList by attendanceViewModel.attendanceList.collectAsState()
    val students by studentViewModel.studentList.observeAsState(emptyList())

    var searchText by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val edits = remember { mutableStateMapOf<Long, Boolean>() }

    LaunchedEffect(Unit) {
        attendanceViewModel.getAllAttendance()
        studentViewModel.getStudents()
    }

    val studentsById = remember(students) {
        students.mapNotNull { s -> s.id?.let { it to s } }.toMap()
    }

    val isPresent: (Attendance) -> Boolean = { a ->
        a.id?.let { edits[it] } ?: (a.status == "Present")
    }

    val filteredAttendance = attendanceList.filter { a ->
        val student = studentsById[a.studentId.toLong()]
        searchText.isBlank() ||
                a.studentId.toString().contains(searchText, ignoreCase = true) ||
                student?.name?.contains(searchText, ignoreCase = true) == true ||
                student?.rollNumber?.contains(searchText, ignoreCase = true) == true
    }

    val total = attendanceList.size
    val presentCount = attendanceList.count(isPresent)
    val absentCount = total - presentCount

    val changed = attendanceList.filter { a ->
        a.id != null && isPresent(a) != (a.status == "Present")
    }

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
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = Blue,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Attendance History",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy
                            )
                            Text(
                                text = "View and edit attendance records",
                                fontSize = 15.sp,
                                color = Muted
                            )
                        }
                    }
                }

                item {
                    HistorySummaryCard(
                        dateLabel = selectedDate?.let { prettyDate(it) } ?: "All dates",
                        total = total,
                        present = presentCount,
                        absent = absentCount
                    )
                }

                item {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search by name, roll number or ID", color = Muted) },
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
                            text = "Records (${filteredAttendance.size})",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )

                        DateFilterChip(
                            label = selectedDate?.let { prettyDate(it) } ?: "All dates",
                            active = selectedDate != null,
                            onClick = { showDatePicker = true },
                            onClear = {
                                selectedDate = null
                                attendanceViewModel.getAllAttendance()
                            }
                        )
                    }
                }

                if (filteredAttendance.isEmpty()) {
                    item { EmptyHistory() }
                } else {
                    items(filteredAttendance) { attendance ->
                        val student = studentsById[attendance.studentId.toLong()]
                        val present = isPresent(attendance)

                        AttendanceHistoryCard(
                            attendance = attendance,
                            isPresent = present,
                            onToggle = { value ->
                                attendance.id?.let { edits[it] = value }
                            },
                            studentName = student?.name,
                            rollNumber = student?.rollNumber,
                            edited = present != (attendance.status == "Present")
                        )
                    }
                }
            }

            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                UpdateButton(changes = changed.size) {
                    changed.forEach { attendance ->
                        val id = attendance.id ?: return@forEach

                        attendanceViewModel.updateAttendance(
                            id,
                            attendance.copy(
                                status = if (isPresent(attendance)) "Present" else "Absent"
                            )
                        )
                    }

                    Toast.makeText(
                        context,
                        "Updating ${changed.size} records",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            // The picker returns UTC midnight, so format in UTC
                            // to avoid the date shifting by a day.
                            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
                                timeZone = TimeZone.getTimeZone("UTC")
                            }
                            val date = formatter.format(Date(millis))

                            selectedDate = date
                            attendanceViewModel.getAttendanceByDate(date)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK", color = Blue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = Muted)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun AttendanceHistoryCard(
    attendance: Attendance,
    isPresent: Boolean,
    onToggle: (Boolean) -> Unit,
    studentName: String? = null,
    rollNumber: String? = null,
    edited: Boolean = false
) {
    val shape = RoundedCornerShape(22.dp)

    val stripe by animateColorAsState(
        targetValue = if (isPresent) Green else Red,
        label = "stripe"
    )

    val title = studentName?.takeIf { it.isNotBlank() } ?: "Student #${attendance.studentId}"

    val (avatarStart, avatarEnd) =
        AvatarGradients[(title.hashCode() and Int.MAX_VALUE) % AvatarGradients.size]

    val initials =
        if (studentName.isNullOrBlank()) "#"
        else studentName
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .map { it.first() }
            .joinToString("")
            .uppercase()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .shadow(3.dp, shape)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, CardLine, shape)
    ) {

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
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(avatarStart, avatarEnd))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = Navy,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (!rollNumber.isNullOrBlank())
                            "Roll no. $rollNumber"
                        else
                            "ID ${attendance.studentId}",
                        color = Muted,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = BlueSoft
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Blue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = prettyDate(attendance.attendanceDate),
                            color = Blue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (edited) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Orange.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "Edited",
                            color = Orange,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                HistoryToggle(
                    label = "Present",
                    icon = Icons.Default.Check,
                    selected = isPresent,
                    selectedBrush = PresentBrush,
                    modifier = Modifier.weight(1f),
                    onClick = { onToggle(true) }
                )
                HistoryToggle(
                    label = "Absent",
                    icon = Icons.Default.Close,
                    selected = !isPresent,
                    selectedBrush = AbsentBrush,
                    modifier = Modifier.weight(1f),
                    onClick = { onToggle(false) }
                )
            }
        }
    }
}

@Composable
private fun HistoryToggle(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    selectedBrush: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val contentColor = if (selected) Color.White else Navy
    val base = modifier
        .height(44.dp)
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
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun HistorySummaryCard(
    dateLabel: String,
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
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = dateLabel,
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
                HistoryStat("Records", total, Color.White, Modifier.weight(1f))
                HistoryStatDivider()
                HistoryStat("Present", present, Mint, Modifier.weight(1f).padding(start = 16.dp))
                HistoryStatDivider()
                HistoryStat("Absent", absent, Coral, Modifier.weight(1f).padding(start = 16.dp))
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
private fun HistoryStat(
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
private fun HistoryStatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(Color.White.copy(alpha = 0.2f))
    )
}


@Composable
private fun DateFilterChip(
    label: String,
    active: Boolean,
    onClick: () -> Unit,
    onClear: () -> Unit
) {
    val shape = RoundedCornerShape(50)

    Row(
        modifier = Modifier
            .clip(shape)
            .background(if (active) BlueSoft else Color.White)
            .border(1.dp, if (active) Blue else Line, shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(start = 12.dp, end = if (active) 6.dp else 12.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = Blue,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = if (active) Blue else Navy,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )

        if (active) {
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable(role = Role.Button, onClick = onClear),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Show all dates",
                    tint = Blue,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun UpdateButton(
    changes: Int,
    onClick: () -> Unit
) {
    val enabled = changes > 0
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
            text = if (enabled) "Update attendance ($changes)" else "No changes yet",
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
private fun EmptyHistory() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No attendance records",
            color = Navy,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Try another date or a different search.",
            color = Muted,
            fontSize = 14.sp
        )
    }
}

private fun prettyDate(iso: String): String = try {
    val parsed = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(iso)
    if (parsed != null) SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(parsed) else iso
} catch (e: Exception) {
    iso
}