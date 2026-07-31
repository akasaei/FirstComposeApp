package com.ali.firstcomposeapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: String,
    val customer: String,
    val status: String,
    val priority: Int,
    val totalValue: Double
)