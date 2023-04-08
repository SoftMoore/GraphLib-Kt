package com.softmoore.graphlib

/**
 * A list of points together with a color used to plot them.  This
 * class is used for both plotting points and drawing line graphs.
 */
internal data class GraphPoints(val points: List<Point>, val color: Int)
