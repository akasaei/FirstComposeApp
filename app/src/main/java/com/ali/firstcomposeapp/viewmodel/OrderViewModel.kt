package com.ali.firstcomposeapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ali.firstcomposeapp.model.OrderUiState
import com.ali.firstcomposeapp.repository.OrderRepository
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val repository = OrderRepository()

    var uiState by mutableStateOf(OrderUiState())
        private set

    init {
        fetchOrders()
    }

    fun fetchOrders() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val orders = repository.fetchOrders(uiState.simulatorError)
                uiState = uiState.copy(
                    isLoading = false,
                    orders = orders,
                    simulatorError = true
                )

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error",
                    simulatorError = false
                )
            }
        }
    }
}
