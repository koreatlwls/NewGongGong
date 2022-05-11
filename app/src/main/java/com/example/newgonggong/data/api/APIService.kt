package com.example.newgonggong.data.api

import com.example.newgonggong.BuildConfig
import com.example.newgonggong.data.model.card.CardAPIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("openapi/tn_pubr_public_chil_wlfare_mlsv_api")
    suspend fun getCard(
        @Query("ctprvnNm")
        ctprvnNm : String,
        @Query("signguNm")
        signguNm : String,
        @Query("serviceKey")
        serviceKey:String = BuildConfig.CARD_API_KEY,
        @Query("pageNo")
        pageNo : Int = 1,
        @Query("numOfRows")
        numOfRows : Int = 500,
        @Query("type")
        type : String = "json"
    ) : Response<CardAPIResponse>

}