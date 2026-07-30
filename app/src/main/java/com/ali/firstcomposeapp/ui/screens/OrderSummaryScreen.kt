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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderStatus
import com.ali.firstcomposeapp.model.OrderUiState
import com.ali.firstcomposeapp.ui.components.DeleteRow
import com.ali.firstcomposeapp.ui.components.TableCell
import com.ali.firstcomposeapp.ui.components.TableHeaderCell
import com.ali.firstcomposeapp.ui.theme.Blue80
import com.ali.firstcomposeapp.ui.theme.FirstComposeAppTheme
import com.ali.firstcomposeapp.util.asCurrency
import com.ali.firstcomposeapp.viewmodel.OrderViewModel
import com.ali.firstcomposeapp.viewmodel.event.OrderEvent

@Composable
fun OrderSummaryScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = hiltViewModel(),
    onOrderDetailClick : (String) -> Unit,
    onDeleteOrder: (String) -> Unit
) {
    val uiState by
    viewModel.uiState.collectAsStateWithLifecycle()

    OrderSummaryContent(
        uiState = uiState,
        modifier = modifier,
        refresh = {
            viewModel.onEvent(OrderEvent.Refresh)
        },
        onOrderDetailClick = onOrderDetailClick,
        onDeleteOrderClick = onDeleteOrder
    )
}

@Composable
fun OrderSummaryContent(
    uiState: OrderUiState,
    refresh: () -> Unit,
    modifier: Modifier = Modifier,
    onOrderDetailClick : (String) -> Unit,
    onDeleteOrderClick : (String) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxWidth(),
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "Order Summary",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp))
                Row {
                    TextButton(
                        onClick = refresh, modifier = Modifier
                            .padding(start = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh ,
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
            when {
                uiState.isLoading -> LoadingScreen()
                uiState.error != null -> ErrorScreen(
                    errorMessage = uiState.error
                )

                else -> OrderList(uiState.orders, onOrderDetailClick  = onOrderDetailClick, onDeleteOrderClick = onDeleteOrderClick)
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Fetching orders...",
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ErrorScreen(
    errorMessage: String
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
    }
}

@Composable
fun OrderList(
    orders: List<Order>,
    onOrderDetailClick: (String) -> Unit,
    onDeleteOrderClick: (String) -> Unit
) {
    val columnWeight3 = .3f
    val columnWeight7 = .7f
    val columnWeight1 = .1f

    val totalValue = orders.sumOf { it.totalValue }

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
                    text = "X",
                    weight = columnWeight1,
                    shape = RoundedCornerShape(topEnd = 10.dp)
                )
            }
        }
        items(items = orders, key = { it.id }) { currentOrder ->
            Row(Modifier
                .fillMaxWidth()
                .border(1.dp, Blue80)
                ) {

                TableCell(text = currentOrder.id, weight = columnWeight3, modifier = Modifier.clickable(onClick = { onOrderDetailClick(currentOrder.id) }))
                TableCell(text = currentOrder.customer, weight = columnWeight3)
                TableCell(text = currentOrder.totalValue.asCurrency(), weight = columnWeight3)
                DeleteRow( weight = columnWeight1, modifier = Modifier.clickable(onClick = {onDeleteOrderClick(currentOrder.id)}))
            }
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                    )
            ) {
                TableCell(text = "Total", weight = columnWeight7)
                TableCell(text = totalValue.asCurrency(), weight = columnWeight3)
            }
        }
    }
}


@Preview(showBackground = true, apiLevel = 36)
@Composable
fun OrderSummaryPreview() {
    FirstComposeAppTheme {
        OrderSummaryContent(
            uiState = OrderUiState(
                orders = listOf(
                    Order("1", "John Doe", OrderStatus.COMPLETED, 1, 100.0),
                    Order("2", "Jane Smith", OrderStatus.PENDING, 2, 250.5),
                    Order("3", "Ali", OrderStatus.IN_PROGRESS, 1, 500.0)
                ),
                isLoading = false,
                error = null
            ),
            refresh = {},
            onOrderDetailClick = {},
            onDeleteOrderClick = {}
        )
    }
}
