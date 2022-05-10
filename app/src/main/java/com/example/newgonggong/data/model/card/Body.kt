package com.example.newgonggong.data.model.card


import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("numOfRows")
    val numOfRows: String,
    @SerializedName("pageNo")
    val pageNo: String,
    @SerializedName("totalCount")
    val totalCount: String
)