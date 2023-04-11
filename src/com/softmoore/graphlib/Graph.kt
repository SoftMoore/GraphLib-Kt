package com.softmoore.graphlib

import android.graphics.Color

/**
 * This class contains information about the colors, points, labels, and graphs
 * to be drawn, but it is essentially independent of Android graphics details.
 * Graph objects are created using the nested subclass Builder (Builder Pattern).
 */
class Graph private constructor(builder: Builder) {
    // list of functions to graph
    internal val functions: List<GraphFunction>

    // list of lists of graphPoints to plot
    internal val graphPoints: List<GraphPoints>

    // list of line graphs to draw
    internal val lineGraphs: List<GraphPoints>

    // list of circles to draw
    internal val circles: List<GraphCircle>

    // default colors for background and axes
    val bgColor:   Int
    val axesColor: Int

    // bounds for world coordinates
    val xMin: Double
    val xMax: Double
    val yMin: Double
    val yMax: Double

    // origin for axes; both axes pass through this point
    val axisX: Double
    val axisY: Double

    // place tick marks at these positions on the axes
    val xTicks: List<Double>
    val yTicks: List<Double>

    // place labels at these positions on the axes (override tick marks)
    val xLabels: List<Label>
    val yLabels: List<Label>

    init
      {
        functions   = builder.functions
        graphPoints = builder.graphPoints
        lineGraphs  = builder.lineGraphs
        circles     = builder.circles
        bgColor     = builder.bgColor
        axesColor   = builder.axesColor
        xMin    = builder.xMin
        xMax    = builder.xMax
        yMin    = builder.yMin
        yMax    = builder.yMax
        axisX   = builder.axisX
        axisY   = builder.axisY
        xTicks  = builder.xTicks
        yTicks  = builder.yTicks
        xLabels = builder.xLabels
        yLabels = builder.yLabels
      }

    class Builder {
        // list of functions to graph
        internal val functions = mutableListOf<GraphFunction>()

        // list of graphPoints to plot
        internal val graphPoints = mutableListOf<GraphPoints>()

        // list of line graphs to draw
        internal val lineGraphs = mutableListOf<GraphPoints>()

        // List of circles to draw
        internal val circles = mutableListOf<GraphCircle>()

        // default colors for background, axes, functions, and graphPoints
        var bgColor = Color.WHITE
            private set
        var axesColor = Color.BLACK
            private set
        private var defaultFunctionColor = Color.BLACK
        private var defaultPointColor    = Color.BLACK
        private var defaultCircleColor   = Color.BLACK

        // bounds for world coordinates
        var xMin = -10.0
            private set
        var xMax = 10.0
            private set
        var yMin = -10.0
            private set
        var yMax = 10.0
            private set

        // origin for axes; both axes pass through this point
        var axisX = 0.0
            private set
        var axisY = 0.0
            private set

        // place tick marks at these positions on the axes
        private var defaultTicks = listOf(-8.0, -6.0, -4.0, -2.0, 2.0, 4.0, 6.0, 8.0)
        var xTicks = defaultTicks
            private set
        var yTicks = defaultTicks
            private set

        // place labels at these positions on the axes (override tick marks)
        var xLabels = listOf<Label>()
            private set
        var yLabels = listOf<Label>()
            private set

