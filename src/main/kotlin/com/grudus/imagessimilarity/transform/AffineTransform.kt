package com.grudus.imagessimilarity.transform

import com.grudus.imagessimilarity.commons.singleColumnMatrix
import com.grudus.imagessimilarity.features.CommonPoints
import org.ejml.simple.SimpleMatrix

class AffineTransform {


    fun transform(p1: CommonPoints, p2: CommonPoints, p3: CommonPoints) {
        val (x1, y1) = p1.point1
        val (x2, y2) = p2.point1
        val (x3, y3) = p3.point1
        val (u1, v1) = p1.point2
        val (u2, v2) = p2.point2
        val (u3, v3) = p3.point2

        val M = SimpleMatrix(
            arrayOf(
                doubleArrayOf(x1, y1, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(x2, y2, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(x3, y3, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, x1, y1, 1.0),
                doubleArrayOf(0.0, 0.0, 0.0, x2, y2, 1.0),
                doubleArrayOf(0.0, 0.0, 0.0, x3, y3, 1.0)
            )
        )

        val N = singleColumnMatrix(u1, u2, u3, v1, v2, v3)

        val result = M.invert().mult(N)

        val A = SimpleMatrix(
            arrayOf(
                doubleArrayOf(result[0], result[1], result[2]),
                doubleArrayOf(result[3], result[4], result[5]),
                doubleArrayOf(0.0, 0.0, 1.0)
            )
        )
        A.print()

        val UPrim = A.mult(singleColumnMatrix(x1, y1, 1.0))

        UPrim.print()

    }


}
