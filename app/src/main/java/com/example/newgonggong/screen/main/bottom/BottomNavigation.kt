package com.example.newgonggong.screen.main.bottom

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newgonggong.FavoriteScreen
import com.example.newgonggong.NearByScreen

@Composable
fun BottomNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavigationItem.NearBy.route) {
        composable(BottomNavigationItem.NearBy.route) {
            NearByScreen()
        }
        composable(BottomNavigationItem.Favorite.route) {
            FavoriteScreen()
        }
    }
}