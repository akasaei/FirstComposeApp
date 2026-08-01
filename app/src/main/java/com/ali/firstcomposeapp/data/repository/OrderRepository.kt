@file:Suppress("unused")

package com.ali.firstcomposeapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ali.firstcomposeapp.data.local.OrderEntity
import com.ali.firstcomposeapp.data.local.dao.OrderDao
import com.ali.firstcomposeapp.data.local.dao.OrderItemDao
import com.ali.firstcomposeapp.data.mapper.toEntity
import com.ali.firstcomposeapp.data.mapper.toOrder
import com.ali.firstcomposeapp.data.mapper.toOrderDetail
import com.ali.firstcomposeapp.data.remote.api.OrderApi
import com.ali.firstcomposeapp.data.remote.api.OrderItemApi
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao,
    private val orderApi: OrderApi,
    private val orderItemApi: OrderItemApi
) {

    companion object {
        private const val PAGE_SIZE = 10
    }

    fun observePagedOrders(): Flow<PagingData<Order>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE * 2,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                orderDao.pagingSource()
            }
        ).flow.map { pagingData ->
            pagingData.map(OrderEntity::toOrder)
        }


    suspend fun hasMore(
        page: Int,
        pageSize: Int
    ): Boolean {

        return orderDao.count() > ((page + 1) * pageSize)
    }

    suspend fun testRemoteOrders(): List<Order> {
        return orderApi.getOrders().map { order -> order.toOrder() }
    }

    suspend fun refreshOrders() {
        val remoteOrders = orderApi.getOrders()
        val remoteOrderItems = orderItemApi.getOrderItems()

        val entities = remoteOrders.map { it.toEntity() }
        val itemEntities = remoteOrderItems.map { it.toEntity() }

        orderDao.insertAll(entities)
        orderItemDao.insertAll(itemEntities)
    }


    suspend fun getOrder(id: String): Order? {

        return orderDao
            .getById(id)
            ?.toOrder()
    }

    suspend fun addOrder(order: Order) {
        orderDao.insert(order.toEntity())
    }

    suspend fun deleteOrder(id: String) {
        orderDao.deleteById(id = id)
    }

    fun observeOrderDetail(
        id: String
    ): Flow<OrderDetail?> =
        flow {

            emitAll(
                orderDao
                    .observeOrderWithItems(id)
                    .map { relation ->
                        relation?.toOrderDetail()
                    }
            )
        }
}


