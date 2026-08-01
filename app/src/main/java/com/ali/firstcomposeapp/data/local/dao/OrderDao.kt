package com.ali.firstcomposeapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ali.firstcomposeapp.data.local.OrderEntity
import com.ali.firstcomposeapp.data.local.OrderWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query(
        """
        SELECT *
        FROM orders
        ORDER BY id
        """
    )
    fun pagingSource(): PagingSource<Int, OrderEntity>

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
    @Query("SELECT * FROM orders WHERE id = :id")
    fun observeOrderWithItems(
        id: String
    ): Flow<OrderWithItems?>

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun count(): Int
}