package com.example.newgonggong.data.model.card


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("ctprvnNm")
    val ctprvnNm: String,
    @SerializedName("dlvrCloseOpenHhmm")
    val dlvrCloseOpenHhmm: String,
    @SerializedName("dlvrOperOpenHhmm")
    val dlvrOperOpenHhmm: String,
    @SerializedName("holidayCloseOpenHhmm")
    val holidayCloseOpenHhmm: String,
    @SerializedName("holidayOperOpenHhmm")
    val holidayOperOpenHhmm: String,
    @SerializedName("institutionNm")
    val institutionNm: String,
    @SerializedName("institutionPhoneNumber")
    val institutionPhoneNumber: String,
    @SerializedName("insttCode")
    val insttCode: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("lnmadr")
    val lnmadr: String,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("mrhstCode")
    val mrhstCode: String,
    @SerializedName("mrhstNm")
    val mrhstNm: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("rdnmadr")
    val rdnmadr: String,
    @SerializedName("referenceDate")
    val referenceDate: String,
    @SerializedName("satOperCloseHhmm")
    val satOperCloseHhmm: String,
    @SerializedName("satOperOperOpenHhmm")
    val satOperOperOpenHhmm: String,
    @SerializedName("signguCode")
    val signguCode: String,
    @SerializedName("signguNm")
    val signguNm: String,
    @SerializedName("weekdayOperColseHhmm")
    val weekdayOperColseHhmm: String,
    @SerializedName("weekdayOperOpenHhmm")
    val weekdayOperOpenHhmm: String
)