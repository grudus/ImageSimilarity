package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.ai.Algorithm
import com.grudus.imagessimilarity.ai.PointsDistanceRansac
import com.grudus.imagessimilarity.commons.square
import com.grudus.imagessimilarity.features.CommonPoints
import com.grudus.imagessimilarity.features.ConsistentPointsFinder
import com.grudus.imagessimilarity.transform.Transform
import com.grudus.imagessimilarity.transform.TransformType
import com.grudus.imagessimilarity.transform.TransformType.*

object ImageSimilarityFactory {

    fun filterCommonPoints(config: Config, imageSize: Int, commonPoints: List<CommonPoints>): List<CommonPoints> =
        when (config.algorithm) {
            Algorithm.POINTS_COHESION -> findCommonPointsByCohesion(config, commonPoints)
            Algorithm.RANSAC_AFFINE -> findCommonPointsByRansac(config, commonPoints, AFFINE, imageSize)
            Algorithm.RANSAC_PERSPECTIVE -> findCommonPointsByRansac(config, commonPoints, PERSPECTIVE, imageSize)
        }

    private fun findCommonPointsByCohesion(config: Config, commonPoints: List<CommonPoints>): List<CommonPoints> {
        val consistentPointsFinder = ConsistentPointsFinder(config.consistentPointNeighbours, config.cohesionPercentage)
        return consistentPointsFinder.findConsistentPoints(commonPoints)
    }

    private fun findCommonPointsByRansac(
        config: Config,
        commonPoints: List<CommonPoints>,
        transformType: TransformType,
        imageSize: Int
    ): List<CommonPoints> {
        val minDistance = (config.ransacMinDistance * imageSize).toInt()
        val maxDistance = (config.ransacMaxDistance * imageSize).toInt()
        val maxError = (config.ransacMaximumValidError * imageSize).toInt().square()

        println("Min distance: $minDistance, Max distance: $maxDistance, Max error: $maxError")

        val ransac =
            PointsDistanceRansac(config.ransacNumberOfIterations, maxError, transformType, minDistance, maxDistance)


        val bestTransform: Transform = ransac.findBestModel(commonPoints)

        return commonPoints
            .filter { (point1, point2) ->
                val transformedPoint1 = bestTransform.transform(point1)
                transformedPoint1 squaredDistanceTo point2 < maxError
            }
    }
}
