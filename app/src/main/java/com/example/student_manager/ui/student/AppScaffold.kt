package com.example.student_manager.ui.student

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppScaffold(
    currentScreen: String,
    onNavigate: (String) -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentScreen = currentScreen,
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
//                .statusBarsPadding()
        ) {
            content()
        }
    }
}