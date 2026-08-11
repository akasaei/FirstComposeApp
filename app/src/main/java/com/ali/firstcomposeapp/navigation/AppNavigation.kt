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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ali.firstcomposeapp.viewmodel.OrderDetailViewModel
import com.ali.firstcomposeapp.viewmodel.OrderViewModel

const val ARG_ORDER_ID = "orderId"
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
            val viewModel =
                hiltViewModel<OrderViewModel>()
            OrderSummaryScreen(
                viewModel = viewModel,
                onOrderDetailClick = { orderId ->
                    navController.navigate(
                        AppDestination.OrderDetails.createRoute(orderId)
                    )
                },
                onDeleteOrder = { orderId ->
                    viewModel.deleteOrder(orderId)
                }
            )
        }

        composable(
            route = AppDestination.OrderDetails.route,
            arguments = listOf(
                navArgument(ARG_ORDER_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            val viewModel =
                hiltViewModel<OrderDetailViewModel>()
            OrderDetailScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}