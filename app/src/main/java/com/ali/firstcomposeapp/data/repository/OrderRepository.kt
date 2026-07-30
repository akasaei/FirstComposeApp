@file:Suppress("unused")

package com.ali.firstcomposeapp.data.repository

import com.ali.firstcomposeapp.data.local.OrderEntity
import com.ali.firstcomposeapp.data.local.OrderItemEntity
import com.ali.firstcomposeapp.data.mapper.toEntity
import com.ali.firstcomposeapp.data.mapper.toOrder
import com.ali.firstcomposeapp.data.mapper.toOrderDetail
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderDetail
import com.ali.firstcomposeapp.model.OrderStatus
import com.ali.firstcomposeapp.data.local.dao.OrderDao
import com.ali.firstcomposeapp.data.local.dao.OrderItemDao
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) {

    private val sampleOrders = listOf(
        OrderEntity("ORD-001", "Telia", OrderStatus.PENDING, 1, 1200.0),
        OrderEntity("ORD-002", "Elisa", OrderStatus.COMPLETED, 2, 850.0),
        OrderEntity("ORD-003", "DNA", OrderStatus.IN_PROGRESS, 1, 2200.0),
        OrderEntity("ORD-004", "Telia", OrderStatus.FAILED, 3, 500.0),
        OrderEntity("ORD-005", "Elisa", OrderStatus.PENDING, 2, 1500.0),
        OrderEntity("ORD-006", "DNA", OrderStatus.COMPLETED, 1, 3000.0)
    )

    private val sampleOrderItems = listOf(
        OrderItemEntity(
            id = "ITEM-001",
            orderId = "ORD-001",
            productName = "SIM Card",
            quantity = 2,
            unitPrice = 20.0
        ),
        OrderItemEntity(
            id = "ITEM-002",
            orderId = "ORD-001",
            productName = "Installation",
            quantity = 1,
            unitPrice = 75.0
        ),
        OrderItemEntity(
            id = "ITEM-003",
            orderId = "ORD-002",
            productName = "Fiber Router",
            quantity = 1,
            unitPrice = 199.0
        ),
        OrderItemEntity(
            id = "ITEM-004",
            orderId = "ORD-003",
            productName = "eSIM Activation",
            quantity = 5,
            unitPrice = 5.0
        )
    )

    suspend fun getOrders(): List<Order> {

        seedDatabase()

        return orderDao
            .getAll()
            .map(OrderEntity::toOrder)
    }

    private suspend fun seedDatabase() {

        if (orderDao.count() == 0) {
            orderDao.insertAll(sampleOrders)
            orderItemDao.insertAll(sampleOrderItems)
        }
    }

    suspend fun getOrder(id: String): Order? {
        seedDatabase()

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

    suspend fun getOrderDetail(
        id: String
    ): OrderDetail? {

        seedDatabase()

        return orderDao
            .getOrderWithItems(id)?.toOrderDetail()

    }
}


