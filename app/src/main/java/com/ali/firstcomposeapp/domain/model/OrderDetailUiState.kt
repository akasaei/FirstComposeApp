package com.ali.firstcomposeapp.domain.model

import com.ali.firstcomposeapp.domain.sync.SyncStatus
import kotlin.time.Instant

data class OrderDetailUiState (
        val selectedOrder: OrderDetail? = null,
        val isLoading: Boolean = false,
        val syncStatus: SyncStatus =
                SyncStatus.Idle,
        val lastSync: Instant? = null
)