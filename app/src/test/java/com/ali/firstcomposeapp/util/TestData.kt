package com.ali.firstcomposeapp.util

import com.ali.firstcomposeapp.domain.model.Order
import com.ali.firstcomposeapp.domain.model.OrderDetail
import com.ali.firstcomposeapp.domain.model.OrderItem
import com.ali.firstcomposeapp.domain.model.OrderStatus

fun fakeOrder(
    id: String = "ORD-001",
    customer: String = "Ali",
    status: OrderStatus = OrderStatus.PENDING,
    priority: Int = 1,
    totalValue: Double = 150.0
) = Order(
    id = id,
    customer = customer,
    status = status,
    priority = priority,
    totalValue = totalValue
)

fun fakeOrderItem(
    id: String = "ITEM-001",
    productName: String = "Keyboard",
    quantity: Int = 2,
    unitPrice: Double = 50.0
) = OrderItem(
    id = id,
    productName = productName,
    quantity = quantity,
    unitPrice = unitPrice
)

fun fakeOrderDetail(
    order: Order = fakeOrder(),
    items: List<OrderItem> = listOf(fakeOrderItem())
) = OrderDetail(
    order = order,
    items = items
)