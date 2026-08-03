package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ali.firstcomposeapp.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    val orders =
        repository
            .observePagedOrders()
            .cachedIn(viewModelScope)

    fun deleteOrder(id: String) {
        viewModelScope.launch {
            repository.deleteOrder(id)
        }
    }

}