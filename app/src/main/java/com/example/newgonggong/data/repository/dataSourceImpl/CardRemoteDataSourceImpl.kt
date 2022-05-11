package com.example.newgonggong.data.repository.dataSourceImpl

import com.example.newgonggong.data.api.APIService
import com.example.newgonggong.data.model.card.CardAPIResponse
import com.example.newgonggong.data.repository.dataSource.CardRemoteDataSource
import retrofit2.Response

class CardRemoteDataSourceImpl(
    private val cardAPIService: APIService
) : CardRemoteDataSource {
    override suspend fun getCard(ctprvnNm: String, signguNm: String): Response<CardAPIResponse> {
        return cardAPIService.getCard(ctprvnNm, signguNm)
    }

}