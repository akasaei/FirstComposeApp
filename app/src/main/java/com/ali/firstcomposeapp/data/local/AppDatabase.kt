package com.ali.firstcomposeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ali.firstcomposeapp.data.local.dao.OrderDao
import com.ali.firstcomposeapp.data.local.dao.OrderItemDao
import com.ali.firstcomposeapp.data.local.dao.RemoteKeysDao


@Database(
    entities = [
        OrderEntity::class,
        OrderItemEntity::class,
        RemoteKeysEntity::class
    ],
    version = 3,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao

    abstract fun remoteKeysDao(): RemoteKeysDao

}