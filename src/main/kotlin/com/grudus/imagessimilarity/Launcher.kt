package com.grudus.imagessimilarity

const val IMAGES_TO_PROCESS_DIRECTORY_PATH = "src/main/resources/images-to-process"
const val EXTRACT_FEATURES_SCRIPT_DIRECTORY = "scripts"
const val EXTRACT_FEATURES_SCRIPT_NAME = "extract_features.sh"

fun main() {
    println("Hello in the image similarity program!")

    val extractor = ImageFeatureExtractor(IMAGES_TO_PROCESS_DIRECTORY_PATH, EXTRACT_FEATURES_SCRIPT_DIRECTORY, EXTRACT_FEATURES_SCRIPT_NAME)

    extractor.extract("ferrari-A2.jpg")
        .onSuccess { println("Success!") }
        .onFailure { it.printStackTrace()}

}
