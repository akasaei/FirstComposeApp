package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ali.firstcomposeapp.model.OrderDetailUiState
import com.ali.firstcomposeapp.data.repository.OrderRepository
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
        observeOrderDetail()
        refreshOrderDetail()

    }

    private fun refreshOrderDetail() {
        viewModelScope.launch {
            try {
                repository.syncOrderDetail(orderId)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message
                            ?: "Unable to load order"
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
                            isLoading = true,
                            error = null
                        )
                    }
                }
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Unable to load order"
                        )
                    }
                }
                .collect { detail ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            selectedOrder = detail,
                            error = if (detail == null) {
                                "Order not found"
                            } else {
                                null
                            }
                        )
                    }
                }
        }
    }

}