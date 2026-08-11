package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.ali.firstcomposeapp.data.datastore.UserPreferencesRepository
import com.ali.firstcomposeapp.data.repository.OrderRepository
import com.ali.firstcomposeapp.domain.model.OrderDetail
import com.ali.firstcomposeapp.domain.sync.SyncOrderDetailUseCase
import com.ali.firstcomposeapp.domain.sync.SyncStatus
import com.ali.firstcomposeapp.navigation.ARG_ORDER_ID
import com.ali.firstcomposeapp.util.MainDispatcherRule
import com.ali.firstcomposeapp.util.fakeOrderDetail
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class OrderDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<OrderRepository>()

    private val syncUseCase =
        mockk<SyncOrderDetailUseCase>()

    private val preferences =
        mockk<UserPreferencesRepository>()

    private lateinit var viewModel: OrderDetailViewModel

    private val orderFlow =
        MutableStateFlow<OrderDetail?>(null)

    private val lastSyncFlow =
        MutableStateFlow<Long?>(null)

    @Before
    fun setup() {

        every {
            repository.observeOrderDetail(any())
        } returns orderFlow

        every {
            preferences.lastSyncTime
        } returns lastSyncFlow


    }

    @Test
    fun refresh_when_sync_succeeds_sets_success() = runTest {

        coEvery {
            syncUseCase("ORD-001")
        } returns Unit

        val savedStateHandle =
            SavedStateHandle(
                mapOf(
                    ARG_ORDER_ID to "ORD-001"
                )
            )

        viewModel =
            OrderDetailViewModel(
                repository,
                syncUseCase,
                preferences,
                savedStateHandle
            )

        backgroundScope.launch {
            viewModel.uiState.collect {}
        }

        advanceUntilIdle()

        viewModel.refresh()

        advanceUntilIdle()

        assertEquals(
            SyncStatus.Success,
            viewModel.uiState.value.syncStatus
        )

        coVerify(atLeast = 1) {
            syncUseCase("ORD-001")
        }
    }

    @Test
    fun refresh_when_sync_fails_sets_failed() = runTest {

        coEvery {
            syncUseCase("ORD-001")
        } throws RuntimeException("Network error")


        val savedStateHandle =
            SavedStateHandle(
                mapOf(
                    ARG_ORDER_ID to "ORD-001"
                )
            )

        viewModel =
            OrderDetailViewModel(
                repository,
                syncUseCase,
                preferences,
                savedStateHandle
            )

        backgroundScope.launch {
            viewModel.uiState.collect {}
        }

        advanceUntilIdle()

        viewModel.refresh()

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(
            SyncStatus.Failed("Network error"),
            state.syncStatus
        )

        coVerify(atLeast = 1) {
            syncUseCase("ORD-001")
        }
    }


    @Test
    fun uiState_updates_when_repository_emits_order() = runTest {


        val savedStateHandle =
            SavedStateHandle(
                mapOf(
                    ARG_ORDER_ID to "ORD-001"
                )
            )

        viewModel =
            OrderDetailViewModel(
                repository,
                syncUseCase,
                preferences,
                savedStateHandle
            )

        val detail = fakeOrderDetail()

        viewModel.uiState.test {

            // Consume the initial state
            awaitItem()

            // Simulate Room emitting the order
            orderFlow.value = detail

            // Wait for the ViewModel coroutine to process it
            advanceUntilIdle()

            val updatedState = awaitItem()

            assertEquals(
                detail,
                updatedState.selectedOrder
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}