package com.ali.firstcomposeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ali.firstcomposeapp.data.local.OrderItemEntity

/**
 * Data Access Object (DAO) for managing [OrderItemEntity] operations in the local database.
 *
 * This interface provides methods for retrieving, inserting, and deleting items
 * associated with specific orders, ensuring data persistence for order line items.
 */
@Dao
interface OrderItemDao {

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getItems(orderId: String): List<OrderItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<OrderItemEntity>)

    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    suspend fun deleteByOrderId(orderId: String)

}