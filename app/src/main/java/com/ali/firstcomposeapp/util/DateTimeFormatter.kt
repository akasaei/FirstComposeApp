package com.ali.firstcomposeapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Formats a given [timestamp] into a time string using the "HH:mm:ss" pattern.
 *
 * @param timestamp The time in milliseconds to be formatted.
 * @return A string representation of the time in 24-hour format (HH:mm:ss).
 */
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