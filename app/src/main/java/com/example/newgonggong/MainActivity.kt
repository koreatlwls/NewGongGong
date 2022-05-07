package com.example.newgonggong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.newgonggong.ui.theme.NewGongGongTheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewGongGongTheme() {
                MainScreen()
            }

            //map()
        }
    }
}

@Composable
fun map(){
    GoogleMap(
        uiSettings = MapUiSettings(compassEnabled = true)
    )
}