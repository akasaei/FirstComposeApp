package com.ali.firstcomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ali.firstcomposeapp.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds


/**
 * ViewModel responsible for managing order-related data and UI logic.
 *
 * This class provides a reactive stream of paged orders and functionality to
 * perform operations such as deleting an order, interacting directly with
 * the [OrderRepository].
 *
 * @param repository The repository used to handle data operations for orders.
 */
@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class
)
@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")

    val searchQuery = _searchQuery.asStateFlow()

    val orders =
        searchQuery
            .debounce(300.milliseconds)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                repository.observePagedOrders(query)
            }
            .cachedIn(viewModelScope)

    fun deleteOrder(id: String) {
        viewModelScope.launch {
            repository.deleteOrder(id)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

}