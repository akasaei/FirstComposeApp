package com.ali.firstcomposeapp.util

//import kotlinx.datetime.Instant
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun formatTime(
    instant: Instant
): String {

    val localDateTime =
        instant.toLocalDateTime(
            TimeZone.currentSystemDefault()
        )

    return "%02d:%02d".format(
        localDateTime.hour,
        localDateTime.minute
    )
}