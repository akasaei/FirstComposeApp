package com.ali.firstcomposeapp.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.ali.firstcomposeapp.data.repository.OrderRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class OrderViewModelTest {

    private val repository = mockk<OrderRepository>()

    private lateinit var viewModel: OrderViewModel

    @Before
    fun setup() {

        every {
            repository.observePagedOrders(any())
        } returns flowOf(PagingData.empty())

        viewModel = OrderViewModel(repository)
    }

    @Test
    fun searchQuery_updates_when_user_types() = runTest {
        viewModel.searchQuery.test {

            assertEquals("", awaitItem())

            viewModel.onSearchQueryChanged("Telia")

            assertEquals("Telia", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteOrder_calls_repository() = runTest {

        coEvery {
            repository.deleteOrder(any())
        } returns Unit

        viewModel.deleteOrder("ORD-001")

        advanceUntilIdle()

        coVerify(exactly = 1) {
            repository.deleteOrder("ORD-001")
        }

    }

}