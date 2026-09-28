package com.example.student_manager.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_manager.ui.theme.Blue
import com.example.student_manager.ui.theme.Muted
import com.example.student_manager.ui.theme.Navy
import com.example.student_manager.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyReportScreen(
    viewModel: StudentViewModel,
    onBackClick: () -> Unit
) {

    val dailyData by viewModel.dailyAttendance.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getDailyAttendance()
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
                text = "Daily Attendance Report",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Navy
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (dailyData.isEmpty()) {

            Text(
                text = "No daily attendance data available",
                color = Muted
            )

        } else {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                DailyBarChart(
                    data = dailyData,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Daily Records",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Navy
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(dailyData) { item ->

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
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Blue
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = item.date,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Navy
                                )

                                Text(
                                    text = "Attendance Records",
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