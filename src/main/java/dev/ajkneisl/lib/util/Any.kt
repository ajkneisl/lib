package dev.ajkneisl.lib.util

/** Do [result] if [state] is true. */
fun <T> T.ifSo(state: Boolean, result: T.() -> Unit): T {
    if (state) {
        result.invoke(this)
    }

    return this
}

/** Do [result] if [state] is true. */
fun <T> T.ifSo(state: T.() -> Boolean, result: T.() -> Unit): T = ifSo(state.invoke(this), result)
