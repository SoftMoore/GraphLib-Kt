package com.softmoore.graphlib

/**
 * Point encapsulates a pair of double values representing
 * a point in the x,y-plane using world coordinates.
 */
data class Point(val x: Double, val y: Double) {
    constructor(x: Int, y: Int): this(x.toDouble(), y.toDouble())
}
