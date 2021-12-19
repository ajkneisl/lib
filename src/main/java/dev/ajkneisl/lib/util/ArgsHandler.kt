package dev.ajkneisl.lib.util

/** Manage initial args */
class ArgsHandler {
    private val hooks = LinkedHashMap<String, () -> Unit>()
    private val nHooks = LinkedHashMap<String, () -> Unit>()

    /** Hook onto [arg] and invoke [argHook]. */
    fun hook(arg: String, argHook: () -> Unit) {
        hooks[arg] = argHook
    }

    /** Hook onto [arg] and invoke both [nArgHook] and [argHook]. */
    fun multiHook(arg: String, nArgHook: () -> Unit, argHook: () -> Unit) {
        hooks[arg] = argHook
        nHooks[arg] = nArgHook
    }

    /**
     * Hook onto [arg] and invoke [argHook].
     *
     * Only invokes when the [arg] is not found in the program arguments.
     */
    fun nHook(arg: String, argHook: () -> Unit) {
        nHooks[arg] = argHook
    }

    /** Initialize the hooks. */
    fun initWith(args: Array<String>) {
        val lArgs = args.map { arg -> arg.toLowerCase() }

        hooks.mapKeys { key -> key.key.toLowerCase() }.forEach { hook ->
            if (lArgs.contains(hook.key)) {
                hook.value.invoke()
            }
        }

        nHooks.mapKeys { key -> key.key.toLowerCase() }.forEach { hook ->
            if (!lArgs.contains(hook.key)) {
                hook.value.invoke()
            }
        }
    }
}
