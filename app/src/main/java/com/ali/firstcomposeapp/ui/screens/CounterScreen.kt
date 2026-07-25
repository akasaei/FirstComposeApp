package com.ali.firstcomposeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ali.firstcomposeapp.ui.theme.FirstComposeAppTheme
import com.ali.firstcomposeapp.viewmodel.CounterViewModel


@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    viewModel: CounterViewModel = viewModel()
) {

    CounterContent(
        count = viewModel.count,
        onIncrease =  viewModel::increase ,
        onDecrease = viewModel::decrease,
        onReset = viewModel::reset,
        modifier = modifier
    )
}



@Composable
fun CounterContent(
    count: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Counter",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(48.dp))

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(horizontal = 48.dp, vertical = 24.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onDecrease,
                enabled = count > 0
            ) {
                Text("Decrease")
            }
            Button(onClick = onIncrease) {
                Text("Increase")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onReset) {
            Text("Reset Counter")
        }


    }
}

@Preview(showBackground = true)
@Composable
fun CounterPreview() {
    FirstComposeAppTheme {
        CounterContent(
            count = 5,
            onIncrease = {},
            onDecrease = {},
            onReset = {}
        )
    }
}