package com.example.student_manager.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_manager.ui.student.MonthlyBarChart
import com.example.student_manager.ui.theme.Blue
import com.example.student_manager.ui.theme.Muted
import com.example.student_manager.ui.theme.Navy
import com.example.student_manager.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyReportScreen(
    viewModel: StudentViewModel,
    onBackClick: () -> Unit
) {

    val monthlyData by viewModel.monthlyAttendance.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMonthlyAttendance()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Monthly Attendance Report",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Navy
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (monthlyData.isEmpty()) {

            Text(
                text = "No monthly attendance data available",
                color = Muted
            )

        } else {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                MonthlyBarChart(
                    data = monthlyData,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Monthly Records",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Navy
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()

            ) {

                items(monthlyData) { item ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = Blue
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = item.month,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Navy
                                )

                                Text(
                                    text = "Total Attendance Records",
                                    color = Muted,
                                    fontSize = 14.sp
                                )
                            }

                            Text(
                                text = item.count.toString(),
                                color = Blue,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}