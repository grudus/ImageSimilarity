package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.io.*
import java.io.File

const val IMAGES_TO_PROCESS_DIRECTORY_PATH = "src/main/resources/images-to-process"
const val EXTRACT_FEATURES_SCRIPT_DIRECTORY = "scripts"
const val EXTRACT_FEATURES_SCRIPT_NAME = "extract_features.sh"

fun main() {
    println("Hello in the image similarity program!")

    val imageReader = ImageReader(File(IMAGES_TO_PROCESS_DIRECTORY_PATH))
    val imageWriter = ImageWriter(File(IMAGES_TO_PROCESS_DIRECTORY_PATH))
    val pngAlphaConverter = PngAlphaConverter()
    val featureExtractor: FeatureExtractor =
        ExecuteScriptFeatureExtractor(File(EXTRACT_FEATURES_SCRIPT_DIRECTORY), EXTRACT_FEATURES_SCRIPT_NAME)

    imageReader.read("directory.png")
        .onSuccess { img -> println("Has alpha? ${pngAlphaConverter.hasAlphaChannel(img)}") }
        .map { img -> pngAlphaConverter.removeAlpha(img) }
        .onSuccess { img -> println("Has alpha? ${pngAlphaConverter.hasAlphaChannel(img)}") }
        .flatMap { img -> imageWriter.write(img, "directory2.png") }
        .flatMap { file -> featureExtractor.extract(file) }
        .onSuccess { println("SUCCESS!") }
        .onFailure { e -> println("Failure!"); e.printStackTrace() }
}
