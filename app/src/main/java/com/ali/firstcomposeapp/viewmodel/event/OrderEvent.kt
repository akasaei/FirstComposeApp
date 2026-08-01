package com.ali.firstcomposeapp.viewmodel.event

sealed interface OrderEvent {

    data object Refresh : OrderEvent
    data object NextPage : OrderEvent

}