package com.ali.firstcomposeapp.model

data class OrderItem(
    val id: String,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double
)