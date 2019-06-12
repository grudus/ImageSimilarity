package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.ai.PointsDistanceRansac
import com.grudus.imagessimilarity.features.CommonFeaturesFinder
import com.grudus.imagessimilarity.features.CommonPoints
import com.grudus.imagessimilarity.features.ConsistentPointsFinder
import com.grudus.imagessimilarity.features.ImageFeatures
import com.grudus.imagessimilarity.transform.Transform

class CommonPointsProcessor {
    private val commonFeaturesFinder = CommonFeaturesFinder()
    private val consistentPointsFinder = ConsistentPointsFinder(CONSISTENT_POINT_NEIGHBOURS, COHESION_PERCENTAGE)
    private val ransac = PointsDistanceRansac(RANSAC_NUMBER_OF_ITERATIONS, RANSAC_MAXIMUM_VALID_ERROR, RANSAC_TRANSFORM, 5, 20_000)


    fun findCommonPointsByCohesion(featuresA: List<ImageFeatures>, featuresB: List<ImageFeatures>): List<CommonPoints> {
        val commonPoints: List<CommonPoints> = commonFeaturesFinder.findByDistance(featuresA, featuresB)

        return consistentPointsFinder.findConsistentPoints(commonPoints)
    }

    fun findCommonPointsByRansac(featuresA: List<ImageFeatures>, featuresB: List<ImageFeatures>): List<CommonPoints> {
        val commonPoints: List<CommonPoints> = commonFeaturesFinder.findByDistance(featuresA, featuresB)

        val bestTransform: Transform = ransac.findBestModel(commonPoints)

        return commonPoints
            .filter { (point1, point2) ->
                val transformedPoint1 = bestTransform.transform(point1)
                transformedPoint1 distanceTo point2 < RANSAC_MAXIMUM_VALID_ERROR
            }
    }
}
