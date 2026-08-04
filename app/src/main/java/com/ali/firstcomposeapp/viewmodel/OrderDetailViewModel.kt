package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ali.firstcomposeapp.data.datastore.UserPreferencesRepository
import com.ali.firstcomposeapp.domain.model.OrderDetailUiState
import com.ali.firstcomposeapp.data.repository.OrderRepository
import com.ali.firstcomposeapp.domain.sync.SyncOrderDetailUseCase
import com.ali.firstcomposeapp.domain.sync.SyncStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val repository: OrderRepository,
    private val syncOrderDetail: SyncOrderDetailUseCase,
    private val preferencesRepository: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: String =
        checkNotNull(savedStateHandle["orderId"]) {
            "Navigation argument orderId is missing"
        }
    private val _uiState =
        MutableStateFlow(OrderDetailUiState())

    val uiState: StateFlow<OrderDetailUiState> =
        _uiState.asStateFlow()

    init {
        observeLastSync()
        observeOrderDetail()
        refresh()

    }

    private fun observeLastSync() {
        viewModelScope.launch {
            preferencesRepository.lastSyncTime.collect { timestamp ->
                _uiState.update {
                    it.copy(
                        lastSync = timestamp
                    )
                }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    syncStatus = SyncStatus.Syncing
                )
            }
            try {
                syncOrderDetail(orderId)
                _uiState.update {
                    it.copy(
                        syncStatus = SyncStatus.Success
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        syncStatus = SyncStatus.Failed(
                            e.message ?: "Unknown error"
                        )
                    )
                }
            }
        }
    }

    private fun observeOrderDetail() {
        viewModelScope.launch {
            repository
                .observeOrderDetail(orderId)
                .onStart {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            syncStatus = SyncStatus.Failed(
                                exception.message ?: "Unknown error"
                            )
                        )
                    }
                }
                .collect { detail ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            selectedOrder = detail
                        )
                    }
                    if (detail == null) {
                        _uiState.update {
                            it.copy(syncStatus = SyncStatus.Failed("Order not found"))
                        }
                    }
                }
        }
    }
}