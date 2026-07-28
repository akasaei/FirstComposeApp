package com.ali.firstcomposeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ali.firstcomposeapp.data.local.OrderEntity

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders LIMIT :offset, :limit")
    suspend fun getAll(limit: Int = 10, offset: Int = 0): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getById(id: String?): OrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<OrderEntity>)

    @Query("DELETE FROM orders")
    suspend fun deleteAll()

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteById(id: String?)
}