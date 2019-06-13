package com.grudus.imagessimilarity.commons

fun Double.isAlmostZero(): Boolean = Math.abs(this) < 1e-10

fun Int.square(): Int = Math.pow(this.toDouble(), 2.0).toInt()
typealias Percent = Double
