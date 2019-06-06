package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.features.CommonFeaturesFinder
import com.grudus.imagessimilarity.features.CommonPoints
import com.grudus.imagessimilarity.features.ConsistentPointsFinder
import com.grudus.imagessimilarity.features.ImageFeatures

class CommonPointsProcessor {
    private val commonFeaturesFinder = CommonFeaturesFinder()
    private val consistentPointsFinder = ConsistentPointsFinder(CONSISTENT_POINT_NEIGHBOURS, COHESION_PERCENTAGE)



    fun findCommonPoints(featuresA: List<ImageFeatures>, featuresB: List<ImageFeatures>): List<CommonPoints> {
        val commonPoints: List<CommonPoints> = commonFeaturesFinder.findByDistance(featuresA, featuresB)

        return consistentPointsFinder.findConsistentPoints(commonPoints)
    }
}
