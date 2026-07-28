package com.ali.firstcomposeapp.data.repository

import com.ali.firstcomposeapp.data.local.OrderEntity
import com.ali.firstcomposeapp.data.local.dao.OrderDao
import com.ali.firstcomposeapp.data.mapper.toOrder
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderStatus


private val sampleOrders = listOf(
    OrderEntity("ORD-001", "Telia", OrderStatus.PENDING, 1, 1200.0),
    OrderEntity("ORD-002", "Elisa", OrderStatus.COMPLETED, 2, 850.0),
    OrderEntity("ORD-003", "DNA", OrderStatus.IN_PROGRESS, 1, 2200.0),
    OrderEntity("ORD-004", "Telia", OrderStatus.FAILED, 3, 500.0),
    OrderEntity("ORD-005", "Elisa", OrderStatus.PENDING, 2, 1500.0),
    OrderEntity("ORD-006", "DNA", OrderStatus.COMPLETED, 1, 3000.0)
)

class OrderRepository(private val orderDao: OrderDao) {
    suspend fun getOrders(): List<Order> {

        seedDatabase()

        return orderDao
            .getAll()
            .map { it.toOrder() }
    }

    suspend fun seedDatabase() {
        if (orderDao.getAll().isEmpty()) {
            orderDao.insertAll(sampleOrders)
        }
    }

    suspend fun getOrder(id: String?): Order? {

        return orderDao
            .getById(id)
            ?.toOrder()
    }
}


