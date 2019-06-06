package com.grudus.imagessimilarity.image

import io.vavr.control.Try
import java.io.File

interface FeatureExtractor {
    fun extract(file: File): Try<File>
}
