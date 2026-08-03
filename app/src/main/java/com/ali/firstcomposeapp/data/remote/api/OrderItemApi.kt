package com.ali.firstcomposeapp.data.remote.api

import com.ali.firstcomposeapp.data.remote.dto.OrderItemDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OrderItemApi {
    @GET("orderItem")
    suspend fun getOrderItems(
        @Query("orderId") orderId: String
    ): List<OrderItemDto?>
}