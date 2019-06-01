package com.grudus.imagessimilarity.features

import io.vavr.control.Try
import java.io.File


class ImageFeaturesReader {

    private val space = Regex("\\s")

    fun read(file: File): Try<List<ImageFeatures>> =
        Try.of {
            file.readLines()
                .drop(2)
                .map { line ->
                    val chunks: List<String> = line.split(space)
                    val points: List<Double> = chunks.take(2).map { it.toDouble() }
                    val features: ShortArray = chunks.takeLast(128).map { it.toShort() }.toShortArray()

                    ImageFeatures(Point(points[0], points[1]), features)
                }
        }
}
