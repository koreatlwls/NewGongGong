package com.example.newgonggong

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newgonggong.data.model.Location
import com.example.newgonggong.data.model.card.CardAPIResponse
import com.example.newgonggong.data.util.Resource
import com.example.newgonggong.domain.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MapsViewModel(
    @get:JvmName("getAdapterContext")
    private val application : Application,
    private val cardRepository: CardRepository
) : AndroidViewModel(application) {

    val seoulStation = Location(37.5283169,126.9294254)
    private val _location = MutableStateFlow(seoulStation)
    val location : StateFlow<Location> = _location.asStateFlow()

    private val zoomLevel = MutableStateFlow(15.0f)

    private val _cameraPosition = MutableStateFlow<Location?>(null)
    val cameraPosition : StateFlow<Location?> = _cameraPosition

    private val _card : MutableLiveData<Resource<CardAPIResponse>> = MutableLiveData()
    val card : LiveData<Resource<CardAPIResponse>> = _card

    fun setLocation(loc: Location){
        _location.value = loc
    }

    fun setCameraPosition(loc: Location){
        if(loc != cameraPosition.value){
            _cameraPosition.value = loc
        }
    }

    fun setZoomLevel(zl: Float) {
        zoomLevel.value = zl
    }

    fun getCard(ctprvnNm : String, signguNm : String) = viewModelScope.launch(Dispatchers.IO) {
        _card.postValue(Resource.Loading())
        try{
            if(isNetworkAvailable(application)){
                val apiResult = cardRepository.getCard(ctprvnNm, signguNm)
                _card.postValue(apiResult)
            }else{
                _card.postValue(Resource.Error("Internet is not available"))
            }
        }catch (e: Exception){

        }
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false

    }
}