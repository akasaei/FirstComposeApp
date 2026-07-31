package com.ali.firstcomposeapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ali.firstcomposeapp.ui.theme.FirstComposeAppTheme


@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onOrderSummaryClick: () -> Unit,
    onCounterClick: () -> Unit
) {
    BottomAppBar {
        Row {
            TextButton(
                onClick = onHomeClick
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier
                        .size(52.dp)
                        .padding(10.dp)
                )
            }

            TextButton(
                onClick = onCounterClick
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Counter",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier
                        .size(52.dp)
                        .padding(10.dp)
                )
            }
            TextButton(
                onClick = onOrderSummaryClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "Order Summary",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier
                        .size(52.dp)
                        .padding(10.dp)
                )
            }
        }
    }

}

@Preview(apiLevel = 36)
@Composable
fun BottomNavigationBarPreview() {
    FirstComposeAppTheme {
        BottomNavigationBar(
            onHomeClick = {},
            onOrderSummaryClick = {},
            onCounterClick = {}
        )
    }
}
