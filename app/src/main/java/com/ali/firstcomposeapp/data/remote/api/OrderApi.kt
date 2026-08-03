package com.ali.firstcomposeapp.data.remote.api

import com.ali.firstcomposeapp.data.remote.dto.OrderDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApi {
    @GET("orders")
    suspend fun getOrders(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<OrderDto>

    @GET("orders/{id}")
    suspend fun getOrder(
        @Path("id") id: String
    ): OrderDto?
}