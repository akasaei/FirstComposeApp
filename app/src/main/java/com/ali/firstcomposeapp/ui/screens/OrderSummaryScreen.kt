package com.ali.firstcomposeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderStatus
import com.ali.firstcomposeapp.ui.components.DeleteRow
import com.ali.firstcomposeapp.ui.components.TableCell
import com.ali.firstcomposeapp.ui.components.TableHeaderCell
import com.ali.firstcomposeapp.ui.theme.Blue80
import com.ali.firstcomposeapp.ui.theme.FirstComposeAppTheme
import com.ali.firstcomposeapp.util.asCurrency
import com.ali.firstcomposeapp.viewmodel.OrderViewModel
import com.ali.firstcomposeapp.viewmodel.event.OrderEvent
import androidx.paging.LoadState
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.flowOf

@Composable
fun OrderSummaryScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = hiltViewModel(),
    onOrderDetailClick: (String) -> Unit,
    onDeleteOrder: (String) -> Unit
) {
    val orders = viewModel.orders.collectAsLazyPagingItems()

    val refreshError by viewModel.refreshError.collectAsStateWithLifecycle()

    OrderSummaryContent(
        orders = orders,
        modifier = modifier,
        refreshError = refreshError,
        refresh = {
            viewModel.onEvent(OrderEvent.Refresh)
        },
        onOrderDetailClick = onOrderDetailClick,
        onDeleteOrderClick = onDeleteOrder
    )
}

@Composable
fun OrderSummaryContent(
    orders: LazyPagingItems<Order>,
    refreshError: String?,
    refresh: () -> Unit,
    modifier: Modifier = Modifier,
    onOrderDetailClick: (String) -> Unit,
    onDeleteOrderClick: (String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(refreshError) {
        refreshError?.let {
            snackbarHostState.showSnackbar(message = it)
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(48.dp))
                Row {

                    Text(
                        text = "Order Summary",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(20.dp)
                            .weight(.8f),
                        textAlign = TextAlign.Center,
                    )
                    TextButton(
                        onClick = refresh, modifier = Modifier
                            .padding(top = 20.dp, end = 10.dp)
                            .weight(.2f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Order Summary",
                            tint = MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier
                                .size(28.dp)
                                .padding(0.dp)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            OrderList(
                orders,
                onOrderDetailClick = onOrderDetailClick,
                onDeleteOrderClick = onDeleteOrderClick
            )
            when (
                val refreshState =
                    orders.loadState.refresh
            ) {
                is LoadState.Loading -> {
                    if (orders.itemCount == 0) {
                        LoadingScreen()
                    }
                }

                is LoadState.Error -> {
                    if (orders.itemCount == 0) {
                        ErrorScreen(
                            errorMessage = refreshState
                                .error
                                .message
                                ?: "Unable to load more orders",
                            onRetry = orders::retry
                        )
                    }
                }

                is LoadState.NotLoading -> Unit
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = modifier.size(64.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = modifier.height(16.dp))
        Text(
            text = "Fetching orders...",
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ErrorScreen(
    errorMessage: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Something went wrong!",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
        TextButton(
            onClick = onRetry
        ) {
            Text("Retry")
        }
    }
}

@Composable
fun OrderList(
    orders: LazyPagingItems<Order>,
    onOrderDetailClick: (String) -> Unit,
    onDeleteOrderClick: (String) -> Unit
) {
    val columnWeight3 = .3f
    val columnWeight6 = .6f
    val columnWeight7 = .7f
    val columnWeight1 = .1f
    val columnWeight4 = .4f


    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(
                Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    )
            ) {
                TableHeaderCell(
                    text = "Id",
                    weight = columnWeight3,
                    shape = RoundedCornerShape(topStart = 10.dp)
                )
                TableHeaderCell(
                    text = "Customer",
                    weight = columnWeight3
                )
                TableHeaderCell(
                    text = "Value",
                    weight = columnWeight3
                )
                TableHeaderCell(
                    text = " ",
                    weight = columnWeight1,
                    shape = RoundedCornerShape(topEnd = 10.dp)
                )
            }
        }

        items(
            count = orders.itemCount,
            key = orders.itemKey { order ->
                order.id
            }) { index ->
            val currentOrder = orders[index]
            Row(
                Modifier
                    .fillMaxWidth()
                    .border(1.dp, Blue80)
            ) {
                if (currentOrder != null) {
                    TableCell(
                        text = currentOrder.id,
                        weight = columnWeight3,
                        modifier = Modifier.clickable(onClick = { onOrderDetailClick(currentOrder.id) })
                    )
                    TableCell(text = currentOrder.customer, weight = columnWeight3)
                    TableCell(text = currentOrder.totalValue.asCurrency(), weight = columnWeight3)
                    DeleteRow(
                        weight = columnWeight1,
                        modifier = Modifier.clickable(onClick = { onDeleteOrderClick(currentOrder.id) })
                    )
                }
            }
        }
        when (
            val appendState =
                orders.loadState.append
        ) {
            is LoadState.Loading -> {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 0.dp)
                    )
                }
            }

            is LoadState.Error -> {
                item {
                    ErrorScreen(
                        errorMessage = appendState
                            .error
                            .message
                            ?: "Unable to load more orders",
                        onRetry = orders::retry
                    )
                }
            }

            is LoadState.NotLoading -> Unit
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OrderSummaryPreview() {
    val fakeOrders = listOf(
        Order("1", "John Doe", OrderStatus.COMPLETED, 1, 100.0),
        Order("2", "Jane Smith", OrderStatus.PENDING, 2, 250.5),
        Order("3", "Ali", OrderStatus.IN_PROGRESS, 1, 500.0)
    )
    val pagingData = PagingData.from(fakeOrders)
    val orders = flowOf(pagingData).collectAsLazyPagingItems()

    FirstComposeAppTheme {
        OrderSummaryContent(
            orders = orders,
            refreshError = null,
            refresh = {},
            onOrderDetailClick = {},
            onDeleteOrderClick = {}
        )
    }
}
