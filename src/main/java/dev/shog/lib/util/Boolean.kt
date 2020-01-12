package dev.shog.lib.util

/**
 * Like a ternary operator in Java.
 * If the [Boolean] is true, return [valueOne]. However, if it's false, return [valueTwo].
 */
fun <T> Boolean.eitherOr(valueOne: T, valueTwo: T): T =
        if (this) valueOne else valueTwo

/**
 * Turn `true` into `enable` and `false` into `disable`.
 */
fun Boolean.toEnableDisable(): String =
        eitherOr("enable", "disable")

/**
 * Add a D onto [toEnableDisable]
 */
fun Boolean.toEnabledDisabled(): String =
        "${toEnableDisable()}d"

/**
 * Turn `true` into `yes` and `false` into `no`.
 */
fun Boolean.toYesNo(): String =
        eitherOr("yes", "no")


