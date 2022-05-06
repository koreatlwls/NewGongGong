package com.example.newgonggong

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MapBotNav(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 60.dp),
        horizontalArrangement =  Arrangement.SpaceBetween
    )  {
        MapBotItem("카드",R.drawable.ic_star)
        MapBotItem("카드",R.drawable.ic_star)
        MapBotItem("카드",R.drawable.ic_star)
    }
}