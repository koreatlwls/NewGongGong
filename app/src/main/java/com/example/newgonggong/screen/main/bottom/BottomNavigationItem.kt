package com.example.newgonggong.screen.main.bottom

import com.example.newgonggong.R

sealed class BottomNavigationItem(var route: String, var icon: Int, var title: String){
    object NearBy : BottomNavigationItem("NearBy", R.drawable.ic_map,"Home")
    object Favorite : BottomNavigationItem("Favorite", R.drawable.ic_star,"Favorite")
}
