package com.softmoore.graphlib

/**
 * Label encapsulates a double value and a string, where the double value
 * represents a point on an axis and the string is used to label that point.
 */
data class Label(val tick: Double, val label: String) {
    constructor(tick: Int, label: String): this(tick.toDouble(), label)
}
