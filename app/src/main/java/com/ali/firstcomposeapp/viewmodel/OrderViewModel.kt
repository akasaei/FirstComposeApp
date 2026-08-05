package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ali.firstcomposeapp.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel responsible for managing order-related data and UI logic.
 *
 * This class provides a reactive stream of paged orders and functionality to
 * perform operations such as deleting an order, interacting directly with
 * the [OrderRepository].
 *
 * @param repository The repository used to handle data operations for orders.
 */
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