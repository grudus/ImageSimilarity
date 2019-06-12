package com.grudus.imagessimilarity.ai

abstract class AbstractRansac<INITIAL_DATA, MODEL, SAMPLES>(
    private val numberOfIterations: Int
) {

    fun findBestModel(initialData: INITIAL_DATA): MODEL {
        var bestModel: MODEL? = null
        var bestScore = Int.MIN_VALUE

        for (i in 0 until numberOfIterations) {
            var model: MODEL?
            do {
                val samples: SAMPLES = chooseSamplesForModel(initialData)
                model = generateModel(samples)
            } while (model == null)

            val score: Int = calculateScore(model, initialData)

            if (score > bestScore) {
                bestScore = score
                bestModel = model
            }
        }

        return bestModel!!
    }

    abstract fun calculateScore(model: MODEL, initialData: INITIAL_DATA): Int
    abstract fun chooseSamplesForModel(initialData: INITIAL_DATA): SAMPLES
    abstract fun generateModel(samples: SAMPLES): MODEL?
}
