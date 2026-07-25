package com.ali.firstcomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ali.firstcomposeapp.model.Order
import com.ali.firstcomposeapp.model.OrderStatus
import com.ali.firstcomposeapp.model.Screen
import com.ali.firstcomposeapp.ui.screen.GreetingScreen
import com.ali.firstcomposeapp.ui.theme.Blue80
import com.ali.firstcomposeapp.ui.theme.FirstComposeAppTheme
import com.ali.firstcomposeapp.ui.components.HorizontalFloatingToolbar
import com.ali.firstcomposeapp.ui.components.TableCell
import com.ali.firstcomposeapp.ui.components.TableHeading
import com.ali.firstcomposeapp.ui.screen.CounterScreen
import com.ali.firstcomposeapp.ui.screen.OrderSummaryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstComposeAppTheme {
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
        }
    }
}



