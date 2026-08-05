package com.ali.firstcomposeapp.domain.sync

sealed interface SyncStatus {

    data object Idle : SyncStatus
    data object Syncing : SyncStatus
    data object Success : SyncStatus

    data class Failed(
        val message: String
    ) : SyncStatus
}