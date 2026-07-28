package com.ali.firstcomposeapp.util

import com.ali.firstcomposeapp.model.OrderStatus
import java.text.NumberFormat
import java.util.Locale

fun OrderStatus.displayName(): String =
    when (this) {
        OrderStatus.PENDING -> "Pending"
        OrderStatus.IN_PROGRESS -> "In Progress"
        OrderStatus.COMPLETED -> "Completed"
        OrderStatus.FAILED -> "Failed"
        OrderStatus.CANCELLED -> "Cancelled"
    }

fun Int.priorityName(): String =
    when (this) {
        1 -> "High"
        2 -> "Medium"
        3 -> "Low"
        else -> "Unknown"
    }

fun Double.asCurrency(): String =
    NumberFormat.getCurrencyInstance(Locale.GERMANY)
        .format(this)