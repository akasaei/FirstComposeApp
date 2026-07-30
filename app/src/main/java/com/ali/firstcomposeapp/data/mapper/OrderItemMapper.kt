package com.ali.firstcomposeapp.data.mapper

import com.ali.firstcomposeapp.data.local.OrderItemEntity
import com.ali.firstcomposeapp.model.OrderItem

fun OrderItemEntity.toOrderItem() =
    OrderItem(
        id = id,
        productName = productName,
        quantity = quantity,
        unitPrice = unitPrice
    )

fun OrderItem.toEntity(orderId: String) =
    OrderItemEntity(
        id = id,
        orderId = orderId,
        productName = productName,
        quantity = quantity,
        unitPrice = unitPrice
    )


