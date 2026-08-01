package com.ali.firstcomposeapp.model

data class OrderUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val orders: List<Order> = emptyList(),
    val error: String? = null,
    val refreshError: String? = null,
    val currentPage: Int = 0,
    val pageSize: Int = 2,
    val hasMore: Boolean = true,
    val isLoadingMore: Boolean = false
)
