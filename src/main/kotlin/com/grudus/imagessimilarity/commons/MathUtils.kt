package com.grudus.imagessimilarity.commons

fun Double.isAlmostZero(): Boolean = Math.abs(this) < 1e-10
