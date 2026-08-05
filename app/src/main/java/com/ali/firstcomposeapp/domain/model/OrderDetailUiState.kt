package com.ali.firstcomposeapp.domain.model

import com.ali.firstcomposeapp.domain.sync.SyncStatus

data class OrderDetailUiState (
        val selectedOrder: OrderDetail? = null,
        val isLoading: Boolean = false,
        val syncStatus: SyncStatus =
                SyncStatus.Idle,
        val lastSync: Long? = 0L
)