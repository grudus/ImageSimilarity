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

    override fun chooseSamplesForModel(initialData: List<CommonPoints>): List<CommonPoints> {
        val firstPoints = initialData.random()

        val pointsAfterFirstSelection = initialData
            .filter { (point1, point2) ->
                val distance1 = point1 distanceTo firstPoints.point1
                val distance2 = point2 distanceTo firstPoints.point2

                distance1 > minDistance && distance1 < maxDistance && distance2 > minDistance && distance2 < maxDistance
            }

        return pointsAfterFirstSelection.shuffled().take(transformType.numberOfPoints - 1) + firstPoints
    }
}
