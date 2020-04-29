package dev.shog.lib.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.concurrent.timerTask

/**
 * Do [result] if [state] is true.
 */
fun <T> T.ifSo(state: Boolean, result: T.() -> Unit): T {
    if (state) {
        result.invoke(this)
    }

    return this
}

/**
 * Do [result] if [state] is true.
 */
fun <T> T.ifSo(state: T.() -> Boolean, result: T.() -> Unit): T =
        ifSo(state.invoke(this), result)

/**
 * A timer task that's blocking.
 */
fun blockingTimerTask(task: suspend CoroutineScope.() -> Unit): TimerTask {
    return timerTask { runBlocking {
        task.invoke(this)
    }}
}