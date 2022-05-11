package com.example.newgonggong.data.repository

import com.example.newgonggong.data.model.card.CardAPIResponse
import com.example.newgonggong.data.repository.dataSource.CardRemoteDataSource
import com.example.newgonggong.data.util.Resource
import com.example.newgonggong.domain.CardRepository
import retrofit2.Response

class CardRepositoryImpl(
    private val cardRemoteDataSource: CardRemoteDataSource
) : CardRepository {

    override suspend fun getCard(ctprvnNm: String, signguNm: String): Resource<CardAPIResponse> {
        return responseToResource(cardRemoteDataSource.getCard(ctprvnNm, signguNm))
    }

    private fun responseToResource(response: Response<CardAPIResponse>): Resource<CardAPIResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}