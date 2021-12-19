package dev.ajkneisl.lib.util

import java.io.PrintWriter
import java.io.StringWriter

/** Get a throwable's stack trace as a string. */
fun Throwable.getStackTraceString(): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw, true)

    printStackTrace(pw)

    return sw.buffer.toString()
}

/** Get a throwable's message. */
fun Throwable.getMessage(): String {
    return "${this.javaClass.simpleName}: ${this.message ?: ""}"
}
