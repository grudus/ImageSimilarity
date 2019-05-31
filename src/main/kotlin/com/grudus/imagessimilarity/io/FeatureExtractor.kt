package com.grudus.imagessimilarity.io

import io.vavr.control.Try
import java.io.File

interface FeatureExtractor {
    fun extract(file: File): Try<Any>
}
