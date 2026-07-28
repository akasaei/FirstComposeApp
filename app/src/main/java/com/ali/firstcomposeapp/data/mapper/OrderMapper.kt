@file:Suppress("unused")

package com.ali.firstcomposeapp.data.mapper

import com.ali.firstcomposeapp.data.local.OrderEntity
import com.ali.firstcomposeapp.model.Order


fun OrderEntity.toOrder(): Order = Order(
    id = id,
    customer = customer,
    status = status,
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