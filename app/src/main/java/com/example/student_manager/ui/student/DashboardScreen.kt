package com.example.student_manager.ui.student

import android.R
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.student_manager.ui.theme.Student_ManagerTheme
import com.example.student_manager.viewmodel.StudentViewModel

@Composable
fun DashboardScreen(
    viewModel: StudentViewModel,
    onNewStudentClick: () -> Unit,
    onViewStudentsClick: () -> Unit
) {
    val students by viewModel.studentList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.getStudents()
    }

    val totalStudents = students.size

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp)
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Welcome Admin",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F4C81)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Here is an overview of the institution's performance today.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNewStudentClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor =  Color(0xFF0F4C81)
                )
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add New Student")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }


        item {
            StateCard(
                title = "Total Students",
                icon = Icons.Default.People,
//                value = totalStudents.toString()
                value = students.size.toString()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            StateCard(
                title = "AVG Attendance",
                icon = Icons.Default.CalendarMonth,
                value = "__"
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            StateCard(
                title = "AVG GPA",
                icon = Icons.Default.BarChart,
                value = "__"
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            StateCard(
                title = "Active Courses",
                icon = Icons.Default.Bookmarks,
                value = students
                    .map { it.course }
                    .distinct()
                    .size
                    .toString()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }



        item {
//            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                ActionCard(
                    title = "Add      Student",
                    onClick = onNewStudentClick,
                    icon = Icons.Default.PersonAdd,
                    modifier = Modifier.weight(1f)
                )

                ActionCard(
                    title = "View Students",
                    onClick = onViewStudentsClick,
                    icon = Icons.Default.People,
                    modifier = Modifier
                        .weight(1f)
                )

            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
//                    .padding(2.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                ActionCard(
                    title = "Academic  Reports",
                    onClick = onNewStudentClick,
                    icon = Icons.Default.QueryStats,
                    modifier = Modifier.weight(1f)
                )

                ActionCard(
                    title = "Settings",
                    onClick = onViewStudentsClick,
                    icon = Icons.Default.Settings,
                    modifier = Modifier
                        .weight(1f)

                )

            }
        }


    }
}


//@Preview(showBackground = true)
//@Composable
//fun StateCardPreview() {
//    Student_ManagerTheme {
//        StateCard(
//            title = "Students",
//            value = "245"
//        )
//    }
//}
@Composable
fun StateCard(
    title: String,
    icon: ImageVector,
    value: String
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            1.dp,
            Color(0xFFCAF0F8)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ){
        Column (
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 16.dp
            )
        ){
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title.uppercase(),
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F4C81)
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ActionCardPreview() {
//    Student_ManagerTheme {
//        ActionCard(
//            title = "Add Student",
//            onClick = {}
//        )
//    }
//}
@Composable
fun ActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            1.dp,
            Color(0xFFCAF0F8)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(
                horizontal = 30.dp,
                vertical = 26.dp
            )
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
//            modifier = Modifier.fillMaxSize()

        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F4C81))
        }
    }
}