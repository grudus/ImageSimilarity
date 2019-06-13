package com.grudus.imagessimilarity.ai

import com.grudus.imagessimilarity.features.CommonPoints
import com.grudus.imagessimilarity.transform.TransformType

class PointsDistanceRansac(
    numberOfIterations: Int,
    maximumValidError: Int,
    private val transformType: TransformType,
    private val minDistance: Int,
    private val maxDistance: Int
) : ImageTransformRansac(numberOfIterations, maximumValidError, transformType) {

    private val r = Math.pow(minDistance.toDouble(), 2.0).toInt()
    private val R = Math.pow(maxDistance.toDouble(), 2.0).toInt()

    override fun chooseSamplesForModel(initialData: List<CommonPoints>): List<CommonPoints> {
        val firstPoints = initialData.random()

        val pointsAfterFirstSelection = initialData
            .filter { (point1, point2) ->
                val distance1 = point1 squaredDistanceTo firstPoints.point1
                val distance2 = point2 squaredDistanceTo firstPoints.point2

                distance1 > r && distance1 < R && distance2 > r && distance2 < R
            }

        return pointsAfterFirstSelection.shuffled().take(transformType.numberOfPoints - 1) + firstPoints
    }
}
