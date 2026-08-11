package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ali.firstcomposeapp.data.datastore.UserPreferencesRepository
import com.ali.firstcomposeapp.data.repository.OrderRepository
import com.ali.firstcomposeapp.domain.model.OrderDetailUiState
import com.ali.firstcomposeapp.domain.sync.SyncOrderDetailUseCase
import com.ali.firstcomposeapp.domain.sync.SyncStatus
import com.ali.firstcomposeapp.navigation.ARG_ORDER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing and providing data for the Order Detail screen.
 *
 * This ViewModel handles:
 * - Observing the specific order details from the [OrderRepository].
 * - Managing the synchronization process via [SyncOrderDetailUseCase].
 * - Combining data from multiple sources (repository, user preferences, and sync status) into a single [OrderDetailUiState].
 * - Automatically refreshing data upon initialization.
 *
 * @property syncOrderDetail The use case used to trigger a network synchronization of order details.
 */
@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    repository: OrderRepository,
    private val syncOrderDetail: SyncOrderDetailUseCase,
    preferencesRepository: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: String =
        checkNotNull(savedStateHandle[ARG_ORDER_ID]) {
            "Navigation argument orderId is missing"
        }

    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)
    val syncStatus = _syncStatus.asStateFlow()

    val uiState : StateFlow<OrderDetailUiState> =
        combine(
            repository.observeOrderDetail(orderId),
            preferencesRepository.lastSyncTime,
            syncStatus
        ) { detail, lastSync, syncStatus ->

            OrderDetailUiState(
                isLoading = syncStatus is SyncStatus.Syncing,
                selectedOrder = detail,
                lastSync = lastSync,
                syncStatus = syncStatus
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = OrderDetailUiState(isLoading = true)
            )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _syncStatus.value = SyncStatus.Syncing

            try {
                syncOrderDetail(orderId)

                _syncStatus.value =
                    SyncStatus.Success

            } catch (e: Throwable) {

                _syncStatus.value =
                    SyncStatus.Failed(
                        e.message ?: "Unknown error"
                    )
            }
        }
    }
}