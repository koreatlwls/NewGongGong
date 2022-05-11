package com.example.newgonggong

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.ExperimentalComposeApi
import com.example.newgonggong.ui.theme.NewGongGongTheme
import com.example.newgonggong.data.util.checkSelfPermissionState
import com.example.newgonggong.data.util.fusedLocationWrapper
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

        Log.d("ABC", mapsViewModel.getCard("대구광역시","달서구").toString())
    }
}
