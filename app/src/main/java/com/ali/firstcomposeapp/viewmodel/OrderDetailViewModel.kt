package com.ali.firstcomposeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ali.firstcomposeapp.data.local.DatabaseProvider
import com.ali.firstcomposeapp.model.OrderDetailUiState
import com.ali.firstcomposeapp.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val database = DatabaseProvider.getDatabase(application)

    private val repository = OrderRepository(
        database.orderDao(),
        orderItemDao = database.orderItemDao()
    )

    private val _uiState =
        MutableStateFlow(OrderDetailUiState())

    val uiState: StateFlow<OrderDetailUiState> =
        _uiState.asStateFlow()

    fun fetchOrderDetail(orderId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            try {
                val orderWithItem = repository.getOrderDetail(orderId)
                if(orderWithItem?.order != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            selectedOrder = orderWithItem
                        )
                    }
                }else{
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Order Not Found"
                        )
                    }
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