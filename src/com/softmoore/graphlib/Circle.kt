package com.softmoore.graphlib

/**
 * Circle encapsulates a circle with center at (x, y) and specified radius.
 */
data class Circle(val x: Double, val y: Double, val radius: Double) {
    constructor(x: Int, y: Int, radius: Int):
            this(x.toDouble(), y.toDouble(), radius.toDouble())
}