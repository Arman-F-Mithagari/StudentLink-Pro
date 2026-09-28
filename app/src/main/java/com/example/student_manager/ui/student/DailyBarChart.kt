package com.example.student_manager.ui.student

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.student_manager.data.DailyAttendance
import com.example.student_manager.ui.theme.Blue

// Work pending
@Composable
fun DailyBarChart(
    data: List<DailyAttendance>,
    modifier: Modifier = Modifier
) {

    val maxValue = data.maxOfOrNull { it.count } ?: 1

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {

        val chartWidth = size.width
        val chartHeight = size.height

        drawLine(
            color = Color.Gray,
            start = Offset(60f, chartHeight - 40f),
            end = Offset(chartWidth, chartHeight - 40f),
            strokeWidth = 3f
        )

        drawLine(
            color = Color.Gray,
            start = Offset(60f, 0f),
            end = Offset(60f, chartHeight - 40f),
            strokeWidth = 3f
        )

        val barWidth = 60f
        val spacing = 30f

        data.forEachIndexed { index, item ->

            val left = 80f + index * (barWidth + spacing)

            val barHeight =
                (item.count.toFloat() / maxValue.toFloat()) *
                        (chartHeight - 80f)

            drawRect(
                color = Blue,
                topLeft = Offset(
                    left,
                    chartHeight - 40f - barHeight
                ),
                size = androidx.compose.ui.geometry.Size(
                    barWidth,
                    barHeight
                )
            )
        }
    }
}