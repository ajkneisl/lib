package dev.shog.lib.util

import kotlin.math.ln
import kotlin.math.pow

/**
 * Turn a [Long] into kib, mb etc.
 */
fun Long.asBytes(): String {
    if (this < 1024)
        return "$this B"

    val exp = (ln(this.toDouble()) / ln(1024.toDouble())).toInt()
    val pre = ("KMGTPE")[exp - 1] + "i"

    return String.format("%.1f %sB", this / 1024.toDouble().pow(exp.toDouble()), pre)
}

/**
 * Turn a [Double] into a percentage.
 */
fun Double.asPercentage(): String =
        "$this%"