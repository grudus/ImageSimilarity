package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.ai.PointsDistanceRansac
import com.grudus.imagessimilarity.commons.square
import com.grudus.imagessimilarity.features.CommonPoints
import com.grudus.imagessimilarity.features.ConsistentPointsFinder
import com.grudus.imagessimilarity.transform.Transform

class CommonPointsProcessor {
    private val consistentPointsFinder = ConsistentPointsFinder(CONSISTENT_POINT_NEIGHBOURS, COHESION_PERCENTAGE)

    fun findCommonPointsByCohesion(commonPoints: List<CommonPoints>): List<CommonPoints> {
        return consistentPointsFinder.findConsistentPoints(commonPoints)
    }

    fun findCommonPointsByRansac(commonPoints: List<CommonPoints>, imageSize: Int): List<CommonPoints> {
        val minDistance = (RANSAC_MIN_DISTANCE * imageSize).toInt()
        val maxDistance = (RANSAC_MAX_DISTANCE * imageSize).toInt()
        val maxError = (RANSAC_MAXIMUM_VALID_ERROR * imageSize).toInt().square()

        println("Min distance: $minDistance, Max distance: $maxDistance, Max error: $maxError")

        val ransac =
            PointsDistanceRansac(RANSAC_NUMBER_OF_ITERATIONS, maxError, RANSAC_TRANSFORM, minDistance, maxDistance)


        val bestTransform: Transform = ransac.findBestModel(commonPoints)

        return commonPoints
            .filter { (point1, point2) ->
                val transformedPoint1 = bestTransform.transform(point1)
                transformedPoint1 squaredDistanceTo point2 < maxError
            }
    }
}
