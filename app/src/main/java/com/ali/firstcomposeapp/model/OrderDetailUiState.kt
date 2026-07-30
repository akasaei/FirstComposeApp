package com.ali.firstcomposeapp.model

data class OrderDetailUiState (
        val isLoading: Boolean = false,
        val selectedOrder: OrderDetail? = null,
        val error: String? = null
)