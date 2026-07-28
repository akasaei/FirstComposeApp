package com.ali.firstcomposeapp.data.local

import androidx.room.TypeConverter
import com.ali.firstcomposeapp.model.OrderStatus

class Converters {

    @TypeConverter
    fun fromOrderStatus(status: OrderStatus): String {
        return status.name
    }

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus {
        return OrderStatus.valueOf(value)
    }
}