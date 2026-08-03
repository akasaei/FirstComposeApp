package com.ali.firstcomposeapp.ui.screens


import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderDetail
import com.ali.firstcomposeapp.model.OrderDetailUiState
import com.ali.firstcomposeapp.model.OrderItem
import com.ali.firstcomposeapp.model.OrderStatus
import com.ali.firstcomposeapp.ui.components.TableCell
import com.ali.firstcomposeapp.ui.components.TableHeaderCell
import com.ali.firstcomposeapp.ui.theme.FirstComposeAppTheme
import com.ali.firstcomposeapp.util.asCurrency
import com.ali.firstcomposeapp.util.displayName
import com.ali.firstcomposeapp.util.priorityName
import com.ali.firstcomposeapp.viewmodel.OrderDetailViewModel

const val columnWeight1 = .05f
const val columnWeight2 = .25f
const val columnWeight3 = .3f
const val columnWeight4 = .4f
const val columnWeight7 = .7f


@Composable
fun OrderDetailScreen(
    viewModel: OrderDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OrderDetailContent(
        uiState = uiState,
        onOrderSummary = onBackClick
    )

}


@Composable
fun OrderDetailContent(
    uiState: OrderDetailUiState,
    modifier: Modifier = Modifier,
    onOrderSummary: () -> Unit
) {

    Scaffold(
        modifier = modifier.fillMaxWidth(),
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(48.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Order Detail",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(columnWeight7)
                    )
                    TextButton(
                        onClick = onOrderSummary, modifier = Modifier
                            .padding(end = 20.dp, top = 0.dp)
                            .weight(.15f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
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
                uiState.selectedOrder != null -> {
                    OrderDetails(
                        selectedOrder = uiState.selectedOrder,
                        errorMessage = uiState.error,
                        modifier = modifier
                    )
                }

                uiState.error != null -> ErrorItemScreen(
                    errorMessage = uiState.error
                )
            }
        }
    }
}


@Composable
fun ErrorItemScreen(
    errorMessage: String,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    Column(
        modifier = modifier,
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
fun OrderDetails(
    selectedOrder: OrderDetail?,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    val order = selectedOrder?.order
    val orderItems = selectedOrder?.items
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(5.dp)
    ) {
        item {
            Text(
                text = "Order ${order?.id}",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (order != null) {
                        with(order) {
                            Text("Customer: ${this.customer}")
                            Text("Status: ${this.status.displayName()}")
                            Text("Priority: ${this.priority.priorityName()}")
                            Text("Total Value : ${this.totalValue.asCurrency()}")
                        }
                    }
                }
            }
        }
        item {
            Text(
                text = "Items (${selectedOrder?.totalItemCount})",
                style = MaterialTheme.typography.titleSmall
            )
            HorizontalDivider(modifier = Modifier.padding(bottom = 10.dp))
        }
        item {
            OrderItemCardHeader()
        }
        if (errorMessage != null) {
            item {
                ErrorItemScreen(
                    errorMessage = errorMessage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                )
            }
        } else if (!orderItems.isNullOrEmpty()) {
            items(
                items = orderItems,
                key = { it.id }) { currentOrderItem ->
                OrderItemCard(currentOrderItem)
            }
        } else {
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    TableCell(
                        text = "There is no additional detail available!",
                        weight = 1.0f,
                        modifier = Modifier
                    )
                }
            }
        }

        item {
            if (!orderItems.isNullOrEmpty()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                        )
                ) {
                    TableCell(text = "Total", weight = columnWeight7)
                    TableCell(
                        text = selectedOrder.totalItemValue.asCurrency(),
                        weight = columnWeight3
                    )
                }
            }
        }
    }
}

@Composable
fun OrderItemCardHeader() {
    Row(
        Modifier
            .fillMaxWidth()
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
            text = "Product",
            weight = columnWeight4
        )
        TableHeaderCell(
            text = "#",
            weight = columnWeight1
        )
        TableHeaderCell(
            text = "Price",
            weight = columnWeight2,
            shape = RoundedCornerShape(topEnd = 10.dp)
        )
    }
}

@Composable
fun OrderItemCard(orderItem: OrderItem) {
    Row(
        Modifier
            .fillMaxWidth()
    ) {
        TableCell(text = orderItem.id, weight = columnWeight3)
        TableCell(text = orderItem.productName, weight = columnWeight4)
        TableCell(text = orderItem.quantity.toString(), weight = columnWeight1)
        TableCell(
            text = orderItem.unitPrice.asCurrency(),
            weight = columnWeight2
        )
    }
}

@Preview(showBackground = true, apiLevel = 36)
@Composable
fun OrderDetailPreview() {
    FirstComposeAppTheme {
        OrderDetailContent(
            uiState = OrderDetailUiState(
                selectedOrder = OrderDetail(
                    Order("1", "John Doe", OrderStatus.COMPLETED, 1, 100.0),
                    items = listOf(
                        OrderItem(
                            id = "ITEM-001",
                            productName = "Virtual SIM",
                            quantity = 2,
                            unitPrice = 199.99
                        )
                    )
                ),
                isLoading = false,
                error = null
            ),
            onOrderSummary = {}
        )
    }
}