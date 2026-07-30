package com.ali.firstcomposeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ali.firstcomposeapp.data.local.OrderEntity
import com.ali.firstcomposeapp.data.local.OrderWithItems

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders LIMIT :limit OFFSET :offset")
    suspend fun getAll(limit: Int = 10, offset: Int = 0): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getById(id: String): OrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orders: OrderEntity)

    @Query("DELETE FROM orders")
    suspend fun deleteAll()

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteById(id: String)

    @Transaction
    @Query("""
    SELECT *
    FROM orders
    WHERE id = :id
""")
    suspend fun getOrderWithItems(
        id: String
    ): OrderWithItems?

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun count(): Int
}