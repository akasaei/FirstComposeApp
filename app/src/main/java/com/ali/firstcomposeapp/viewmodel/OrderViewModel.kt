package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val repository = OrderRepository()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchOrders()
    }

    fun fetchOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _orders.value = repository.fetchOrders()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
