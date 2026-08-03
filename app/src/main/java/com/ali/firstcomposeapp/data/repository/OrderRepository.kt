package com.ali.firstcomposeapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ali.firstcomposeapp.data.local.AppDatabase
import com.ali.firstcomposeapp.data.local.OrderEntity
import com.ali.firstcomposeapp.data.local.dao.OrderDao
import com.ali.firstcomposeapp.data.local.dao.OrderItemDao
import com.ali.firstcomposeapp.data.mapper.toEntity
import com.ali.firstcomposeapp.data.mapper.toOrder
import com.ali.firstcomposeapp.data.mapper.toOrderDetail
import com.ali.firstcomposeapp.data.mediator.OrderRemoteMediator
import com.ali.firstcomposeapp.data.remote.api.OrderApi
import com.ali.firstcomposeapp.data.remote.api.OrderItemApi
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val database: AppDatabase,
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao,
    private val orderApi: OrderApi,
    private val orderItemApi: OrderItemApi
) {

    companion object {
        private const val PAGE_SIZE = 10
    }

    @OptIn(ExperimentalPagingApi::class)
    fun observePagedOrders(): Flow<PagingData<Order>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE * 2,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),

            remoteMediator = OrderRemoteMediator(
                database = database,
                orderApi = orderApi
            ),

            pagingSourceFactory = {
                orderDao.pagingSource()
            }

        )
            .flow
            .map { pagingData ->
                pagingData.map(OrderEntity::toOrder)
            }

    suspend fun deleteOrder(id: String) {
        orderDao.deleteById(id = id)
    }

    fun observeOrderDetail(
        id: String
    ): Flow<OrderDetail?> =
        orderDao
            .observeOrderWithItems(id)
            .map { relation ->
                relation?.toOrderDetail()
            }

    suspend fun refreshOrderItems(
        orderId: String
    ) {

        val remoteItems = orderItemApi.getOrderItems(orderId)
        if(remoteItems.isNotEmpty()) {
            orderItemDao.insertAll(
                remoteItems.mapNotNull {
                    it?.toEntity()
                }
            )
        }
    }
}


