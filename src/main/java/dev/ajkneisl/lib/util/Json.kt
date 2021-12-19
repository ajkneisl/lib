package dev.ajkneisl.lib.util

import org.json.JSONArray
import org.json.JSONObject

/** Create a json object of [objs]. */
fun jsonObjectOf(vararg objs: Pair<String, Any>): JSONObject {
    val json = JSONObject()

    objs.forEach { obj -> json.put(obj.first, obj.second) }

    return json
}

/** Create a json array of [objs]. */
fun jsonArrayOf(vararg objs: Any): JSONArray {
    val json = JSONArray()

    objs.forEach { obj -> json.put(obj) }

    return json
}
