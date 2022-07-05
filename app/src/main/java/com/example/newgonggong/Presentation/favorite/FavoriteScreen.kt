package com.example.newgonggong

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FavoriteScreen(
    viewModel: MapsViewModel
) {
    val favoritesItem by viewModel.favorites_item.collectAsState(setOf())
    val favoritesItemList = favoritesItem.toList()

    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = colorResource(id = R.color.orange),
            title = {Text(text = "GongGong-Favorites", color = Color.White)})
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(favoritesItemList) { favorites ->
                val favoritesSplit = favorites.split("+")
                FavoriteView(
                    mrhstnm = favoritesSplit[0],
                    rdnmadr = favoritesSplit[1],
                    onToggleFavorite = {
                        viewModel.toggleFavorite(
                            favoritesSplit[0],
                            favoritesSplit[1]
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun FavoriteView(
    mrhstnm: String,
    rdnmadr: String,
    onToggleFavorite: () -> Unit
) {

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.ic_store),
            modifier = Modifier.size(48.dp),
            contentDescription = "Favorite"
        )

        Spacer(modifier = Modifier.size(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = mrhstnm,
                style = androidx.compose.ui.text.TextStyle(fontSize = 24.sp),
                maxLines = 1
            )
            Text(
                text = rdnmadr,
                style = androidx.compose.ui.text.TextStyle(
                    color = Color.DarkGray,
                    fontSize = 16.sp
                ),
                maxLines = 1
            )
        }

        FavoritesButton(
            isFavorite = true,
            onClick = onToggleFavorite
        )
    }
}