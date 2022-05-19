package com.example.newgonggong

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Geocoder
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
import com.example.newgonggong.data.model.Resource
import com.example.newgonggong.domain.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
class MapsViewModel(
    @get:JvmName("getAdapterContext")
    private val application: Application,
    private val cardRepository: CardRepository
) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val seoulStation = Location(37.5283169, 126.9294254)
    private val _location = MutableStateFlow(seoulStation)
    val location: StateFlow<Location> = _location.asStateFlow()

    private val zoomLevel = MutableStateFlow(15.0f)

    private val _cameraPosition = MutableStateFlow<Location?>(null)
    val cameraPosition: StateFlow<Location?> = _cameraPosition

    private val _card: MutableLiveData<Resource<CardAPIResponse>> = MutableLiveData()
    val card: LiveData<Resource<CardAPIResponse>> = _card

    private var lastCtprvnNm: String? = null
    private var lastSignguNm: String? = null

    fun setLocation(loc: Location) {
        _location.value = loc
        val addressSplit = getAddress(context, loc.latitude, loc.longitude).split(" ")

        if (addressSplit.size > 2) {
            getCard(addressSplit[1], addressSplit[2])
        }
    }

    fun setCameraPosition(loc: Location) {
        if (loc != cameraPosition.value) {
            _cameraPosition.value = loc
            val addressSplit = getAddress(context, loc.latitude, loc.longitude).split(" ")

            if (addressSplit.size > 2) {
                if (addressSplit[1] != lastCtprvnNm && addressSplit[2] != lastSignguNm) {
                    getCard(addressSplit[1], addressSplit[2])
                    lastCtprvnNm = addressSplit[1]
                    lastSignguNm = addressSplit[2]
                }
            }
        }
    }

    fun setZoomLevel(zl: Float) {
        zoomLevel.value = zl
    }

    private fun getCard(ctprvnNm: String, signguNm: String) =
        viewModelScope.launch(Dispatchers.IO) {
            _card.postValue(Resource.Loading())
            try {
                if (isNetworkAvailable(application)) {
                    val apiResult = cardRepository.getCard(ctprvnNm, signguNm)
                    _card.postValue(apiResult)
                } else {
                    _card.postValue(Resource.Error("Internet is not available"))
                }
            } catch (e: Exception) {
               _card.postValue(Resource.Error(e.toString()))
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

    private fun getAddress(context: Context, lat: Double, lng: Double): String {
        val geocoder = Geocoder(context, Locale.KOREA)
        val list = geocoder.getFromLocation(lat, lng, 1)

        if (list.size != 0) {
            return list[0].getAddressLine(0)
        }
        return "서울특별시 중구 소공동 세종대로18길 2"
    }
}