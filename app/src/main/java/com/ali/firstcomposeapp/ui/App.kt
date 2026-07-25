package com.ali.firstcomposeapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ali.firstcomposeapp.model.Screen
import com.ali.firstcomposeapp.ui.components.HorizontalFloatingToolbar
import com.ali.firstcomposeapp.ui.screens.CounterScreen
import com.ali.firstcomposeapp.ui.screens.GreetingScreen
import com.ali.firstcomposeapp.ui.screens.OrderSummaryScreen

@Composable
fun App(){
    var currentScreen by rememberSaveable  { mutableStateOf(Screen.Greeting) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            HorizontalFloatingToolbar(
                currentScreen = currentScreen,
                onHomeClick = { currentScreen = Screen.Greeting },
                onCounterClick = { currentScreen = Screen.Counter },
                onOrderSummaryClick = { currentScreen = Screen.OrderSummary }
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (currentScreen) {
            Screen.Greeting -> GreetingScreen(
                modifier = modifier
            )

            Screen.Counter -> CounterScreen(
                modifier = modifier
            )

            Screen.OrderSummary -> OrderSummaryScreen(
                modifier = modifier
            )
        }
    }
}