package com.example.newgonggong

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.ExperimentalComposeApi
import com.example.newgonggong.ui.theme.NewGongGongTheme
import com.example.newgonggong.utils.checkSelfPermissionState
import com.example.newgonggong.utils.fusedLocationWrapper
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mapsViewModel by viewModel<MapsViewModel>()

    @OptIn(ExperimentalComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewGongGongTheme() {
                val fusedLocationWrapper = fusedLocationWrapper()
                val fineLocation = checkSelfPermissionState(activity = this, permission = Manifest.permission.ACCESS_FINE_LOCATION)
                MainScreen(fineLocation, fusedLocationWrapper, mapsViewModel)
            }
        }
    }
}
