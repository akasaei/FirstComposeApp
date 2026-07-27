package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ali.firstcomposeapp.model.OrderUiState
import com.ali.firstcomposeapp.repository.OrderRepository
import com.ali.firstcomposeapp.viewmodel.event.OrderEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val repository = OrderRepository()
    private val _uiState =
        MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> =
        _uiState.asStateFlow()

    fun onEvent(event: OrderEvent) {
        when (event) {
            is OrderEvent.SetSimulation -> {
                _uiState.update { it.copy(simulateFailure = event.enabled) }
            }

            is OrderEvent.Refresh ->
                fetchOrders()
        }
    }

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            try {
                val orders = repository.fetchOrders(_uiState.value.simulateFailure)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        orders = orders
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }
}
