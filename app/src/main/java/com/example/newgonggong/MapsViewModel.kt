package com.example.newgonggong

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

sealed class UiState<out T : Any>{
    object Loading : UiState<Nothing>()
    data class Success<out T : Any>(val data: T) : UiState<T>()
    data class Error(val exception : Exception) : UiState<Nothing>()
}

@ExperimentalCoroutinesApi
class MapsViewModel(
    application : Application
) : AndroidViewModel(application) {
    val seoulStation = Location(37.5283169,126.9294254)
    val location = MutableStateFlow<Location>(seoulStation)

    val cameraPosition = MutableStateFlow<Location?>(null)
    private val zoomLevel = MutableStateFlow<Float>(15.0f)

    private val context = application

    fun setLocation(loc: Location){
        location.value = loc
    }

    fun setCameraPosition(loc:Location){
        if(loc != cameraPosition.value){
            cameraPosition.value = loc
        }
    }

    fun setZoomLevel(zl: Float) {
        zoomLevel.value = zl
    }
}