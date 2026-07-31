package com.ali.firstcomposeapp.data.remote.api

import com.ali.firstcomposeapp.data.remote.dto.OrderDto
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderApi {

    @GET("orders")
    suspend fun getOrders(): List<OrderDto>

    @GET("orders/{id}")
    suspend fun getOrder(
        @Path("id") id: String
    ): OrderDto?
}