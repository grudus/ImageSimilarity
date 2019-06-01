package com.grudus.imagessimilarity

const val IMAGES_TO_PROCESS_DIRECTORY_PATH = "src/main/resources/images-to-process"
const val PROCESSED_IMAGES_DIRECTORY_PATH = "src/main/resources/processed-images"
const val EXTRACT_FEATURES_SCRIPT_DIRECTORY_PATH = "scripts"
const val EXTRACT_FEATURES_SCRIPT_NAME = "extract_features.sh"

fun main() {
    println("Hello in the image similarity program!")

    val extractor = ImageFeatureExtractor(
        IMAGES_TO_PROCESS_DIRECTORY_PATH,
        PROCESSED_IMAGES_DIRECTORY_PATH,
        EXTRACT_FEATURES_SCRIPT_DIRECTORY_PATH,
        EXTRACT_FEATURES_SCRIPT_NAME
    )

    extractor.extract("ferrari-A.jpg")
        .onFailure { it.printStackTrace() }
        .onSuccess { features -> println("Successfully parsed ${features.size} features") }

}
