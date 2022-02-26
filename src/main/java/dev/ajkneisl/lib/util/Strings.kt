package dev.ajkneisl.lib.util

import java.util.*

/**
 * Capitalize string.
 */
fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }