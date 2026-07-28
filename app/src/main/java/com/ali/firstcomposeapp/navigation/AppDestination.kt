package com.ali.firstcomposeapp.navigation

sealed class AppDestination(val route: String) {

    data object Home : AppDestination("home")

    data object Counter : AppDestination("counter")

    data object Orders : AppDestination("orders")

    data object OrderDetails : AppDestination("orders/{orderId}") {

        fun createRoute(orderId: String) =
            "orders/$orderId"
    }
}