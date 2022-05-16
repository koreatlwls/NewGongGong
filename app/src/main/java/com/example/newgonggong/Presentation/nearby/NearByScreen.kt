package com.example.newgonggong

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newgonggong.data.model.Location
import com.example.newgonggong.data.model.Resource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun NearByScreen(
    viewModel: MapsViewModel
) {
    val scaffoldState = rememberScaffoldState()

    val location by viewModel.location.collectAsState()
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(location.latitude, location.longitude), 15f)
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true,
                zoomControlsEnabled = false
            )
        )
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

    val card by viewModel.card.observeAsState()
    when (card) {
        is Resource.Success -> {
            Log.d("ABC", card?.data?.response?.body?.items.toString())
        }
        is Resource.Loading -> {
            Log.d("ABC", "Loading ~ ")
        }
        is Resource.Error -> {
            Log.d("ABC", "Error ~ ")
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.padding(bottom = 50.dp),
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings
        ) {
            card?.data?.response?.body?.items?.forEach {
                val cardLocation = LatLng(it.latitude, it.longitude)
                Marker(position = cardLocation, title = it.mrhstNm)
            }
        }
    }
}



