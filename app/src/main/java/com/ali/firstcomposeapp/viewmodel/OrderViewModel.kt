package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ali.firstcomposeapp.data.repository.OrderRepository
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.viewmodel.event.OrderEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    private val _refreshError =
        MutableStateFlow<String?>(null)

    val refreshError: StateFlow<String?> =
        _refreshError.asStateFlow()

    val orders: Flow<PagingData<Order>> =
        repository
            .observePagedOrders()
            .cachedIn(viewModelScope)

    init {
        refreshOrders()
    }

    fun refreshOrders() {
        viewModelScope.launch {
            runCatching {
                repository.refreshOrders()
            }.onFailure { exception ->
                _refreshError.value =
                    exception.message ?: "Unable to refresh orders"
            }
        }
    }

    fun deleteOrder(id: String) {
        viewModelScope.launch {
            repository.deleteOrder(id)
        }
    }

    fun onEvent(event: OrderEvent) {
        when (event) {
            is OrderEvent.Refresh ->
                refreshOrders()

        }
    }
}
