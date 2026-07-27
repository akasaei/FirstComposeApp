package com.ali.firstcomposeapp.viewmodel.event

sealed interface OrderEvent {

    data object Refresh : OrderEvent

    data class SetSimulation(
        val enabled: Boolean
    ) : OrderEvent
}