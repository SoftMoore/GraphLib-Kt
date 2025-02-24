package com.softmoore.graphlib

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.roundToLong

class GraphView: View {
    private var paint = Paint()
    private var pointRadius = 0
    private var tickOffset  = 0
    private var labelOffset = 0
    private var textSize    = 0

    // the graph to be drawn with all its properties
    private lateinit var g: Graph

    constructor(context: Context?): super(context)

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)

    init {
        // adjust point radius, tick offset, text size,
        // and stroke width based on display density
        val configuration = context.resources.configuration
        val densityDpi    = configuration.densityDpi
        val strokeWidth: Int

        // adjust properties based on screen density
        when {
            densityDpi <= DisplayMetrics.DENSITY_LOW -> {
                pointRadius = 3
                tickOffset  = 3
                labelOffset = 3
                textSize    = 7
                strokeWidth = 1
            }
            densityDpi <= DisplayMetrics.DENSITY_MEDIUM -> {
                pointRadius = 3
                tickOffset  = 4
                labelOffset = 3
                textSize    = 8
                strokeWidth = 1
            }
            densityDpi <= DisplayMetrics.DENSITY_HIGH -> {
                pointRadius = 4
                tickOffset  = 7
                labelOffset = 5
                textSize    = 15
                strokeWidth = 1
            }
            densityDpi <= DisplayMetrics.DENSITY_XHIGH -> {
                pointRadius = 6
                tickOffset  = 8
                labelOffset = 5
                textSize    = 20
                strokeWidth = 2
            }
            densityDpi <= DisplayMetrics.DENSITY_XXHIGH -> {
                pointRadius = 8
                tickOffset  = 9
                labelOffset = 7
                textSize    = 30
                strokeWidth = 3
            }
            else -> {
                pointRadius = 10
                tickOffset  = 10
                labelOffset = 9
                textSize    = 35
                strokeWidth = 4
            }
        }

        // set default values
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth.toFloat()
    }

    /**
     * Set the graph to be drawn.
     */
    fun setGraph(g: Graph) {
        this.g = g
        invalidate()
    }

    /**
     * Converts the world x coordinate to the screen x coordinate
     */
    private fun toScreenX(x: Double): Int {
        val slope: Double = width / (g.xMax - g.xMin)
        return (slope * (x - g.xMin)).toInt()
    }

    /**
     * Converts the world y coordinate to the screen y coordinate
     */
    private fun toScreenY(y: Double): Int {
        val slope: Double = height / (g.yMin - g.yMax)
        return (slope * (y - g.yMax)).toInt()
    }

    /**
     * Converts the screen x coordinate to the world x coordinates.
     */
    private fun toWorldX(x: Int): Double {
        val slope: Double = (g.xMax - g.xMin) / width
        return slope * x + g.xMin
    }

    /**
     * Converts the screen y coordinate to the world y coordinates.
     */
    private fun toWorldY(y: Int): Double {
        val slope: Double = (g.yMin - g.yMax) / height
        return slope * y + g.yMax
    }

    private fun drawViewFrame(canvas: Canvas) {
        canvas.drawColor(g.bgColor)
        paint.color = g.axesColor
        paint.style = Paint.Style.STROKE
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun drawAxes(canvas: Canvas) {
        val axisX: Int = toScreenX(g.axisX)
        val axisY: Int = toScreenY(g.axisY)
        val bounds = Rect()

        paint.color    = g.axesColor
        paint.textSize = textSize.toFloat()

        // draw x axis with tick marks and labels
        if (isOnScreenY(axisY)) {
            canvas.drawLine(0f, axisY.toFloat(), width.toFloat(), axisY.toFloat(), paint)
            paint.textAlign = Paint.Align.CENTER

            // draw x labels if they exist; otherwise draw x tick labels
            if (g.xLabels.isNotEmpty()) {
                for (label in g.xLabels) {
                    val xTickLabel = label.label
                    paint.getTextBounds(xTickLabel, 0, xTickLabel.length, bounds)
                    val xTickLabelHeight = bounds.height()
                    val screenXTick = toScreenX(label.tick)
                    if (isOnScreenX(screenXTick)) {
                        canvas.drawLine(screenXTick.toFloat(), axisY - tickOffset.toFloat(),
                                        screenXTick.toFloat(), axisY + tickOffset.toFloat(),
                                        paint)
                        canvas.drawText(xTickLabel, screenXTick.toFloat(),
                                        axisY + tickOffset + labelOffset + xTickLabelHeight.toFloat(),
                                        paint)
                    }
                }
            }
            else {
                for (xTick in g.xTicks) {
                    var xTickLabel: String = xTick.toString()
                    if (xTick == round(xTick))
                        xTickLabel = xTick.roundToLong().toString()
                    paint.getTextBounds(xTickLabel, 0, xTickLabel.length, bounds)
                    val xTickLabelHeight: Int = bounds.height()
                    val screenXTick: Int = toScreenX(xTick)
                    if (isOnScreenX(screenXTick)) {
                        canvas.drawLine(screenXTick.toFloat(), axisY - tickOffset.toFloat(),
                                        screenXTick.toFloat(), axisY + tickOffset.toFloat(),
                                        paint)
                        canvas.drawText(xTickLabel, screenXTick.toFloat(),
                                        axisY + tickOffset + labelOffset + xTickLabelHeight.toFloat(),
                                        paint)
                    }
                }
            }
        }

        // draw y axis with tick marks and labels
        if (isOnScreenX(axisX)) {
            canvas.drawLine(axisX.toFloat(), 0f, axisX.toFloat(), height.toFloat(), paint)
            paint.textAlign = Paint.Align.CENTER

            // draw y labels if they exist; otherwise draw y tick labels
            if (g.yLabels.isNotEmpty()) {
                for (label in g.yLabels) {
                    val yTickLabel: String = label.label
                    paint.getTextBounds(yTickLabel, 0, yTickLabel.length, bounds)
                    val yTickLabelHeight: Int = bounds.height()
                    val yTickLabelWidth: Int  = bounds.width()
                    val screenYTick: Int = toScreenY(label.tick)
                    if (isOnScreenY(screenYTick)) {
                        canvas.drawLine(axisX - tickOffset.toFloat(), screenYTick.toFloat(),
                                        axisX + tickOffset.toFloat(), screenYTick.toFloat(),
                                        paint)
                        canvas.drawText(yTickLabel,
                                        axisX - tickOffset - labelOffset - (yTickLabelWidth / 2).toFloat(),
                                        screenYTick + yTickLabelHeight / 2.toFloat(),
                                        paint)
                    }
                }
            }
            else {
                for (yTick in g.yTicks) {
                    var yTickLabel: String = yTick.toString()
                    if (yTick == round(yTick))
                        yTickLabel = yTick.roundToLong().toString()
                    paint.getTextBounds(yTickLabel, 0, yTickLabel.length, bounds)
                    val yTickLabelHeight: Int = bounds.height()
                    val yTickLabelWidth: Int  = bounds.width()
                    val screenYTick: Int = toScreenY(yTick)
                    if (isOnScreenY(screenYTick)) {
                        canvas.drawLine(axisX - tickOffset.toFloat(), screenYTick.toFloat(),
                                        axisX + tickOffset.toFloat(), screenYTick.toFloat(),
                                        paint)
                        canvas.drawText(yTickLabel,
                                        axisX - tickOffset - labelOffset - (yTickLabelWidth / 2).toFloat(),
                                        screenYTick + yTickLabelHeight / 2.toFloat(),
                                        paint)
                    }
                }
            }
        }
    }

    private fun drawFunctions(canvas: Canvas) {
        for (graphFunction in g.functions)
            drawFunction(graphFunction, canvas)
    }

    private fun drawFunction(graphFunction: GraphFunction, canvas: Canvas) {
        val function: (Double) -> Double = graphFunction.function
        val screenPoints: List<ScreenPoint> = getScreenPointsForFunction(function)

        // use a path of line segments for the graph
        val path = Path()
        if (screenPoints.isNotEmpty()) {
            // move path to first screen point
            var screenPoint = screenPoints[0]
            var screenX: Int = screenPoint.x
            path.moveTo(screenPoint.x.toFloat(), screenPoint.y.toFloat())

            for (i in 1 until screenPoints.size) {
                screenPoint = screenPoints[i]
                if (screenPoint.x == screenX + 1)
                    path.lineTo(screenPoint.x.toFloat(), screenPoint.y.toFloat())
                else
                    path.moveTo(screenPoint.x.toFloat(), screenPoint.y.toFloat())
                screenX = screenPoint.x
            }
        }
        paint.style = Paint.Style.STROKE
        paint.color = graphFunction.color
        canvas.drawPath(path, paint)
    }

    private fun drawGraphPoints(canvas: Canvas) {
        for (graphPoint in g.graphPoints)
            drawPoints(graphPoint, canvas)
    }

    private fun drawPoints(graphPoint: GraphPoints, canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = graphPoint.color

        for ((x, y) in graphPoint.points) {
            val screenX: Int = toScreenX(x)
            val screenY: Int = toScreenY(y)

            if (isNearScreenX(screenX) && isNearScreenY(screenY))
                canvas.drawCircle(screenX.toFloat(), screenY.toFloat(),
                                  pointRadius.toFloat(), paint)
        }
    }

    private fun drawLineGraphs(canvas: Canvas) {
        for (graphPoints in g.lineGraphs)
            drawLineGraph(graphPoints, canvas)
    }

    private fun drawLineGraph(graphPoints: GraphPoints, canvas: Canvas) {
        // use a path for the line segments of the line graph.
        val path = Path()
        val points: List<Point> = graphPoints.points

        // find first point
        var point: Point = points[0]
        var screenX: Int = toScreenX(point.x)
        var screenY: Int = toScreenY(point.y)
        path.moveTo(screenX.toFloat(), screenY.toFloat())

        // find remaining points
        for (i in 1 until points.size) {
            point   = points[i]
            screenX = toScreenX(point.x)
            screenY = toScreenY(point.y)
            path.lineTo(screenX.toFloat(), screenY.toFloat())
        }

        paint.style = Paint.Style.STROKE
        paint.color = graphPoints.color
        canvas.drawPath(path, paint)
    }

    private fun drawGraphCircles(canvas: Canvas) {
        for (circle in g.circles)
            drawGraphCircle(circle, canvas)
    }

    private fun drawGraphCircle(graphCircle: GraphCircle, canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = graphCircle.color

        val circle: Circle = graphCircle.circle

        val screenX: Int = toScreenX(circle.x)
        val screenY: Int = toScreenY(circle.y)
        val screenR: Int = toScreenX(circle.x + circle.radius) - screenX

        canvas.drawCircle(screenX.toFloat(), screenY.toFloat(), screenR.toFloat(), paint)
    }

    /**
     * Returns the list of screen points for the specified function.
     */
    private fun getScreenPointsForFunction(f: (Double) -> Double): List<ScreenPoint> {
        val screenPoints = mutableListOf<ScreenPoint>()
        var screenY: Int

        // construct list of all possible screen points
        for (screenX in -1..width) {
            val x: Double = toWorldX(screenX)
            val y: Double = f.invoke(x)

            if (isFinite(y)) {
                screenY = toScreenY(y)

                if (isNearScreenY(screenY))
                    screenPoints.add(ScreenPoint(screenX, screenY))
            }
        }

        return screenPoints
    }

    /**
     * Returns true when the specified screenX value represents
     * a screenX coordinate for a point on the screen.
     */
    private fun isOnScreenX(screenX: Int): Boolean = screenX in 0 until width

    /**
     * Returns true when the specified screenY value represents
     * a screenY coordinate for a point on the screen.
     */
    private fun isOnScreenY(screenY: Int): Boolean = screenY in 0..height

    /**
     * Returns true when the specified screenX value represents
     * a screenX coordinate for a point near the screen.
     */
    private fun isNearScreenX(screenX: Int): Boolean = abs(screenX) <= 2*width

    /**
     * Returns true when the specified screenY value represents
     * a screenY coordinate for a point near the screen.
     */
    private fun isNearScreenY(screenY: Int): Boolean = abs(screenY) <= 2*height

    /**
     * Replacement method for Double.isFinite(), which was not defined until Java 8.
     */
    private fun isFinite(d: Double): Boolean = abs(d) <= Double.MAX_VALUE
                                     // as implemented in class java.lang.Double

    override fun onDraw(canvas: Canvas) {
        drawViewFrame(canvas)
        drawAxes(canvas)
        drawFunctions(canvas)
        drawGraphPoints(canvas)
        drawLineGraphs(canvas)
        drawGraphCircles(canvas)
    }
}
