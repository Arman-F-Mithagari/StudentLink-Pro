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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.student_manager.data.Attendance
import com.example.student_manager.data.Student
import com.example.student_manager.ui.theme.Blue
import com.example.student_manager.ui.theme.BlueSoft
import com.example.student_manager.ui.theme.CardLine
import com.example.student_manager.ui.theme.Green
import com.example.student_manager.ui.theme.Muted
import com.example.student_manager.ui.theme.Navy
import com.example.student_manager.ui.theme.Orange
import com.example.student_manager.ui.theme.PageBg
import com.example.student_manager.ui.theme.Purple
import com.example.student_manager.ui.theme.Red
import com.example.student_manager.ui.theme.SaveBrush
import com.example.student_manager.ui.theme.Teal
import com.example.student_manager.ui.theme.ToggleIdle
import com.example.student_manager.ui.theme.AvatarGradients
import com.example.student_manager.viewmodel.AttendanceViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private const val LowAttendanceThreshold = 75

private const val PreviewRecords = 7


@Composable
fun StudentDetailsScreen(
    student: Student,
    onBackClick: () -> Unit,
    onEditClick: (Student) -> Unit,
    attendance: String? = null,
    attendanceViewModel: AttendanceViewModel = viewModel()
) {
    val context = LocalContext.current

    val allAttendance by attendanceViewModel.attendanceList.collectAsState()

    LaunchedEffect(student.id) {
        attendanceViewModel.getAllAttendance()
    }

    val records = remember(allAttendance, student.id) {
        allAttendance
            .filter { it.studentId.toLong() == student.id }
            .sortedByDescending { it.attendanceDate }
    }

    val totalDays = records.size
    val presentDays = records.count { it.status == "Present" }
    val absentDays = totalDays - presentDays
    val percent = if (totalDays == 0) null else presentDays * 100 / totalDays

    val attendanceValue = attendance ?: percent?.let { "$it%" } ?: "N/A"
    val attendanceCaption = if (attendance == null && totalDays > 0) {
        "$presentDays of $totalDays days"
    } else null
    val attendanceValueColor =
        if (attendance == null && percent != null && percent < LowAttendanceThreshold) Red
        else Navy

    val (avatarStart, avatarEnd) =
        AvatarGradients[(student.name.hashCode() and Int.MAX_VALUE) % AvatarGradients.size]

    val initials = student.name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first() }
        .joinToString("")
        .uppercase()
        .ifEmpty { "?" }

    val isAlumni = student.semester >= 8 // check and update later

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
                    .offset(x = 70.dp, y = 320.dp)
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE6EEFF))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
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
                            text = "Student Details",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy
                        )
                        Text(
                            text = "Profile and academic information",
                            fontSize = 15.sp,
                            color = Muted
                        )
                    }
                }

                ProfileHero(
                    name = student.name,
                    rollNumber = student.rollNumber,
                    course = student.course,
                    initials = initials,
                    avatarStart = avatarStart,
                    avatarEnd = avatarEnd,
                    isAlumni = isAlumni
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailAction(
                        label = "Edit Student",
                        icon = Icons.Default.Edit,
                        contentColor = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SaveBrush),
                        onClick = { onEditClick(student) }
                    )
                    DetailAction(
                        label = "Export",
                        icon = Icons.Default.Download,
                        contentColor = Blue,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(BlueSoft),
                        onClick = {
                            Toast.makeText(
                                context,
                                "Export feature coming soon",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard(
                            title = "Attendance",
                            value = attendanceValue,
                            icon = Icons.Default.CalendarMonth,
                            accent = Green,
                            caption = attendanceCaption,
                            valueColor = attendanceValueColor,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "CGPA",
                            value = "N/A",
                            icon = Icons.Default.BarChart,
                            accent = Purple,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard(
                            title = "Subjects",
                            value = "N/A",
                            icon = Icons.Default.Subject,
                            accent = Orange,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Standing",
                            value = "N/A",
                            icon = Icons.Default.StarBorder,
                            accent = Teal,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                AttendanceRecordCard(
                    records = records,
                    present = presentDays,
                    absent = absentDays,
                    percent = percent
                )

                val infoShape = RoundedCornerShape(22.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, infoShape)
                        .clip(infoShape)
                        .background(Color.White)
                        .border(1.dp, CardLine, infoShape)
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Contact & Academic Information",
                        color = Navy,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    InfoRow(Icons.Default.Email, Blue, "Email", student.email)
                    InfoDivider()
                    InfoRow(Icons.Default.Phone, Green, "Phone", student.phoneNumber)
                    InfoDivider()
                    InfoRow(Icons.Default.School, Purple, "Course", student.course)
                    InfoDivider()
                    InfoRow(
                        Icons.Default.MenuBook,
                        Orange,
                        "Semester",
                        student.semester.toString()
                    )
                    InfoDivider()
                    InfoRow(Icons.Default.Groups, Teal, "Division", student.division)
                }
            }
        }
    }
}

