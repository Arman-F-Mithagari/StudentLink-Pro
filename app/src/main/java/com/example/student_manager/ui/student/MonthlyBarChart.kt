package com.example.student_manager.ui.student

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.student_manager.data.MonthlyAttendance

//Work Pending
@Composable
fun MonthlyBarChart(
    data: List<MonthlyAttendance>,
    modifier: Modifier = Modifier
) {

    val maxValue = data.maxOfOrNull { it.count } ?: 1

    Column(modifier = modifier) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {

            val barWidth = size.width / (data.size * 2)

            data.forEachIndexed { index, item ->

                val barHeight =
                    (item.count.toFloat() / maxValue.toFloat()) * size.height

                val x = index * (barWidth * 2) + barWidth / 2

                drawRect(
                    color = Color(0xFF2563EB),
                    topLeft = Offset(
                        x,
                        size.height - barHeight
                    ),
                    size = androidx.compose.ui.geometry.Size(
                        barWidth,
                        barHeight
                    )
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEach {
                Text(
                    text = it.month.take(3),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}