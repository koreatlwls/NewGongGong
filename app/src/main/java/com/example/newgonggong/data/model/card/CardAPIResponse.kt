package com.example.newgonggong.data.model.card


import com.google.gson.annotations.SerializedName

data class CardAPIResponse(
    @SerializedName("response")
    val response: Response
)