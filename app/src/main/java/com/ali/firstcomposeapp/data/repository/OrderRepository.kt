package com.ali.firstcomposeapp.data.repository

import androidx.paging.PagingData
import com.ali.firstcomposeapp.domain.model.Order
import com.ali.firstcomposeapp.domain.model.OrderDetail
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    fun observePagedOrders(
        query: String
    ): Flow<PagingData<Order>>

    fun observeOrderDetail(
        orderId: String
    ): Flow<OrderDetail?>

    suspend fun syncOrderDetail(
        orderId: String
    )

    suspend fun deleteOrder(
        id: String
    )
}


