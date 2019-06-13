package com.grudus.imagessimilarity.features

data class Point(val x: Double, val y: Double) {
    override fun toString(): String = "{$x, $y}"

    infix fun squaredDistanceTo(point: Point): Double =
        Math.pow(y - point.y, 2.0) + Math.pow(x - point.x, 2.0)
}
