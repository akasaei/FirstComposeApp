package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ali.firstcomposeapp.data.repository.OrderRepository
import com.ali.firstcomposeapp.model.OrderUiState
import com.ali.firstcomposeapp.viewmodel.event.OrderEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> =
        _uiState.asStateFlow()

    fun onEvent(event: OrderEvent) {
        when (event) {

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
                val orders = repository.getOrders()
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
