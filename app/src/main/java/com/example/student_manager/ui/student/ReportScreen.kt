package com.example.student_manager.ui.student

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.student_manager.ui.theme.Blue
import com.example.student_manager.ui.theme.CardLine
import com.example.student_manager.ui.theme.Green
import com.example.student_manager.ui.theme.Muted
import com.example.student_manager.ui.theme.Navy
import com.example.student_manager.ui.theme.PageBg
import com.example.student_manager.ui.theme.Purple
import com.example.student_manager.ui.theme.Red
import com.example.student_manager.ui.theme.SaveBrush
import com.example.student_manager.ui.theme.ToggleIdle
import com.example.student_manager.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    onBackClick: () -> Unit,
    viewModel: StudentViewModel
) {
    val context = LocalContext.current

    val reportData by viewModel.reportData.observeAsState()
    val monthlyData by viewModel.monthlyAttendance.collectAsState()
    val dailyData by viewModel.dailyAttendance.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getReportSummary()
        viewModel.getMonthlyAttendance()
        viewModel.getDailyAttendance()
    }

    val overallPercent = reportData?.attendancePercentage ?: 0.0
    val monthlyMax = monthlyData.maxOfOrNull { it.count } ?: 0L
    val dailyMax = dailyData.maxOfOrNull { it.count } ?: 0L

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
                .offset(x = 70.dp, y = 220.dp)
                .size(160.dp)
                .clip(CircleShape)
                .background(Color(0xFFE6EEFF))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
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
                        text = "Reports",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy
                    )
                    Text(
                        text = "Attendance overview and trends",
                        fontSize = 15.sp,
                        color = Muted
                    )
                }
            }

            OverviewCard(percent = overallPercent)

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ReportCard(
                        title = "Students",
                        value = reportData?.totalStudents?.toString() ?: "0",
                        icon = Icons.Default.PeopleAlt,
                        accent = Blue,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    ReportCard(
                        title = "Attendance",
                        value = reportData?.totalAttendance?.toString() ?: "0",
                        icon = Icons.Default.Assignment,
                        accent = Purple,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ReportCard(
                        title = "Present",
                        value = reportData?.presentCount?.toString() ?: "0",
                        icon = Icons.Default.CheckCircle,
                        accent = Green,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    ReportCard(
                        title = "Absent",
                        value = reportData?.absentCount?.toString() ?: "0",
                        icon = Icons.Default.Cancel,
                        accent = Red,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
            }

            TrendCard(title = "Monthly Attendance") {
                if (monthlyData.isEmpty()) {
                    EmptyTrend("No monthly data yet.")
                } else {
                    monthlyData.forEach { item ->
                        TrendBarRow(
                            label = item.month,
                            value = item.count,
                            maxValue = monthlyMax,
                            color = Blue
                        )
                    }
                }
            }

            TrendCard(title = "Daily Attendance") {
                if (dailyData.isEmpty()) {
                    EmptyTrend("No daily data yet.")
                } else {
                    dailyData.forEach { item ->
                        TrendBarRow(
                            label = item.date,
                            value = item.count,
                            maxValue = dailyMax,
                            color = Purple
                        )
                    }
                }
            }

            Text(
                text = "Quick Actions",
                color = Navy,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionButton(
                    label = "View Defaulters",
                    icon = Icons.Default.PersonOff,
                    modifier = Modifier.weight(1f)
                ) {
                    Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
                }
                QuickActionButton(
                    label = "Export Report",
                    icon = Icons.Default.Download,
                    modifier = Modifier.weight(1f)
                ) {
                    Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun OverviewCard(percent: Double) {
    val shape = RoundedCornerShape(26.dp)
    val fraction = (percent / 100.0).coerceIn(0.0, 1.0).toFloat()

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
                    imageVector = Icons.Default.QueryStats,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Overall Attendance",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = String.format("%.1f%%", percent),
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

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
        }
    }
}

@Composable
fun ReportCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    accent: Color = Blue
) {
    val shape = RoundedCornerShape(22.dp)

    Column(
        modifier = modifier
            .shadow(3.dp, shape)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, CardLine, shape)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
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

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = value,
            color = Navy,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = title,
            color = Muted,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun TrendCard(
    title: String,
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
        Text(
            text = title,
            color = Navy,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        content()
    }
}

@Composable
private fun TrendBarRow(
    label: String,
    value: Long,
    maxValue: Long,
    color: Color
) {
    val fraction = if (maxValue == 0L) 0f else value.toFloat() / maxValue

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = Navy,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value.toString(),
                color = Muted,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(ToggleIdle)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
        }
    }
}

@Composable
private fun EmptyTrend(message: String) {
    Text(
        text = message,
        color = Muted,
        fontSize = 14.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun QuickActionButton(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(SaveBrush)
            .clickable(role = Role.Button, onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}