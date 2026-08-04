package com.ali.firstcomposeapp.domain.sync

import com.ali.firstcomposeapp.data.repository.OrderRepository
import javax.inject.Inject

class SyncOrderDetailUseCase @Inject constructor(
    private val repository: OrderRepository
) {

    suspend operator fun invoke(
        orderId: String
    ) {
        repository.syncOrderDetail(orderId)
    }
}