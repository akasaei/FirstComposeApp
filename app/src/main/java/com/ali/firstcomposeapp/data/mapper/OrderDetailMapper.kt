package com.ali.firstcomposeapp.data.mapper

import com.ali.firstcomposeapp.data.local.OrderWithItems
import com.ali.firstcomposeapp.model.OrderDetail

fun OrderWithItems.toOrderDetail() =
    OrderDetail(
        order = order.toOrder(),
        items = items.map { it.toOrderItem() }
    )