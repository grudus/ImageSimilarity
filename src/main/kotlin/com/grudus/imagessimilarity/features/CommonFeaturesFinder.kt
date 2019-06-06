package com.grudus.imagessimilarity.features

import com.grudus.imagessimilarity.commons.CannotFindNearestPointException

class CommonFeaturesFinder {


    fun findByDistance(features1: List<ImageFeatures>, features2: List<ImageFeatures>): List<CommonPoints> {
        val nearestFromFeatures1ToFeatures2 = findNearestPointsFromTo(features1, features2)
        val nearestFromFeatures2ToFeatures1 = findNearestPointsFromTo(features2, features1)

        val commonPoints = mutableListOf<CommonPoints>()

        for (pathFrom1To2 in nearestFromFeatures1ToFeatures2) {
            for (pathFrom2To1 in nearestFromFeatures2ToFeatures1) {
                if (pathFrom1To2.from == pathFrom2To1.to && pathFrom1To2.to == pathFrom2To1.from)
                    commonPoints += CommonPoints(pathFrom1To2.from, pathFrom1To2.to)
            }
        }


        return commonPoints
    }

    private fun findNearestPointsFromTo(
        fromFeatures: List<ImageFeatures>,
        toFeatures: List<ImageFeatures>
    ): List<Path> {
        val nearestPoints = mutableListOf<Path>()

        for (feature in fromFeatures) {
            val nearestPoint = toFeatures.minBy { _f -> euclideanDistance(feature.features, _f.features) }
                ?.point ?: throw CannotFindNearestPointException()
            nearestPoints += Path(feature.point, nearestPoint)
        }

        return nearestPoints
    }


    private fun euclideanDistance(points1: ShortArray, points2: ShortArray): Double {
        var result = 0.0
        for (i in points1.indices) {
            result += Math.pow(points1[i].toDouble() - points2[i].toDouble(), 2.0)
        }
        return result
    }

    private data class Path(val from: Point, val to: Point)

}
