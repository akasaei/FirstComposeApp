package com.ali.firstcomposeapp.model

data class OrderUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val orders: List<Order> = emptyList(),
    val error: String? = null,
    val refreshError: String? = null
)
