package com.ali.firstcomposeapp.model

data class OrderDetail(
    val order: Order,
    val items: List<OrderItem>
){
    val totalItemCount: Int
        get() = items.sumOf { it.quantity }

    val totalItemValue: Double
        get() = items.sumOf { it.quantity * it.unitPrice }
}