@Composable
private fun AttendanceRecordCard(
    records: List<Attendance>,
    present: Int,
    absent: Int,
    percent: Int?
) {
    val shape = RoundedCornerShape(22.dp)
    var showAll by remember { mutableStateOf(false) }
    val visible = if (showAll) records else records.take(PreviewRecords)
    val tone = if (percent != null && percent < LowAttendanceThreshold) Red else Green

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, shape)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, CardLine, shape)
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Attendance Record",
                color = Navy,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            if (percent != null) {
                Text(
                    text = "$percent%",
                    color = tone,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (percent != null) {
            val total = present + absent
            val fraction = if (total == 0) 0f else present.toFloat() / total

            Spacer(modifier = Modifier.height(12.dp))

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
                        .background(tone)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CountChip("$present present", Green)
                CountChip("$absent absent", Red)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (records.isEmpty()) {
            Text(
                text = "No attendance recorded yet.",
                color = Muted,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            visible.forEachIndexed { index, record ->
                if (index > 0) InfoDivider()
                RecordRow(record)
            }

            if (records.size > PreviewRecords) {
                TextButton(
                    onClick = { showAll = !showAll },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = if (showAll) "Show less" else "Show all ${records.size} records",
                        color = Blue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordRow(record: Attendance) {
    val isPresent = record.status == "Present"
    val tone = if (isPresent) Green else Red

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(tone)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = recordDate(record.attendanceDate),
            color = Navy,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        Surface(
            shape = RoundedCornerShape(50),
            color = tone.copy(alpha = 0.12f)
        ) {
            Text(
                text = if (isPresent) "Present" else "Absent",
                color = tone,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun CountChip(text: String, tone: Color) {
    Surface(
        shape = RoundedCornerShape(50),
        color = tone.copy(alpha = 0.12f)
    ) {
        Text(
            text = text,
            color = tone,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

private fun recordDate(iso: String): String = try {
    val parsed = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(iso)
    if (parsed != null) SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(parsed) else iso
} catch (e: Exception) {
    iso
}

@Composable
private fun ProfileHero(
    name: String,
    rollNumber: String,
    course: String,
    initials: String,
    avatarStart: Color,
    avatarEnd: Color,
    isAlumni: Boolean
) {
    val shape = RoundedCornerShape(26.dp)
    val courseIcon =
        if (course.contains("comput", ignoreCase = true)) Icons.Default.Laptop
        else Icons.Default.School

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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .border(3.dp, Color.White.copy(alpha = 0.9f), CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(avatarStart, avatarEnd))),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = name,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Roll no. $rollNumber",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeroChip(
                    text = course,
                    icon = courseIcon,
                    modifier = Modifier.weight(1f, fill = false)
                )
                HeroChip(
                    text = if (isAlumni) "Alumni" else "Active student",
                    icon = null
                )
            }
        }
    }
}

@Composable
private fun HeroChip(
    text: String,
    icon: ImageVector?,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.White.copy(alpha = 0.18f),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DetailAction(
    label: String,
    icon: ImageVector,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.clickable(role = Role.Button, onClick = onClick),
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
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
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
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(accent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = accent,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = value,
            color = valueColor,
            fontSize = 26.sp,
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
private fun InfoRow(
    icon: ImageVector,
    accent: Color,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = label,
                color = Muted,
                fontSize = 12.sp
            )
            Text(
                text = value,
                color = Navy,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun InfoDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(CardLine)
    )
}