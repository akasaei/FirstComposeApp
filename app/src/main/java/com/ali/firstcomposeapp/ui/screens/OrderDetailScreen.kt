package com.ali.firstcomposeapp.ui.screens


import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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

const val columnWeight1 = .1f
const val columnWeight2 = .2f
const val columnWeight3 = .3f
const val columnWeight4 = .4f
const val columnWeight7 = .7f


@Composable
fun OrderDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderDetailViewModel = hiltViewModel(),
    orderId: String,
    onOrderSummary: () -> Unit
) {
    LaunchedEffect(orderId) {
        viewModel.fetchOrderDetail(orderId)
    }

    val uiState by
    viewModel.uiState.collectAsState()
    OrderDetailContent(
        uiState = uiState,
        orderId = orderId,
        modifier = modifier,
        onOrderSummary = onOrderSummary
    )

}


@Composable
fun OrderDetailContent(
    uiState: OrderDetailUiState,
    orderId: String?,
    modifier: Modifier = Modifier,
    onOrderSummary: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxWidth(),
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "Order Detail",
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
                        onClick = onOrderSummary, modifier = Modifier
                            .padding(start = 20.dp, top = 15.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Order Summary",
                            tint = MaterialTheme.colorScheme.inverseSurface,
                            modifier = Modifier
                                .size(28.dp)
                                .padding(0.dp)
                        )
                    }
                    Text(
                        text = "Order ID:  $orderId",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(20.dp),
                        textAlign = TextAlign.Center
                    )
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

                else -> OrderDetails(uiState.selectedOrder)
            }
        }
    }
}

@Composable
fun OrderDetails(selectedOrder: OrderDetail?) {
    val order = selectedOrder?.order
    val orderItems = selectedOrder?.items
    Column(
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp)
    ) {
        if (order != null) {
            with(order) {
                Text("Customer: ${this.customer}")
                Text("Status: ${this.status.displayName()}")
                Text("Priority: ${this.priority.priorityName()}")
                Text("Total Value : ${this.totalValue.asCurrency()}")
            }
        }

        Spacer(modifier = Modifier.height(48.dp))


        Text("Items (${selectedOrder?.totalItemCount})")
        LazyColumn(
            contentPadding = PaddingValues(6.dp),
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
            if (!orderItems.isNullOrEmpty()) {
                items(items = orderItems, key = { it.id }) { currentOrderItem ->
                    OrderItemCard(currentOrderItem)
                }
            } else {
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        TableCell(text = "There is no additional detail available!", weight = 1.0f)
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
                            productName = "SIM Card",
                            quantity = 2,
                            unitPrice = 20.0
                        )
                    )
                ),
                isLoading = false,
                error = null
            ),
            orderId = "1",
            onOrderSummary = {}
        )
    }
}