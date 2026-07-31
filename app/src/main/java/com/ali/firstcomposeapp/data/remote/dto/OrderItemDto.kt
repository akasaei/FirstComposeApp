package com.ali.firstcomposeapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderItemDto(
    val id: String,
    val orderId: String,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double
)