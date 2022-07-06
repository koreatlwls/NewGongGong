package com.example.newgonggong

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.newgonggong.data.model.Location
import com.example.newgonggong.data.model.Resource
import com.example.newgonggong.data.model.card.Item
import com.example.newgonggong.ui.theme.White
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NearByScreen(
    viewModel: MapsViewModel
) {
    val card by viewModel.card.observeAsState()

    val favorites by viewModel.favorites_rdnmadr.collectAsState(setOf())

    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.padding(bottom = 50.dp),
        topBar = {
            TopAppBar(backgroundColor = colorResource(id = R.color.orange),
                title = { Text(text = "GongGong", color = Color.White) }
            )
        }
    ) {
        ModalBottomSheetLayout(
            sheetContent = { CardSheetContent(viewModel = viewModel) },
            sheetState = modalBottomSheetState
        ) {
            Column {
                Box(modifier = Modifier.weight(0.5f)) {
                    GoogleMapView(viewModel, card?.data?.response?.body?.items)
                }
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    when (card) {
                        is Resource.Success -> {
                            card?.data?.response?.body?.items?.let { cards ->
                                CardListView(
                                    viewModel = viewModel,
                                    cards = cards,
                                    favorites = favorites
                                ) {
                                    coroutineScope.launch {
                                        modalBottomSheetState.show()
                                    }
                                    val location =
                                        Location(it.latitude.toDouble(), it.longitude.toDouble())
                                    viewModel.setLocation(location)
                                    viewModel.setCurrentCard(it)
                                }
                            }
                        }
                        is Resource.Loading -> {
                            CircularProgressIndicator()
                        }
                        is Resource.Error -> {
                            Text("주변에 위치한 급식카드 가맹점이 없습니다.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GoogleMapView(viewModel: MapsViewModel, cards: List<Item>?) {
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
    ) {
        cards?.forEach {
            val cardLocation = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
            val icon = bitmapDescriptorFromVector(context, R.drawable.ic_store, R.color.orange)
            Marker(position = cardLocation, title = it.mrhstNm, icon = icon)
        }
    }
}

// TODO move this in to common code
fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int,
    @ColorRes tintColor: Int? = null
): BitmapDescriptor? {

    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    tintColor?.let {
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, it))
    }

    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

@Composable
fun CardListView(
    viewModel: MapsViewModel, cards: List<Item>,
    favorites: Set<String>, itemClick: (card: Item) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(cards) { card ->
            CardView(
                card = card,
                itemClick = itemClick,
                isFavorite = favorites.contains(card.rdnmadr),
                onToggleFavorite = {
                    viewModel.toggleFavorite(card.mrhstNm, card.rdnmadr)
                }
            )
        }
    }
}

@Composable
fun CardView(
    card: Item,
    itemClick: (card: Item) -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = { itemClick(card) })
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.ic_store),
            modifier = Modifier.size(48.dp),
            contentDescription = "Card"
        )

        Spacer(modifier = Modifier.size(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = card.mrhstNm, style = TextStyle(fontSize = 24.sp), maxLines = 1)
            Text(
                text = card.rdnmadr,
                style = TextStyle(color = Color.DarkGray, fontSize = 16.sp),
                maxLines = 1
            )
        }
        FavoritesButton(
            isFavorite = isFavorite,
            onClick = onToggleFavorite
        )
    }
}

@Composable
fun FavoritesButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = { onClick() },
        modifier = modifier
    )
    {
        if (isFavorite) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                tint = White,
                contentDescription = "Favorited"
            )
        } else {
            Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = "Unfavorited")
        }
    }
}

@Composable
fun CardSheetContent(viewModel: MapsViewModel) {
    val currentCard by viewModel.currentCard.collectAsState()

    Column(Modifier.defaultMinSize(minHeight = 400.dp)) {
        Text(
            text = ("가게명 : " + currentCard?.mrhstNm),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = ("도로명 : " + currentCard?.rdnmadr),
            style = typography.h6,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Text(
            text = ("전화번호 : " + currentCard?.phoneNumber),
            style = typography.h6,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Text(
            text = ("평일운영시각 : " + currentCard?.weekdayOperOpenHhmm + "~" + currentCard?.weekdayOperColseHhmm),
            style = typography.h6,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Text(
            text = ("토요일운영시각 : " + currentCard?.satOperOperOpenHhmm + "~" + currentCard?.satOperCloseHhmm),
            style = typography.h6,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Text(
            text = ("공휴일운영시각 : " + currentCard?.holidayOperOpenHhmm + "~" + currentCard?.holidayCloseOpenHhmm),
            style = typography.h6,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}