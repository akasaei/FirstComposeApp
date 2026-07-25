package com.ali.firstcomposeapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderStatus
import com.ali.firstcomposeapp.ui.components.TableCell
import com.ali.firstcomposeapp.ui.components.TableHeading
import com.ali.firstcomposeapp.ui.theme.Blue80
import com.ali.firstcomposeapp.ui.theme.FirstComposeAppTheme
import com.ali.firstcomposeapp.viewmodel.OrderViewModel


@Composable
fun OrderSummaryScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = viewModel()
) {
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    OrderSummaryContent(
        orders = orders,
        isLoading = isLoading,
        modifier = modifier
    )
}


@Composable
fun OrderSummaryContent(
    orders: List<Order>,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {

    val columnWeight4 = .4f
    val columnWeight3 = .3f
    val columnWeight7 = .7f

    Column(
        modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Order Summary",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                color = Blue80
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )

        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = Blue80
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Fetching orders...", color = Blue80)
            }
        } else {
            LazyColumn(
                reverseLayout = false,
                contentPadding = PaddingValues(6.dp)
            ) {
                item {
                    Row(
                        Modifier
                            .border(
                                border = BorderStroke(1.dp, Blue80),
                                shape = RoundedCornerShape(
                                    topStart = 10.dp,
                                    topEnd = 10.dp,
                                    bottomEnd = 0.dp,
                                    bottomStart = 0.dp
                                )
                            )
                            .background(
                                color = Blue80, shape = RoundedCornerShape(
                                    topStart = 10.dp,
                                    topEnd = 10.dp,
                                    bottomEnd = 0.dp,
                                    bottomStart = 0.dp
                                )
                            )
                    )
                    {
                        TableHeading(
                            text = "Id",
                            Modifier
                                .weight(columnWeight4)
                                .border(
                                    border = BorderStroke(1.dp, Blue80),
                                    shape = RoundedCornerShape(
                                        topStart = 10.dp,
                                        topEnd = 0.dp,
                                        bottomEnd = 0.dp,
                                        bottomStart = 0.dp
                                    )
                                )
                        )
                        TableHeading(
                            text = "Customer",
                            Modifier
                                .weight(columnWeight3)
                                .border(
                                    border = BorderStroke(1.dp, Blue80),
                                    shape = RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 0.dp,
                                        bottomEnd = 0.dp,
                                        bottomStart = 0.dp
                                    )
                                )
                        )
                        TableHeading(
                            text = "Value",
                            Modifier
                                .weight(columnWeight3)
                                .border(
                                    border = BorderStroke(1.dp, Blue80),
                                    shape = RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 10.dp,
                                        bottomEnd = 0.dp,
                                        bottomStart = 0.dp
                                    )
                                )
                        )
                    }
                }
                items(orders.size) { index ->
                    val currentOrder = orders[index]
                    Row(Modifier.fillMaxWidth()) {
                        TableCell(text = currentOrder.id, weight = columnWeight4)
                        TableCell(text = currentOrder.customer, weight = columnWeight3)
                        TableCell(text = currentOrder.totalValue.toString(), weight = columnWeight3)
                    }
                }
                item {
                    Row(Modifier.fillMaxWidth()) {
                        TableCell(text = "Total", weight = columnWeight7)
                        TableCell(
                            text = orders.sumOf { order -> order.totalValue }.toString(),
                            weight = columnWeight3
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OrderSummaryPreview() {
    FirstComposeAppTheme {
        OrderSummaryContent(
            orders = listOf(
                Order("1", "John Doe", OrderStatus.COMPLETED, 1, 100.0),
                Order("2", "Jane Smith", OrderStatus.PENDING, 2, 250.5),
                Order("3", "Ali", OrderStatus.IN_PROGRESS, 1, 500.0)
            ),
            isLoading = false
        )
    }
}
