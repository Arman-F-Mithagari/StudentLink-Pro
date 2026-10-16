package com.example.student_manager.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.student_manager.ui.theme.Blue
import com.example.student_manager.ui.theme.BlueSoft
import com.example.student_manager.ui.theme.CardLine
import com.example.student_manager.ui.theme.Green
import com.example.student_manager.ui.theme.Muted
import com.example.student_manager.ui.theme.Navy
import com.example.student_manager.ui.theme.Orange
import com.example.student_manager.ui.theme.PageBg
import com.example.student_manager.ui.theme.Red
import com.example.student_manager.ui.theme.SaveBrush
import com.example.student_manager.ui.theme.ToggleIdle
import com.example.student_manager.viewmodel.StudentViewModel
import java.util.Locale


@Composable
fun DefaultersScreen(
    viewModel: StudentViewModel,
    onBackClick: () -> Unit
) {
    var threshold by remember { mutableFloatStateOf(75f) }
    var appliedThreshold by remember { mutableIntStateOf(75) }

    val defaulters by viewModel.defaulters.collectAsStateWithLifecycle()
    val loading by viewModel.defaultersLoading.collectAsStateWithLifecycle()
    val error by viewModel.defaultersError.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getDefaulters(75.0)
    }

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
                    .offset(x = 70.dp, y = 220.dp)
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE6EEFF))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding())
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
                            text = "Defaulters",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy
                        )
                        Text(
                            text = "Students below the attendance requirement",
                            fontSize = 14.sp,
                            color = Muted
                        )
                    }
                }

                ThresholdCard(
                    threshold = threshold,
                    appliedThreshold = appliedThreshold,
                    onThresholdChange = { threshold = it },
                    onApply = {
                        appliedThreshold = threshold.toInt()
                        viewModel.getDefaulters(appliedThreshold.toDouble())
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Below $appliedThreshold%",
                            color = Navy,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (!loading && error == null) {
                            Text(
                                text = "${defaulters.size} student(s) found",
                                color = Muted,
                                fontSize = 13.sp
                            )
                        }
                    }

                    if (defaulters.isNotEmpty() && !loading && error == null) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Red.copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = null,
                                    tint = Red,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Needs attention",
                                    color = Red,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    when {
                        loading -> LoadingState()

                        error != null -> ErrorState(
                            message = error ?: "Something went wrong",
                            onRetry = { viewModel.getDefaulters(appliedThreshold.toDouble()) }
                        )

                        defaulters.isEmpty() -> EmptyDefaulters(threshold = appliedThreshold)

                        else -> LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 8.dp)
                        ) {
                            items(
                                items = defaulters,
                                key = { it.studentId ?: it.email }
                            ) { student ->
                                DefaulterCard(
                                    name = student.name,
                                    email = student.email,
                                    course = student.course,
                                    percent = student.attendancePercentage,
                                    threshold = appliedThreshold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ThresholdCard(
    threshold: Float,
    appliedThreshold: Int,
    onThresholdChange: (Float) -> Unit,
    onApply: () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    val dirty = threshold.toInt() != appliedThreshold

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, shape)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, CardLine, shape)
            .padding(18.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Blue.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Blue,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Minimum attendance requirement",
                color = Navy,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "${threshold.toInt()}%",
            color = Blue,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Slider(
            value = threshold,
            onValueChange = onThresholdChange,
            valueRange = 0f..100f,
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = Blue,
                activeTrackColor = Blue,
                inactiveTrackColor = ToggleIdle
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0%", color = Muted, fontSize = 12.sp)
            Text("100%", color = Muted, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .alpha(if (dirty) 1f else 0.6f)
                .clip(RoundedCornerShape(16.dp))
                .background(SaveBrush)
                .clickable(role = Role.Button, onClick = onApply),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Apply Threshold",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
private fun DefaulterCard(
    name: String,
    email: String,
    course: String,
    percent: Double,
    threshold: Int
) {
    val shape = RoundedCornerShape(20.dp)
    val fraction = (percent / 100.0).coerceIn(0.0, 1.0).toFloat()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, shape)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, CardLine, shape)
    ) {

        // Status stripe
        Box(
            modifier = Modifier
                .width(6.dp)
                .fillMaxHeight()
                .background(Red)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(14.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Red.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PriorityHigh,
                        contentDescription = null,
                        tint = Red,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        color = Navy,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (email.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = Muted,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = email,
                                color = Muted,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Text(
                    text = String.format(Locale.getDefault(), "%.1f%%", percent),
                    color = Red,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (course.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = BlueSoft
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = Blue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = course,
                            color = Blue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

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
                        .background(Red)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Below the required $threshold%",
                color = Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Blue)
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.WarningAmber,
            contentDescription = null,
            tint = Red,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = message,
            color = Red,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(SaveBrush)
                .clickable(role = Role.Button, onClick = onRetry)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Retry", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EmptyDefaulters(threshold: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Green.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = Green,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "No defaulters found",
            color = Navy,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Every student is above $threshold% attendance.",
            color = Muted,
            fontSize = 13.sp
        )
    }
}