        /**
         * Add a function to graph.  The function will be graphed using the default function color.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun addFunction(function: (Double) -> Double): Builder {
            functions.add(GraphFunction(function, defaultFunctionColor))
            return this
        }

        /**
         * Add a function to graph and the color to be used for graphing the function.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun addFunction(color: Int, function: (Double) -> Double): Builder {
            functions.add(GraphFunction(function, color))
            return this
        }

        /**
         * Add a list of points to the graph.  The points will be plotted using the default point color.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun addPoints(points: List<Point>): Builder {
            graphPoints.add(GraphPoints(points, defaultPointColor))
            return this
        }

        /**
         * Add a list of points to the graph and the color to be used for plotting those points.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun addPoints(color: Int, points: List<Point>): Builder {
            graphPoints.add(GraphPoints(points, color))
            return this
        }

        /**
         * Add a list of points for a line graph.  The line graph will be drawn using the default point color.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun addLineGraph(points: List<Point>): Builder {
            lineGraphs.add(GraphPoints(points, defaultPointColor))
            return this
        }

        /**
         * Add a list of points for a line graph and the color for drawing the line graph.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun addLineGraph(color: Int, points: List<Point>): Builder {
            lineGraphs.add(GraphPoints(points, defaultPointColor))
            return this
        }

        /**
         * Add a circle to the graph.  The circle will be drawn using the default circle color.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun addCircle(circle: Circle): Builder {
            circles.add(GraphCircle(circle, defaultCircleColor))
            return this
        }

        /**
         * Add a circle to the graph and the color to be used for drawing the circle.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun addCircle(color: Int, circle: Circle): Builder {
            circles.add(GraphCircle(circle, defaultCircleColor))
            return this
        }

        /**
         * Set the background color for the graph.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun setBackgroundColor(bgColor: Int): Builder {
            this.bgColor = bgColor
            return this
        }

        /**
         * Set the color to be used for the graph's axes.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun setAxesColor(axesColor: Int): Builder {
            this.axesColor = axesColor
            return this
        }

        /**
         * Set the default color to be used for graphing functions.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun setFunctionColor(functionColor: Int): Builder {
            this.defaultFunctionColor = functionColor
            return this
        }

        /**
         * Set the default color to be used for plotting points.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun setPointColor(pointColor: Int): Builder {
            this.defaultPointColor = pointColor
            return this
        }

        /**
         * Set the default color to be used for drawing circles.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun setCircleColor(circleColor: Int): Builder {
            this.defaultCircleColor = circleColor
            return this
        }

        /**
         * Set the world coordinates (window) for the graph.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun setWorldCoordinates(xMin: Double, xMax: Double, yMin: Double, yMax: Double): Builder {
            this.xMin = xMin
            this.xMax = xMax
            this.yMin = yMin
            this.yMax = yMax
            return this
        }

        /**
         * Set the world coordinates (window) for the graph.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        fun setWorldCoordinates(xMin: Int, xMax: Int, yMin: Int, yMax: Int): Builder {
            this.xMin = xMin.toDouble()
            this.xMax = xMax.toDouble()
            this.yMin = yMin.toDouble()
            this.yMax = yMax.toDouble()
            return this
        }

        /**
         * Set the axes to be drawn for this graph.  Note that the value for
         * x determines the y-axis and the value for y determines the x-axis.
         */
        fun setAxes(x: Double, y: Double): Builder {
            this.axisX = x
            this.axisY = y
            return this
        }

        /**
         * Set the axes to be drawn for this graph.  Note that the value for
         * x determines the y-axis and the value for y determines the x-axis.
         */
        fun setAxes(x: Int, y: Int): Builder {
            this.axisX = x.toDouble()
            this.axisY = y.toDouble()
            return this
        }

        /**
         * Set the tick marks to be used for the x-axis.
         */
        fun setXTicks(xTicks: List<Double>): Builder {
            this.xTicks = xTicks
            return this
        }

        /**
         * Set the tick marks to be used for the x-axis.
         */
        fun setXTicks(vararg xTicks: Double): Builder {
            this.xTicks = xTicks.toList()
            return this
        }

        /**
         * Set the tick marks to be used for the x-axis.
         */
        fun setXTicks(vararg xTicks: Int): Builder {
            this.xTicks = xTicks.map { it.toDouble() }.toList()
            return this
        }

        /**
         * Set the tick marks to be used for the y-axis.
         */
        fun setYTicks(yTicks: List<Double>): Builder {
            this.yTicks = yTicks
            return this
        }

        /**
         * Set the tick marks to be used for the y-axis.
         */
        fun setYTicks(vararg yTicks: Double): Builder {
            this.yTicks = yTicks.toList()
            return this
        }

        /**
         * Set the tick marks to be used for the y-axis.
         */
        fun setYTicks(vararg yTicks: Int): Builder {
            this.yTicks = yTicks.map { it.toDouble() }.toList()
            return this
        }

        /**
         * Set the labels to be used for the x-axis.
         */
        fun setXLabels(xLabels: List<Label>): Builder {
            this.xLabels = xLabels
            return this
        }

        /**
         * Set the labels to be used for the y-axis.
         */
        fun setYLabels(yLabel: List<Label>): Builder {
            this.yLabels = yLabels
            return this
        }

        /**
         * Create a Graph with the arguments supplied to this builder.
         */
        fun build(): Graph = Graph(this)
    }
}
