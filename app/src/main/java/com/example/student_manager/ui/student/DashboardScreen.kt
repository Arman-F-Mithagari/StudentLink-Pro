package com.example.student_manager.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.student_manager.ui.theme.Blue
import com.example.student_manager.ui.theme.CardLine
import com.example.student_manager.ui.theme.DeepBlue
import com.example.student_manager.ui.theme.Green
import com.example.student_manager.ui.theme.Muted
import com.example.student_manager.ui.theme.Navy
import com.example.student_manager.ui.theme.Orange
import com.example.student_manager.ui.theme.PageBg
import com.example.student_manager.ui.theme.Purple
import com.example.student_manager.ui.theme.Red
import com.example.student_manager.ui.theme.Teal
import com.example.student_manager.viewmodel.AttendanceViewModel
import com.example.student_manager.viewmodel.StudentViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val AvgAttendanceWarning = 75


@Composable
fun DashboardScreen(
    viewModel: StudentViewModel,
    onNewStudentClick: () -> Unit,
    onViewStudentsClick: () -> Unit,
    onReportsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    avgAttendance: String? = null,
    avgGpa: String = "--",
    attendanceViewModel: AttendanceViewModel = viewModel()
) {
    val students by viewModel.studentList.observeAsState(emptyList())
    val allAttendance by attendanceViewModel.attendanceList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getStudents()
        attendanceViewModel.getAllAttendance()
    }

    val activeCourses = students
        .map { it.course }
        .filter { it.isNotBlank() }
        .distinct()
        .size

    val totalRecords = allAttendance.size
    val presentRecords = allAttendance.count { it.status == "Present" }
    val avgPercent = if (totalRecords == 0) null else presentRecords * 100 / totalRecords

    val avgAttendanceValue = avgAttendance ?: avgPercent?.let { "$it%" } ?: "--"
    val avgAttendanceCaption =
        if (avgAttendance == null && totalRecords > 0) "$totalRecords records" else null
    val avgAttendanceColor =
        if (avgAttendance == null && avgPercent != null && avgPercent < AvgAttendanceWarning) Red
        else Navy

    val today = remember {
        SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault()).format(Date())
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
                .offset(x = 70.dp, y = 260.dp)
                .size(160.dp)
                .clip(CircleShape)
                .background(Color(0xFFE6EEFF))
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                HeroCard(date = today, onNewStudentClick = onNewStudentClick)
            }

            item {
                SectionTitle("Overview")
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StateCard(
                            title = "Total Students",
                            icon = Icons.Default.People,
                            value = students.size.toString(),
                            accent = Blue,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        StateCard(
                            title = "Avg Attendance",
                            icon = Icons.Default.CalendarMonth,
                            value = avgAttendanceValue,
                            accent = Green,
                            caption = avgAttendanceCaption,
                            valueColor = avgAttendanceColor,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StateCard(
                            title = "Avg GPA",
                            icon = Icons.Default.BarChart,
                            value = avgGpa,
                            accent = Purple,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        StateCard(
                            title = "Active Courses",
                            icon = Icons.Default.Bookmarks,
                            value = activeCourses.toString(),
                            accent = Orange,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }
            }

            item {
                SectionTitle("Quick Actions")
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ActionCard(
                            title = "Add Student",
                            icon = Icons.Default.PersonAdd,
                            onClick = onNewStudentClick,
                            accent = Blue,
                            modifier = Modifier.weight(1f)
                        )
                        ActionCard(
                            title = "View Students",
                            icon = Icons.Default.People,
                            onClick = onViewStudentsClick,
                            accent = Purple,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ActionCard(
                            title = "Academic Reports",
                            icon = Icons.Default.QueryStats,
                            onClick = onReportsClick,
                            accent = Orange,
                            modifier = Modifier.weight(1f)
                        )
                        ActionCard(
                            title = "Settings",
                            icon = Icons.Default.Settings,
                            onClick = onSettingsClick,
                            accent = Teal,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroCard(
    date: String,
    onNewStudentClick: () -> Unit
) {
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

        Column(modifier = Modifier.padding(22.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = date,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Welcome, Admin",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Here is an overview of the institution's performance today.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onNewStudentClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = DeepBlue
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add New Student",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = Navy,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun StateCard(
    title: String,
    icon: ImageVector,
    value: String,
    modifier: Modifier = Modifier,
    accent: Color = Blue,
    caption: String? = null,
    valueColor: Color = Navy
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
        IconTile(icon = icon, accent = accent)

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = value,
            color = valueColor,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = title,
            color = Muted,
            fontSize = 14.sp
        )

        if (caption != null) {
            Text(
                text = caption,
                color = Muted,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
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
            .clickable(role = Role.Button, onClick = onClick)
            .padding(16.dp)
    ) {
        IconTile(icon = icon, accent = accent)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            color = Navy,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun IconTile(icon: ImageVector, accent: Color) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(accent.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(24.dp)
        )
    }
}