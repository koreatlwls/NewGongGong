package com.example.newgonggong.data.repository.dataSource

import com.example.newgonggong.data.model.card.CardAPIResponse
import retrofit2.Response

interface CardRemoteDataSource {
    suspend fun getCard(ctprvnNm : String, signguNm : String) : Response<CardAPIResponse>
}