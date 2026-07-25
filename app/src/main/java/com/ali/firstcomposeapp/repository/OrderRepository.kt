package com.ali.firstcomposeapp.repository

import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderStatus
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


private val orders = listOf(
    Order("ORD-001", "Telia", OrderStatus.PENDING, 1, 1200.0),
    Order("ORD-002", "Elisa", OrderStatus.COMPLETED, 2, 850.0),
    Order("ORD-003", "DNA", OrderStatus.IN_PROGRESS, 1, 2200.0),
    Order("ORD-004", "Telia", OrderStatus.FAILED, 3, 500.0),
    Order("ORD-005", "Elisa", OrderStatus.PENDING, 2, 1500.0),
    Order("ORD-006", "DNA", OrderStatus.COMPLETED, 1, 3000.0)
)

class OrderRepository {

    suspend fun fetchOrders(
        simulateError: Boolean = false
    ): List<Order> {
        delay(2.seconds)
        if (simulateError) {
            throw Exception("Network unavailable")
        } else {
            return orders
        }

    }


}