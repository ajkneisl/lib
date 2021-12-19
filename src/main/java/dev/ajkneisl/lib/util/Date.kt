package dev.ajkneisl.lib.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

private val FORMATTER =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())

/** Shortcut for [System.currentTimeMillis]. */
fun currentTimeMillis(): Long = System.currentTimeMillis()

/** Get a [Long] as a [Date]. */
fun Long.asDate(): Date = Date.from(asInstant())

/** Get a [Long] as an [Instant]. */
fun Long.asInstant(): Instant = Instant.ofEpochMilli(this)

/** Format a [Long] using [FORMATTER]. */
fun Long.defaultFormat(): String = asInstant().defaultFormat()

/** Get the age of a [Long] */
fun Long.getAge(): Long = System.currentTimeMillis() - this

/** Turn a [Long] into a fancy date. */
fun Long.fancyDate(): String {
    var response = ""

    val seconds = this / 1000

    if (seconds <= 60) {
        // Assuming there's multiple seconds
        return "$seconds seconds"
    }

    val minutes = seconds / 60

    if (minutes < 60)
        return if (minutes > 1) "$minutes minutes ${seconds - minutes * 60} seconds"
        else "$minutes minute ${seconds - minutes * 60} seconds"

    val hours = minutes / 60
    val hoursMinutes = minutes - hours * 60

    if (hours < 24) {
        response += if (hours > 1) "$hours hours " else "$hours hour "
        response += if (hoursMinutes > 1) "$hoursMinutes minutes" else "$hoursMinutes minute"

        return response
    }

    val days = hours / 24
    val hoursDays = hours - days * 24

    if (days < 7) {
        response += if (days > 1) "$days days " else "$days day "
        response += if (hoursDays > 1) "$hoursDays hours" else "$hoursDays hour"

        return response
    }

    val weeks = days / 7
    val weekDays = days - weeks * 7

    response += if (weeks > 1) "$weeks weeks " else "$weeks week "
    response += if (weekDays > 1) "$weekDays days" else "$weekDays day"

    return response
}

/** Format a [Instant] to date. */
fun Instant.defaultFormat(): String = FORMATTER.format(this)
