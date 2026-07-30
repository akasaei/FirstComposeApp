package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ali.firstcomposeapp.data.repository.OrderRepository
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderUiState
import com.ali.firstcomposeapp.viewmodel.event.OrderEvent
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
            repository
                .observeOrders()
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
                            error = exception.message ?: "Unknown error"
                        )
                    }
                }
                .collect { orders ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            orders = orders
                        )
                    }
                }
        }
    }

    fun addOrder(order: Order) {
        viewModelScope.launch {
            try {
                repository.addOrder(order)
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        error = exception.message
                            ?: "Unable to add order"
                    )
                }
            }
        }
    }

    fun deleteOrder(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteOrder(id)
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        error = exception.message
                            ?: "Unable to delete order"
                    )
                }
            }
        }
    }
}
