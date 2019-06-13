package com.grudus.imagessimilarity.features

import com.grudus.imagessimilarity.commons.Percent

class ConsistentPointsFinder(
    private val neighboursToTakePercent: Percent,
    private val cohesionPercentage: Percent
) {

    init {
        require(cohesionPercentage in 0.0..1.0)
        require(neighboursToTakePercent in 0.0..1.0)
    }

    fun findConsistentPoints(listOfCommonPoints: List<CommonPoints>): List<CommonPoints> {
        val points1ToPoints2: Map<Point, Point> = listOfCommonPoints
            .groupBy({ it.point1 }, { it.point2 }).mapValues { it.value[0] }
        val points2ToPoints1: Map<Point, Point> = listOfCommonPoints
            .groupBy({ it.point2 }, { it.point1 }).mapValues { it.value[0] }

        val numberOfNeighbors = (listOfCommonPoints.size * neighboursToTakePercent).toInt()

        return listOfCommonPoints
            .filter { (point1, point2) ->
                val neighbours1: Set<Point> = findNeighbours(point1, points1ToPoints2.keys, numberOfNeighbors)
                val neighbours2: Set<Point> = findNeighbours(point2, points2ToPoints1.keys, numberOfNeighbors)

                var consistentPoints = 0

                neighbours1.forEach { neig1 ->
                    val common = points1ToPoints2[neig1]
                    if (neighbours2.contains(common))
                        consistentPoints++
                }


                (consistentPoints.toDouble() / numberOfNeighbors) >= cohesionPercentage
            }
    }

    private fun findNeighbours(
        searchedPoint: Point,
        allPoints: Set<Point>,
        numberOfNeighbors: Int
    ): Set<Point> =
        allPoints.sortedBy { point -> point squaredDistanceTo searchedPoint }
            .take(numberOfNeighbors)
            .toSet()
}
