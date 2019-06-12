package com.grudus.imagessimilarity.transform

import com.grudus.imagessimilarity.commons.singleColumnMatrix
import com.grudus.imagessimilarity.features.Point
import org.ejml.simple.SimpleMatrix

class PerspectiveTransform(private val matrix: SimpleMatrix) : Transform {

    override fun transform(point: Point): Point {
        val (x, y) = point
        val vectorResult = matrix.mult(singleColumnMatrix(x, y, 1.0))
        return Point(vectorResult[0] / vectorResult[2], vectorResult[1] / vectorResult[2])
    }
}
