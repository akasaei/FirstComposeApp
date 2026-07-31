package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ali.firstcomposeapp.data.repository.OrderRepository
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderUiState
import com.ali.firstcomposeapp.viewmodel.event.OrderEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
                refreshOrders()
        }
    }

    private fun refreshOrders() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isRefreshing = true,
                    refreshError = null
                )
            }

            try {
                repository.refreshOrders()

                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        refreshError = null
                    )
                }

            } catch (exception: Exception) {

                _uiState.update { state ->

                    if (state.orders.isEmpty()) {
                        state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = exception.message
                                ?: "Unable to load orders"
                        )
                    } else {
                        state.copy(
                            isRefreshing = false,
                            refreshError = exception.message
                                ?: "Unable to refresh orders"
                        )
                    }
                }
            }
        }
    }

    init {
        observeOrders()
        refreshOrders()
    }

    private fun observeOrders() {
        viewModelScope.launch {
            repository
                .observeOrders()
                .collect { orders ->

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            orders = orders,
                            error = null
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
