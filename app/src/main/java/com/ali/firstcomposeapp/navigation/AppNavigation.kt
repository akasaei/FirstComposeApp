package com.ali.firstcomposeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ali.firstcomposeapp.ui.screens.CounterScreen
import com.ali.firstcomposeapp.ui.screens.GreetingScreen
import com.ali.firstcomposeapp.ui.screens.OrderDetailScreen
import com.ali.firstcomposeapp.ui.screens.OrderSummaryScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = AppDestination.Home.route,
        modifier = modifier
    ) {

        composable(AppDestination.Home.route) {
            GreetingScreen()
        }

        composable(AppDestination.Counter.route) {
            CounterScreen()
        }

        composable(AppDestination.Orders.route) {
            OrderSummaryScreen(
                onOrderDetailClick = { orderId ->
                    navController.navigate(
                        AppDestination.OrderDetails.createRoute(orderId)
                    )
                }
            )
        }

        composable(
            route = AppDestination.OrderDetails.route
        ) { backStackEntry ->

            val orderId: String =
                backStackEntry.arguments
                    ?.getString("orderId")!!
            OrderDetailScreen(
                orderId = orderId
            ) { navController.popBackStack() }
        }
    }
}