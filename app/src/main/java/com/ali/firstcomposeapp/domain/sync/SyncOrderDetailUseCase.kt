package com.ali.firstcomposeapp.domain.sync

import com.ali.firstcomposeapp.data.datastore.UserPreferencesRepository
import com.ali.firstcomposeapp.data.repository.OrderRepository
import javax.inject.Inject

class SyncOrderDetailUseCase @Inject constructor(
    private val repository: OrderRepository,
    private val preferencesRepository: UserPreferencesRepository
) {

    suspend operator fun invoke(
        orderId: String
    ) {
        repository.syncOrderDetail(orderId)
        preferencesRepository.saveLastSyncTime(
            timestamp = System.currentTimeMillis()
        )
    }
}