package dev.shog.lib

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

private val FORMATTER = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.LONG)
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())

/**
 * Get a [Long] as a [Date].
 */
fun Long.asDate(): Date =
        Date.from(asInstant())

/**
 * Get a [Long] as an [Instant].
 */
fun Long.asInstant(): Instant =
        Instant.ofEpochMilli(this)

/**
 * Format a [Instant] to date.
 */
fun Instant.defaultFormat(): String =
        FORMATTER.format(this)
