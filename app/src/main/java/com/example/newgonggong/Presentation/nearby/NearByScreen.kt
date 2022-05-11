package com.example.newgonggong

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.newgonggong.Presentation.nearby.*
import com.example.newgonggong.data.model.Location
import com.example.newgonggong.data.util.Resource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun NearByScreen(
    viewModel: MapsViewModel
) {
    val scaffoldState = rememberScaffoldState()
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
        )
    )
    val context = LocalContext.current
    val location by viewModel.location.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(location.latitude, location.longitude), 15f)
    }
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true,
                zoomControlsEnabled = false
            )
        )
    }

    val card by viewModel.card.observeAsState()
    when(card){
        is Resource.Success -> {
            Log.d("ABC",card?.data?.response?.body?.items.toString())
        }
        is Resource.Loading -> {
            Log.d("ABC", "Loading ~ ")
        }
        is Resource.Error ->{
            Log.d("ABC", "Error ~ ")
        }
    }

    LaunchedEffect(location) {
        cameraPositionState.move(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), 15f
            )
        )
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        val position = cameraPositionState.position
        val isMoving = cameraPositionState.isMoving

        if (!isMoving) {
            val cameraLocation = Location(position.target.latitude, position.target.longitude)
            viewModel.setCameraPosition(cameraLocation)
            viewModel.setZoomLevel(cameraPositionState.position.zoom)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
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
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings
        ){

            card?.data?.response?.body?.items?.toList()?.forEach {
                val latitude = it.latitude
                val longitude = it.longitude
                if(latitude != null && longitude !=null){
                    val cardLocation = LatLng(latitude, longitude)
                    Marker(position = cardLocation, title =it.mrhstNm)
                }
            }
        }
    }
}



