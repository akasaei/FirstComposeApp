package com.ali.firstcomposeapp.domain.model

data class Order(
    val id: String,
    val customer: String,
    val status: OrderStatus,
    val priority: Int,
    val totalValue: Double
)