package com.example.newgonggong

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.newgonggong.screen.main.bottom.BottomNavigation

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BotNavBar(navController) },
    ){
        BottomNavigation(navController = navController)
    }
}
