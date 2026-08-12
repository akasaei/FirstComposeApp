package com.ali.firstcomposeapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
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
import com.ali.firstcomposeapp.domain.model.Order
import com.ali.firstcomposeapp.domain.model.OrderDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DefaultOrderRepository @Inject constructor(
    private val database: AppDatabase,
    private val orderApi: OrderApi,
    private val orderItemApi: OrderItemApi
) : OrderRepository {
    private val orderDao: OrderDao = database.orderDao()
    private val orderItemDao: OrderItemDao = database.orderItemDao()
    companion object {
        private const val PAGE_SIZE = 10
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun observePagedOrders(query: String): Flow<PagingData<Order>> =
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
                orderDao.pagingSource(query)
            }

        )
            .flow
            .map { pagingData ->
                pagingData.map(OrderEntity::toOrder)
            }

    override fun observeOrderDetail(
        orderId: String
    ): Flow<OrderDetail?> =
        orderDao
            .observeOrderWithItems(orderId)
            .map { relation ->
                relation?.toOrderDetail()
            }

    override suspend fun deleteOrder(id: String) {
        orderDao.deleteById(id = id)
    }

    override suspend fun syncOrderDetail(orderId: String) {
        val remoteOrder =
            orderApi.getOrder(orderId) ?: return

        val remoteItems =
            orderItemApi.getOrderItems(orderId)

        database.withTransaction {
            orderDao.insert(remoteOrder.toEntity())

            orderItemDao.deleteByOrderId(orderId)

            orderItemDao.insertAll(
                remoteItems.mapNotNull { it?.toEntity() }
            )

        }
    }

}