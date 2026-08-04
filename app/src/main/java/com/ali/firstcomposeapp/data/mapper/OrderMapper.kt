@file:Suppress("unused")

package com.ali.firstcomposeapp.data.mapper

import com.ali.firstcomposeapp.data.local.OrderEntity
import com.ali.firstcomposeapp.data.remote.dto.OrderDto
import com.ali.firstcomposeapp.domain.model.Order
import com.ali.firstcomposeapp.domain.model.OrderStatus


fun OrderEntity.toOrder(): Order = Order(
    id = id,
    customer = customer,
    status = status,
    priority = priority,
    totalValue = totalValue
)

fun OrderDto.toOrder(): Order = Order(
    id = id,
    customer = customer,
    status = runCatching {
        OrderStatus.valueOf(status.uppercase())
    }.getOrDefault(OrderStatus.FAILED),
    priority = priority,
    totalValue = totalValue
)

fun OrderDto.toEntity(): OrderEntity =
    OrderEntity(
        id = id,
        customer = customer,
        status = runCatching {
            OrderStatus.valueOf(status.uppercase())
        }.getOrDefault(OrderStatus.FAILED),
        priority = priority,
        totalValue = totalValue
    )

fun Order.toEntity(): OrderEntity =
    OrderEntity(
        id = id,
        customer = customer,
        status = status,
        priority = priority,
        totalValue = totalValue
    )