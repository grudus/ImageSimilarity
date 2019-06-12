package com.grudus.imagessimilarity.ai

import com.grudus.imagessimilarity.features.CommonPoints
import com.grudus.imagessimilarity.transform.Transform
import com.grudus.imagessimilarity.transform.TransformFactory
import com.grudus.imagessimilarity.transform.TransformType
import kotlin.random.Random

class ImageTransformRansac
    (
    numberOfIterations: Int,
    private val maximumValidError: Int,
    private val transformType: TransformType
) :
    AbstractRansac<List<CommonPoints>, Transform, List<CommonPoints>>(numberOfIterations) {

    override fun chooseSamplesForModel(initialData: List<CommonPoints>): List<CommonPoints> =
        (0 until transformType.numberOfPoints)
            .map { initialData[Random.nextInt(initialData.size)] }

    override fun calculateScore(model: Transform, initialData: List<CommonPoints>): Int =
        initialData.count {
            val transformedPoint = model.transform(it.point1)
            transformedPoint distanceTo it.point2 < maximumValidError
        }

    override fun generateModel(samples: List<CommonPoints>): Transform? =
        TransformFactory.createTransform(transformType, samples)

}
