package com.ali.firstcomposeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ali.firstcomposeapp.domain.model.OrderStatus

@Entity(tableName = "orders")
data class OrderEntity(

    @PrimaryKey
    val id: String,

    val customer: String,

    val status: OrderStatus,

    val priority: Int,

    val totalValue: Double
)