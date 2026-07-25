package com.ali.firstcomposeapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun RowScope.TableHeaderCell(
    text: String,
    weight: Float,
    shape: Shape = RoundedCornerShape(0.dp)
) {
    TableHeading(
        text = text,
        modifier = Modifier
            .weight(weight)
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = shape
            )
            .padding(8.dp)
    )
}
@Composable
fun TableHeading(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        ),
        modifier = modifier.padding(0.dp)
    )
}
