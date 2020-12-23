package com.softmoore.graphlib


/**
 * Point encapsulates a pair of double values representing a point
 * in the x,y-plane using world coordinates.
 */
data class Point(val x : Double, val y : Double) : Comparable<Point>
  {
    constructor(x : Int, y : Int) : this(x.toDouble(), y.toDouble())

    /**
     * Compare using lexicographic (alphabetic) order.
     */
    override operator fun compareTo(other : Point) : Int
        = when
            {
              x < other.x -> -1
              x > other.x -> 1
              else ->
                  when
                    {
                      y < other.y -> -1
                      y > other.y -> 1
                      else -> 0
                    }
            }
  }
