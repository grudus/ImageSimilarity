package com.grudus.imagessimilarity.transform

import com.grudus.imagessimilarity.features.Point

interface Transform {
    fun transform(point: Point): Point
}
