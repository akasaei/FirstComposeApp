package com.ali.firstcomposeapp.data.remote.api

import com.ali.firstcomposeapp.data.remote.dto.OrderItemDto
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderItemApi {

    @GET("orderItem")
    suspend fun getOrderItems(): List<OrderItemDto>

    @GET("orderItem/{id}")
    suspend fun getOrderItem(
        @Path("id") id: String
    ): OrderItemDto?
}