package com.ali.firstcomposeapp.domain.model

data class OrderItem(
    val id: String,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double
)