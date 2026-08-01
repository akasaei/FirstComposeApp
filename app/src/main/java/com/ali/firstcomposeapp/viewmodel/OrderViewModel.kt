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
import kotlinx.coroutines.flow.first
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

            is OrderEvent.NextPage ->
                loadNextPage()
        }
    }

    private fun refreshOrders() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isLoadingMore = false,
                    error = null,
                    isRefreshing = true,
                    refreshError = null
                )
            }

            try {
                loadFirstPage()
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
                            isLoadingMore = false,
                            isLoading = false,
                            isRefreshing = false,
                            error = exception.message
                                ?: "Unable to load orders"
                        )
                    } else {
                        state.copy(
                            isLoadingMore = false,
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
        refreshOrders()
    }

    fun loadFirstPage() {
        loadPage(page = 0, replace = true)
    }

    fun loadNextPage() {
        val state = _uiState.value

        if (state.isLoading || state.isLoadingMore || !state.hasMore) return

        loadPage(
            page = state.currentPage + 1,
            replace = false
        )
    }


    private fun loadPage(
        page: Int,
        replace: Boolean
    ) {
        viewModelScope.launch {
            _uiState.update {
                if (replace) it.copy(isLoading = true)
                else it.copy(isLoadingMore = true)
            }

            val state = _uiState.value
            try {
                val orders = repository
                    .getOrdersPage(
                        page = page,
                        pageSize = state.pageSize
                    )
                    .first()

                _uiState.update {
                    val newList = if (replace) orders else it.orders + orders
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        orders = newList.distinctBy { order -> order.id },
                        currentPage = page,
                        hasMore = repository.hasMore(
                            page = page,
                            pageSize = state.pageSize
                        ),
                        error = null
                    )
                }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = exception.message ?: "Failed to load page"
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
