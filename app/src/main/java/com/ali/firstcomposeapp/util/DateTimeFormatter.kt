package com.ali.firstcomposeapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(
    timestamp: Long
): String {

    val formatter = SimpleDateFormat(
        "HH:mm:ss",
        Locale.getDefault()
    )

    return formatter.format(
        Date(timestamp)
    )
}