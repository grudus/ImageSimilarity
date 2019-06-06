package com.grudus.imagessimilarity.features

class ConsistentPointsFinder(
    private val numberOfNeighbours: Int,
    private val cohesionPercentage: Double
) {

    init {
        require(cohesionPercentage in 0.0..1.0)
        require(numberOfNeighbours > 0)
    }

    fun findConsistentPoints(listOfCommonPoints: List<CommonPoints>): List<CommonPoints> {
        val points1ToPoints2: Map<Point, Point> = listOfCommonPoints
            .groupBy({ it.point1 }, { it.point2 }).mapValues { it.value[0] }
        val points2ToPoints1: Map<Point, Point> = listOfCommonPoints
            .groupBy({ it.point2 }, { it.point1 }).mapValues { it.value[0] }


        return listOfCommonPoints
            .filter { (point1, point2) ->
                val neighbours1: Set<Point> = findNeighbours(point1, points1ToPoints2.keys)
                val neighbours2: Set<Point> = findNeighbours(point2, points2ToPoints1.keys)

                var consistentPoints = 0

                neighbours1.forEach { neig1 ->
                    val common = points1ToPoints2[neig1]
                    if (neighbours2.contains(common) && points2ToPoints1[common] == neig1)
                        consistentPoints++
                }


                (consistentPoints.toDouble() / numberOfNeighbours.toDouble()) >= cohesionPercentage
            }
    }

    private fun findNeighbours(
        point: Point,
        allPoints: Set<Point>
    ): Set<Point> =
        allPoints.sortedBy { euclideanDistance(it, point) }
            .take(numberOfNeighbours)
            .toSet()

    private fun euclideanDistance(point1: Point, point2: Point): Double =
        Math.pow(point1.y - point2.y, 2.0) + Math.pow(point1.x - point2.x, 2.0)
}
