package com.ali.firstcomposeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ORDERS_REMOTE_KEY = "orders"

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey
    val label: String,

    val nextPage: Int?
)