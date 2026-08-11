package com.ali.firstcomposeapp.domain.sync

import com.ali.firstcomposeapp.data.datastore.UserPreferencesRepository
import com.ali.firstcomposeapp.data.repository.OrderRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertFailsWith

class SyncOrderDetailUseCaseTest {

    private val repository = mockk<OrderRepository>()
    private val preferencesRepository = mockk<UserPreferencesRepository>()

    private lateinit var useCase: SyncOrderDetailUseCase

    @Before
    fun setup() {
        useCase = SyncOrderDetailUseCase(
            repository,
            preferencesRepository
        )
    }

    @Test
    fun invoke_syncsOrder_and_savesLastSyncTime() = runTest {
        coEvery {
            repository.syncOrderDetail("L1")
        } returns Unit

        coEvery {
            preferencesRepository.saveLastSyncTime(any())
        } returns Unit

        useCase(orderId = "L1")

        coVerify(exactly = 1) {
            repository.syncOrderDetail("L1")
        }

        coVerify(exactly = 1) {
            preferencesRepository.saveLastSyncTime(any())
        }
    }

    @Test
    fun invoke_whenSyncFails_doesNotSaveLastSyncTime() = runTest {

        coEvery {
            repository.syncOrderDetail(any())
        } throws IOException("Network error")

        assertFailsWith<IOException> {
            useCase("ORD-001")
        }

        coVerify(exactly = 1) {
            repository.syncOrderDetail("ORD-001")
        }

        coVerify(exactly = 0) {
            preferencesRepository.saveLastSyncTime(any())
        }
    }

    @Test
    fun invoke_whenSavingTimestampFails_propagatesException() = runTest {

        coEvery {
            repository.syncOrderDetail(any())
        } returns Unit

        coEvery {
            preferencesRepository.saveLastSyncTime(any())
        } throws IOException()

        assertFailsWith<IOException> {
            useCase("ORD-001")
        }

        coVerify {
            repository.syncOrderDetail(any())
        }

        coVerify {
            preferencesRepository.saveLastSyncTime(any())
        }
    }

}