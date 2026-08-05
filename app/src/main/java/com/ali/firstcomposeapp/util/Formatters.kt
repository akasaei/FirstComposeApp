package com.ali.firstcomposeapp.util

import com.ali.firstcomposeapp.domain.model.OrderStatus
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

/**
 * Formats this [Double] value as a currency string using the German locale ([Locale.GERMANY]).
 *
 * @return A string representation of the value formatted with the Euro symbol and German
 * formatting rules (e.g., 1.234,56 €).
 */
fun Double.asCurrency(): String =
    NumberFormat.getCurrencyInstance(Locale.GERMANY)
        .format(this)
