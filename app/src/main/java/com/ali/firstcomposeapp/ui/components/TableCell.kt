package com.ali.firstcomposeapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ali.firstcomposeapp.ui.theme.Blue80

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .border(1.dp, Blue80)
            .weight(weight)
            .padding(8.dp)
    )
}

@Composable
fun RowScope.DeleteRow(
    weight: Float,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.Delete ,
        contentDescription = "Delete Order",
        tint = MaterialTheme.colorScheme.error,
        modifier = modifier
            .size(38.dp)
            .weight(weight)
            .padding(5.dp)
    )
}
