package com.example.student_manager.ui.student

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BottomNavBar(
    currentScreen: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar (
        containerColor = Color(0xFFFFFFFF)
    ) {

        NavigationBarItem(
            selected = currentScreen == "dashboard",
            onClick = { onNavigate("dashboard") },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )


        NavigationBarItem(
            selected = currentScreen == "view",
            onClick = { onNavigate("view") },
            icon = { Icon(Icons.Default.People, null) },
            label = { Text("Students") }
        )

        NavigationBarItem(
            selected = currentScreen == "attendance",
            onClick = { onNavigate("attendance") },
            icon = { Icon(Icons.Default.DateRange, null) },
            label = { Text("Attendance") }
        )

        NavigationBarItem(
            selected = currentScreen == "reports",
            onClick = { onNavigate("reports") },
            icon = { Icon(Icons.Default.BarChart, null) },
            label = { Text("Reports") }
        )
    }
}

