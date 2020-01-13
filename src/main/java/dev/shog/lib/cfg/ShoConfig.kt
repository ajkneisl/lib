package dev.shog.lib.cfg

import java.io.Serializable

/**
 * A ShoConfig.
 * To interact properly with [ConfigHandler], you should make another class
 * that inherits this.
 */
open class ShoConfig(var version: Float? = null): Serializable