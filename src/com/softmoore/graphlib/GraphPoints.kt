package com.softmoore.graphlib


/**
 * A list of points together with a color used to plot them.  This
 * class is used for both plotting points and drawing line graphs.
 */
internal data class GraphPoints(val points : List<Point>, val color : Int)
/*
  {
    val points : List<Point>

    init
      {
        // we need a list that is sorted and has no duplicates
        if (points.size > 1)
          {
            val tempPoints = points.toMutableList()
            tempPoints.sort()
            tempPoints.removeDuplicates()
            this.points = tempPoints
          }
        else
            this.points = points
      }


    // assumes that the list is sorted
    private fun <E : Comparable<E>> MutableList<E>.removeDuplicates()
      {
        val iter = iterator()
        var oldItem = iter.next()
        while (iter.hasNext())
          {
            val nextItem = iter.next()
            if (oldItem.compareTo(nextItem) == 0)
              iter.remove()
            oldItem = nextItem
          }
      }
  }
*/