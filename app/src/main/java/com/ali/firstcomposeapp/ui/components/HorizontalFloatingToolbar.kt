package com.ali.firstcomposeapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.ali.firstcomposeapp.model.Screen


@Composable
fun HorizontalFloatingToolbar(
    currentScreen: Screen,
    onHomeClick: () -> Unit,
    onOrderSummaryClick: () -> Unit,
    onCounterClick: () -> Unit
) {
    BottomAppBar {
        Row {
            TextButton(onClick = onHomeClick, enabled = (currentScreen != Screen.Greeting)) {
                Text("Home")
            }
            TextButton(onClick = onCounterClick, enabled = (currentScreen != Screen.Counter)) {
                Text("Counter")
            }
            TextButton(
                onClick = onOrderSummaryClick,
                enabled = (currentScreen != Screen.OrderSummary)
            ) {
                Text("Order summary")
            }
        }
    }

}
