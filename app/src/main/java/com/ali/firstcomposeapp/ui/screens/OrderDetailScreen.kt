package com.ali.firstcomposeapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderDetailUiState
import com.ali.firstcomposeapp.model.OrderStatus
import com.ali.firstcomposeapp.ui.theme.FirstComposeAppTheme
import com.ali.firstcomposeapp.util.asCurrency
import com.ali.firstcomposeapp.util.displayName
import com.ali.firstcomposeapp.util.priorityName
import com.ali.firstcomposeapp.viewmodel.OrderDetailViewModel

@Composable
fun OrderDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderDetailViewModel = viewModel(),
    orderId: String?,
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

                else -> OrderDetails(uiState.order)
            }
        }
    }
}

@Composable
fun OrderDetails(order: Order?) {
    Column (modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
        with(order) {
            Text("Id: ${this?.id}")
            Text("Customer: ${this?.customer}")
            Text("Status: ${this?.status?.displayName()}")
            Text("Priority: ${this?.priority?.priorityName()}")
            Text("Total Value : ${this?.totalValue?.asCurrency()}")
        }
    }
}

@Preview(showBackground = true, apiLevel = 36)
@Composable
fun OrderDetailPreview() {
    FirstComposeAppTheme {
        OrderDetailContent(
            uiState = OrderDetailUiState(
                order = Order("1", "John Doe", OrderStatus.COMPLETED, 1, 100.0),
                isLoading = false,
                error = null
            ),
            orderId = "1",
            onOrderSummary = {}
        )
    }
}