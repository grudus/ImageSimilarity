package com.grudus.imagessimilarity.transform

import com.grudus.imagessimilarity.commons.canBeInverted
import com.grudus.imagessimilarity.commons.singleColumnMatrix
import com.grudus.imagessimilarity.features.CommonPoints
import org.ejml.simple.SimpleMatrix

object TransformFactory {

    fun createTransform(type: TransformType, commonPoints: List<CommonPoints>): Transform? =
        when (type) {
            TransformType.AFFINE -> createAffineTransform(commonPoints)
            TransformType.PERSPECTIVE -> createPerspectiveTransform(commonPoints)
        }

    private fun createAffineTransform(commonPoints: List<CommonPoints>): Transform? {
        val (x1, y1) = commonPoints[0].point1
        val (x2, y2) = commonPoints[1].point1
        val (x3, y3) = commonPoints[2].point1
        val (u1, v1) = commonPoints[0].point2
        val (u2, v2) = commonPoints[1].point2
        val (u3, v3) = commonPoints[2].point2

        val mainTransformMatrix = SimpleMatrix(
            arrayOf(
                doubleArrayOf(x1, y1, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(x2, y2, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(x3, y3, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, x1, y1, 1.0),
                doubleArrayOf(0.0, 0.0, 0.0, x2, y2, 1.0),
                doubleArrayOf(0.0, 0.0, 0.0, x3, y3, 1.0)
            )
        )

        if (!mainTransformMatrix.canBeInverted()) {
            return null
        }

        val secondPointsVector = singleColumnMatrix(u1, u2, u3, v1, v2, v3)

        val result = mainTransformMatrix.invert().mult(secondPointsVector)

        val affineMatrix = SimpleMatrix(
            arrayOf(
                doubleArrayOf(result[0], result[1], result[2]),
                doubleArrayOf(result[3], result[4], result[5]),
                doubleArrayOf(0.0, 0.0, 1.0)
            )
        )
        return AffineTransform(affineMatrix)
    }

    private fun createPerspectiveTransform(commonPoints: List<CommonPoints>): Transform? {
        val (x1, y1) = commonPoints[0].point1
        val (x2, y2) = commonPoints[1].point1
        val (x3, y3) = commonPoints[2].point1
        val (x4, y4) = commonPoints[3].point1
        val (u1, v1) = commonPoints[0].point2
        val (u2, v2) = commonPoints[1].point2
        val (u3, v3) = commonPoints[2].point2
        val (u4, v4) = commonPoints[3].point2

        val mainTransformMatrix = SimpleMatrix(
            arrayOf(
                doubleArrayOf(x1, y1, 1.0, 0.0, 0.0, 0.0, -u1 * x1, -u1 * y1),
                doubleArrayOf(x2, y2, 1.0, 0.0, 0.0, 0.0, -u2 * x2, -u2 * y2),
                doubleArrayOf(x3, y3, 1.0, 0.0, 0.0, 0.0, -u3 * x3, -u3 * y3),
                doubleArrayOf(x4, y4, 1.0, 0.0, 0.0, 0.0, -u4 * x4, -u4 * y4),
                doubleArrayOf(0.0, 0.0, 0.0, x1, y1, 1.0, -v1 * x1, -v1 * y1),
                doubleArrayOf(0.0, 0.0, 0.0, x2, y2, 1.0, -v2 * x2, -v2 * y2),
                doubleArrayOf(0.0, 0.0, 0.0, x3, y3, 1.0, -v3 * x3, -v3 * y3),
                doubleArrayOf(0.0, 0.0, 0.0, x4, y4, 1.0, -v4 * x4, -v4 * y4)
            )
        )

        if (!mainTransformMatrix.canBeInverted()) {
            return null
        }

        val secondPointsVector = singleColumnMatrix(u1, u2, u3, u4, v1, v2, v3, v4)

        val result = mainTransformMatrix.invert().mult(secondPointsVector)

        val perspectiveMatrix = SimpleMatrix(
            arrayOf(
                doubleArrayOf(result[0], result[1], result[2]),
                doubleArrayOf(result[3], result[4], result[5]),
                doubleArrayOf(result[6], result[7], 1.0)
            )
        )
        return PerspectiveTransform(perspectiveMatrix)
    }


}
