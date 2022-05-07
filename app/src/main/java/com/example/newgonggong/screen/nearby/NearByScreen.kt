package com.example.newgonggong

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.newgonggong.screen.nearby.Identifier
import com.example.newgonggong.screen.nearby.MinFabItem
import com.example.newgonggong.screen.nearby.MultiFloatingButton
import com.example.newgonggong.screen.nearby.MultiFloatingState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings

@Composable
fun NearByScreen() {
    var multiFloatingState by remember {
        mutableStateOf(MultiFloatingState.Collapsed)
    }
    val items = listOf(
        MinFabItem(
            icon = R.drawable.ic_card,
            label = "급식카드",
            identifier = Identifier.Card.name
        ),
        MinFabItem(
            icon = R.drawable.ic_food,
            label = "무료급식소",
            identifier = Identifier.Food.name
        ),
        MinFabItem(
            icon = R.drawable.ic_welfare,
            label = "복지관",
            identifier = Identifier.Welfare.name
        ),
        MinFabItem(
            icon = R.drawable.ic_location,
            label = "현재위치",
            identifier = Identifier.Location.name
        )
    )
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.padding(bottom = 50.dp),
        floatingActionButton = {
            MultiFloatingButton(
                multiFloatingState = multiFloatingState,
                onMultiFabStateChange = {
                    multiFloatingState = it
                },
                items = items,
                context = context
            )
        }
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            uiSettings = MapUiSettings(zoomControlsEnabled = false),

            //현재위치로 변경해야함
            cameraPositionState = CameraPositionState(
                CameraPosition(
                    LatLng(22.5726, 88.3639), 12f, 0f, 0f
                )
            )
        )
    }
}

