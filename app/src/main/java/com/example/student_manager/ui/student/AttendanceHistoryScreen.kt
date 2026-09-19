package com.example.student_manager.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.student_manager.data.Attendance
import com.example.student_manager.viewmodel.AttendanceViewModel
import androidx.compose.material.icons.filled.DateRange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceHistoryScreen(
    attendanceViewModel: AttendanceViewModel = viewModel(),
    onNavigate: (String) -> Unit
) {

    val attendanceList by attendanceViewModel
        .attendanceList
        .collectAsState()

    var searchText by remember {
        mutableStateOf("")
    }

    var selectedDate by remember {
        mutableStateOf("2026-09-19")
    }

    val attendanceMap = remember {
        mutableStateMapOf<Long, Boolean>()
    }

    LaunchedEffect(Unit) {
        attendanceViewModel.getAllAttendance()
    }

    val filteredAttendance = attendanceList.filter {
        it.studentId.toString()
            .contains(searchText, true)
    }

    val presentCount = attendanceList.count {
        it.status == "Present"
    }

    val absentCount = attendanceList.count {
        it.status == "Absent"
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Attendance History",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "View and edit attendance records",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2563EB)
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Attendance Summary",
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Present : $presentCount",
                    color = Color.White
                )

                Text(
                    text = "Absent : $absentCount",
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

//        OutlinedTextField(
//            value = selectedDate,
//            onValueChange = {
//                selectedDate = it
//            },
//            label = {
//                Text("Date")
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Search Student ID")
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        showDatePicker = true
                    }
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Calender"
                    )
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Selected Date: $selectedDate",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(filteredAttendance) { attendance ->

                AttendanceHistoryCard(
                    attendance = attendance,
                    isPresent =
                        attendanceMap[attendance.id ?: 0L]
                            ?: (attendance.status == "Present"),
                    onToggle = { value ->

                        attendanceMap[
                            attendance.id ?: 0L
                        ] = value
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                attendanceList.forEach { attendance ->

                    attendance.id?.let { id ->

                        attendanceViewModel
                            .updateAttendance(
                                id,
                                attendance.copy(
                                    status =
                                        if (
                                            attendanceMap[id]
                                                ?: (attendance.status == "Present")
                                        )
                                            "Present"
                                        else
                                            "Absent"
                                )
                            )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Update Attendance")
        }
    }

    if (showDatePicker) {

        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        datePickerState.selectedDateMillis?.let { millis ->

                            val formatter = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            )

                            selectedDate =
                                formatter.format(Date(millis))

                            attendanceViewModel
                                .getAttendanceByDate(
                                    selectedDate
                                )
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {

            DatePicker(
                state = datePickerState
            )
        }
    }
}

@Composable
fun AttendanceHistoryCard(
    attendance: Attendance,
    isPresent: Boolean,
    onToggle: (Boolean) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Student ID : ${attendance.studentId}",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = attendance.attendanceDate,
                    color = Color.Gray
                )
            }

            Text(
                text =
                    if (isPresent)
                        "Present"
                    else
                        "Absent",
                color =
                    if (isPresent)
                        Color(0xFF2E7D32)
                    else
                        Color.Red
            )

            Spacer(modifier = Modifier.width(12.dp))

            Switch(
                checked = isPresent,
                onCheckedChange = onToggle
            )
        }
    }
}