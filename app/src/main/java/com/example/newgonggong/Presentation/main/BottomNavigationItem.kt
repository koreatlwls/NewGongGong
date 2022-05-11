package com.example.newgonggong.Presentation.main

import com.example.newgonggong.R

sealed class BottomNavigationItem(var route: String, var icon: Int, var title: String){
    object NearBy : BottomNavigationItem("NearBy", R.drawable.ic_map,"홈")
    object Favorite : BottomNavigationItem("Favorite", R.drawable.ic_star,"즐겨찾기")
}
