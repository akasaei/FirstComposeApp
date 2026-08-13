package com.ali.firstcomposeapp.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.ali.firstcomposeapp.data.local.AppDatabase
import com.ali.firstcomposeapp.data.local.OrderEntity
import com.ali.firstcomposeapp.data.local.OrderItemEntity
import com.ali.firstcomposeapp.data.remote.api.OrderApi
import com.ali.firstcomposeapp.data.remote.api.OrderItemApi
import com.ali.firstcomposeapp.data.remote.dto.OrderDto
import com.ali.firstcomposeapp.data.remote.dto.OrderItemDto
import com.ali.firstcomposeapp.domain.model.OrderStatus
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith


@OptIn(ExperimentalCoroutinesApi::class)
class DefaultOrderRepositoryTest {

    private lateinit var database: AppDatabase

    private val orderApi = mockk<OrderApi>()
    private val orderItemApi = mockk<OrderItemApi>()

    private lateinit var repository: DefaultOrderRepository

    @Before
    fun setup() {


        val context =
            ApplicationProvider.getApplicationContext<Context>()

        database =
            Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()

        repository =
            DefaultOrderRepository(
                database = database,
                orderApi = orderApi,
                orderItemApi = orderItemApi
            )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun syncOrderDetail_when_remote_data_is_valid_updates_local_database() =
        runTest {

            val remoteOrder = OrderDto(
                id = "ORD-001",
                customer = "Ali",
                status = "PENDING",
                priority = 1,
                totalValue = 150.0
            )

            val remoteItem = OrderItemDto(
                id = "ITEM-001",
                orderId = "ORD-001",
                productName = "Keyboard",
                quantity = 2,
                unitPrice = 50.0
            )

            coEvery {
                orderApi.getOrder("ORD-001")
            } returns remoteOrder

            coEvery {
                orderItemApi.getOrderItems("ORD-001")
            } returns listOf(remoteItem)

            repository.syncOrderDetail("ORD-001")
            val storedOrder =
                database.orderDao().getById("ORD-001")

            assertEquals(
                "ORD-001",
                storedOrder?.id
            )

            assertEquals(
                "Ali",
                storedOrder?.customer
            )

            assertEquals(
                "PENDING",
                storedOrder?.status.toString()
            )
            val storedItems =
                database.orderItemDao().getItems("ORD-001")

            assertEquals(
                1,
                storedItems.size
            )

            assertEquals(
                "ITEM-001",
                storedItems.first().id
            )

            assertEquals(
                "Keyboard",
                storedItems.first().productName
            )
        }

    @Test
    fun syncOrderDetail_replaces_existing_order_items() =
        runTest {

            val orderId = "ORD-001"
            val localOrder = OrderEntity(
                id = orderId,
                customer = "Ali",
                status = OrderStatus.PENDING,
                priority = 1,
                totalValue = 100.0
            )

            database.orderDao().insert(localOrder)

            // Existing local item
            val oldItem = OrderItemEntity(
                id = "ITEM-OLD",
                orderId = orderId,
                productName = "Old Keyboard",
                quantity = 1,
                unitPrice = 40.0
            )

            database.orderItemDao().insertAll(
                listOf(oldItem)
            )

            // Remote data
            val remoteOrder = OrderDto(
                id = orderId,
                customer = "Ali",
                status = "PENDING",
                priority = 1,
                totalValue = 150.0
            )

            val newItem = OrderItemDto(
                id = "ITEM-NEW",
                orderId = orderId,
                productName = "New Keyboard",
                quantity = 2,
                unitPrice = 50.0
            )

            coEvery {
                orderApi.getOrder(orderId)
            } returns remoteOrder

            coEvery {
                orderItemApi.getOrderItems(orderId)
            } returns listOf(newItem)

            // Synchronize
            repository.syncOrderDetail(orderId)

            // Read local database
            val storedItems =
                database.orderItemDao().getItems(orderId)

            // Only the new remote item should remain
            assertEquals(
                1,
                storedItems.size
            )

            assertEquals(
                "ITEM-NEW",
                storedItems.first().id
            )

            assertEquals(
                "New Keyboard",
                storedItems.first().productName
            )
        }

    @Test
    fun syncOrderDetail_when_remote_order_is_null_keeps_existing_local_data() =
        runTest {

            val orderId = "ORD-001"

            // Existing local order
            val localOrder = OrderEntity(
                id = orderId,
                customer = "Ali",
                status = OrderStatus.PENDING,
                priority = 1,
                totalValue = 100.0
            )

            database.orderDao().insert(localOrder)

            // Existing local item
            val localItem = OrderItemEntity(
                id = "ITEM-OLD",
                orderId = orderId,
                productName = "Old Keyboard",
                quantity = 1,
                unitPrice = 40.0
            )

            database.orderItemDao().insertAll(
                listOf(localItem)
            )

            // Remote order does not exist
            coEvery {
                orderApi.getOrder(orderId)
            } returns null

            repository.syncOrderDetail(orderId)

            // Verify the local order is still there
            val storedOrder =
                database.orderDao().getById(orderId)

            assertEquals(
                orderId,
                storedOrder?.id
            )

            assertEquals(
                "Ali",
                storedOrder?.customer
            )

            // Verify the local item is still there
            val storedItems =
                database.orderItemDao().getItems(orderId)

            assertEquals(
                1,
                storedItems.size
            )

            assertEquals(
                "ITEM-OLD",
                storedItems.first().id
            )

            assertEquals(
                "Old Keyboard",
                storedItems.first().productName
            )
        }


    @Test
    fun syncOrderDetail_when_fetching_items_fails_keeps_existing_local_data() =
        runTest {

            val orderId = "ORD-001"

            // Existing local order
            val localOrder = OrderEntity(
                id = orderId,
                customer = "Ali",
                status = OrderStatus.PENDING,
                priority = 1,
                totalValue = 100.0
            )

            database.orderDao().insert(localOrder)

            // Existing local item
            val localItem = OrderItemEntity(
                id = "ITEM-OLD",
                orderId = orderId,
                productName = "Old Keyboard",
                quantity = 1,
                unitPrice = 40.0
            )

            database.orderItemDao().insertAll(
                listOf(localItem)
            )

            // Remote order succeeds
            val remoteOrder = OrderDto(
                id = orderId,
                customer = "Updated Ali",
                status = "COMPLETED",
                priority = 2,
                totalValue = 200.0
            )

            coEvery {
                orderApi.getOrder(orderId)
            } returns remoteOrder

            // But fetching items fails
            coEvery {
                orderItemApi.getOrderItems(orderId)
            } throws RuntimeException("Failed to fetch order items")

            // The exception should be propagated
            assertFailsWith<RuntimeException> {
                repository.syncOrderDetail(orderId)
            }

            // Verify the original order is untouched
            val storedOrder =
                database.orderDao().getById(orderId)

            assertEquals(
                "Ali",
                storedOrder?.customer
            )

            assertEquals(
                OrderStatus.PENDING,
                storedOrder?.status
            )

            assertEquals(
                100.0,
                storedOrder?.totalValue
            )

            // Verify the original item is untouched
            val storedItems =
                database.orderItemDao().getItems(orderId)

            assertEquals(
                1,
                storedItems.size
            )

            assertEquals(
                "ITEM-OLD",
                storedItems.first().id
            )

            assertEquals(
                "Old Keyboard",
                storedItems.first().productName
            )
        }

    @Test
    fun syncOrderDetail_when_remote_data_exists_replaces_existing_local_data() =
        runTest {

            val orderId = "ORD-001"

            // Existing local data
            val localOrder = OrderEntity(
                id = orderId,
                customer = "Ali",
                status = OrderStatus.PENDING,
                priority = 1,
                totalValue = 100.0
            )

            database.orderDao().insert(localOrder)

            val oldItem = OrderItemEntity(
                id = "ITEM-OLD",
                orderId = orderId,
                productName = "Old Keyboard",
                quantity = 1,
                unitPrice = 40.0
            )

            database.orderItemDao().insertAll(
                listOf(oldItem)
            )

            // New data returned by the server
            val remoteOrder = OrderDto(
                id = orderId,
                customer = "Ali Updated",
                status = "COMPLETED",
                priority = 2,
                totalValue = 250.0
            )

            val newItem = OrderItemDto(
                id = "ITEM-NEW",
                orderId = orderId,
                productName = "New Monitor",
                quantity = 2,
                unitPrice = 125.0
            )

            coEvery {
                orderApi.getOrder(orderId)
            } returns remoteOrder

            coEvery {
                orderItemApi.getOrderItems(orderId)
            } returns listOf(newItem)

            // Synchronize
            repository.syncOrderDetail(orderId)

            // Verify order was updated
            val storedOrder =
                database.orderDao().getById(orderId)

            assertEquals(
                "Ali Updated",
                storedOrder?.customer
            )

            assertEquals(
                OrderStatus.COMPLETED,
                storedOrder?.status
            )

            assertEquals(
                2,
                storedOrder?.priority
            )

            assertEquals(
                250.0,
                storedOrder?.totalValue
            )

            // Verify old item was removed and new item was inserted
            val storedItems =
                database.orderItemDao().getItems(orderId)

            assertEquals(
                1,
                storedItems.size
            )

            assertEquals(
                "ITEM-NEW",
                storedItems.first().id
            )

            assertEquals(
                "New Monitor",
                storedItems.first().productName
            )
        }

    @Test
    fun observeOrderDetail_emits_order_with_items() = runTest {

        val order = OrderEntity(
            id = "ORD-001",
            customer = "Ali",
            status = OrderStatus.PENDING,
            priority = 1,
            totalValue = 150.0
        )

        val item = OrderItemEntity(
            id = "ITEM-001",
            orderId = "ORD-001",
            productName = "Keyboard",
            quantity = 2,
            unitPrice = 50.0
        )

        database.orderDao().insert(order)

        database.orderItemDao().insertAll(
            listOf(item)
        )

        repository
            .observeOrderDetail("ORD-001")
            .test {

                val result = awaitItem()

                assertEquals(
                    "ORD-001",
                    result?.order?.id
                )

                assertEquals(
                    "Ali",
                    result?.order?.customer
                )

                assertEquals(
                    OrderStatus.PENDING,
                    result?.order?.status
                )

                assertEquals(
                    1,
                    result?.items?.size
                )

                assertEquals(
                    "ITEM-001",
                    result?.items?.first()?.id
                )

                assertEquals(
                    "Keyboard",
                    result?.items?.first()?.productName
                )

                cancelAndIgnoreRemainingEvents()
            }
    }

}