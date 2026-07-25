package com.ali.firstcomposeapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {

    var count by mutableIntStateOf(0)
        private set

    fun increase() {
        count++
    }

    fun decrease() {
        if (count > 0) count--
    }

    fun reset() {
        count = 0
    }
}