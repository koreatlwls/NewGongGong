package com.example.newgonggong

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.ColorRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.newgonggong.data.model.Location
import com.example.newgonggong.data.model.Resource
import com.example.newgonggong.data.model.card.Item
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@Composable
fun NearByScreen(
    viewModel: MapsViewModel
) {
    val scaffoldState = rememberScaffoldState()

    val card by viewModel.card.observeAsState()

    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.padding(bottom = 50.dp),
    ) {
        GoogleMapView(viewModel = viewModel, cards = card?.data?.response?.body?.items )
        when(card){
            is Resource.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Error -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "급식카드 가맹점을 가져오는데 실패하였습니다."
                    )
                }
            }
        }
    }
}

@Composable
private fun GoogleMapView(viewModel: MapsViewModel, cards : List<Item>?){
    val context = LocalContext.current
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

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings
    ){
        cards?.forEach {
            val cardLocation = LatLng(it.latitude, it.longitude)
            val icon = bitmapDescriptorFromVector(context, R.drawable.ic_store,R.color.orange)
            Marker(position = cardLocation, title = it.mrhstNm, icon = icon)
        }
    }
}

// TODO move this in to common code
fun bitmapDescriptorFromVector(context: Context, vectorResId: Int, @ColorRes tintColor: Int? = null): BitmapDescriptor? {

    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)

    tintColor?.let {
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, it))
    }
 
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}
