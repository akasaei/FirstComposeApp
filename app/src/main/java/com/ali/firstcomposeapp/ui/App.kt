package com.ali.firstcomposeapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ali.firstcomposeapp.navigation.AppDestination
import com.ali.firstcomposeapp.navigation.AppNavigation
import com.ali.firstcomposeapp.ui.components.HorizontalFloatingToolbar

@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            HorizontalFloatingToolbar(
                onHomeClick = {
                    navController.navigate(
                        AppDestination.Home.route
                    ){
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onCounterClick = {
                    navController.navigate(
                        AppDestination.Counter.route
                    ){
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onOrderSummaryClick = {
                    navController.navigate(
                        AppDestination.Orders.route
                    ){
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}