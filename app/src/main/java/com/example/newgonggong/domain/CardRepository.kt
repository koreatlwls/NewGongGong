package com.example.newgonggong.domain

import com.example.newgonggong.data.model.card.CardAPIResponse
import com.example.newgonggong.data.model.Resource

interface CardRepository {
    suspend fun getCard(ctprvnNm : String, signguNm : String) : Resource<CardAPIResponse>
}