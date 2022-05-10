package com.example.newgonggong

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newgonggong.screen.main.BottomNavigationItem
import com.example.newgonggong.utils.FusedLocationWrapper
import com.example.newgonggong.utils.PermissionState

@SuppressLint("MissingPermission")
@Composable
fun MainScreen(
    fineLocation : PermissionState,
    fusedLocationWrapper: FusedLocationWrapper,
    viewModel: MapsViewModel
) {
    val navController = rememberNavController()
    val hasLocationPermission by fineLocation.hasPermission.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
    ){
        if(hasLocationPermission){
            LaunchedEffect(fusedLocationWrapper){
                val location = fusedLocationWrapper.awaitLastLocation()
                viewModel.setLocation(Location(location.latitude, location.longitude))
            }

            NavHost(navController, startDestination = BottomNavigationItem.NearBy.route) {
                composable(BottomNavigationItem.NearBy.route) {
                    NearByScreen(viewModel)
                }
                composable(BottomNavigationItem.Favorite.route) {
                    FavoriteScreen()
                }
            }
        }else{
            fineLocation.launchPermissionRequest()
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavigationItem.NearBy,
        BottomNavigationItem.Favorite
    )
    androidx.compose.material.BottomNavigation(
        backgroundColor = colorResource(id = R.color.orange),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                selected = currentRoute == item.route,
                label = { Text(text = item.title, fontSize = 14.sp) },
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

