package com.ali.firstcomposeapp.data.mapper

import com.ali.firstcomposeapp.data.local.OrderItemEntity
import com.ali.firstcomposeapp.data.remote.dto.OrderItemDto
import com.ali.firstcomposeapp.domain.model.OrderItem

fun OrderItemDto.toEntity(): OrderItemEntity =
    OrderItemEntity(
        id = id,
        orderId = orderId,
        productName = productName,
        quantity = quantity,
        unitPrice = unitPrice
    )

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


