package com.ali.firstcomposeapp.domain.sync

sealed interface SyncStatus {

    data object Idle : SyncStatus
    data object Syncing : SyncStatus
    data class Success(
        val syncedAt: Long
    ) : SyncStatus

    data class Failed(
        val message: String
    ) : SyncStatus
}