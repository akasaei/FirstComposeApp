package com.ali.firstcomposeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ali.firstcomposeapp.data.local.DatabaseProvider
import com.ali.firstcomposeapp.data.repository.OrderRepository
import com.ali.firstcomposeapp.model.OrderUiState
import com.ali.firstcomposeapp.viewmodel.event.OrderEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val database = DatabaseProvider.getDatabase(application)

    private val repository = OrderRepository(
        database.orderDao(),
        orderItemDao = database.orderItemDao()
    )
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
