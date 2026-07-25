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

    var simulateError by mutableStateOf(true)
        private set

    fun setSimulation(enabled: Boolean) {
        simulateError = enabled
    }
    var uiState by mutableStateOf(OrderUiState())
        private set

    init {
        fetchOrders()
    }

    fun fetchOrders() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val orders = repository.fetchOrders(simulateError)
                uiState = uiState.copy(
                    isLoading = false,
                    orders = orders
                )

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}
