package com.ali.firstcomposeapp.model

data class OrderDetailUiState (
        val isLoading: Boolean = false,
        val order: Order? = null,
        val error: String? = null
)