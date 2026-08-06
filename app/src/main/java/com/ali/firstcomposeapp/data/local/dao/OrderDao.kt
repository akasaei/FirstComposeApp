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

/**
 * Data Access Object (DAO) for managing [OrderEntity] records in the local Room database.
 *
 * This interface provides methods for performing CRUD operations on the "orders" table,
 * supporting pagination, reactive updates via [Flow], and relational data retrieval
 * through [OrderWithItems].
 */
@Dao
interface OrderDao {

    @Query(
        "SELECT * FROM orders order by id"
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

    @Query("""
    SELECT *
    FROM orders
    WHERE
        (:query = '')
        OR id LIKE '%' || :query || '%'
        OR customer LIKE '%' || :query || '%'
    ORDER BY id
""")
    fun pagingSource(
        query: String
    ): PagingSource<Int, OrderEntity>
}