package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.features.ImageFeatures
import com.grudus.imagessimilarity.image.*
import com.grudus.imagessimilarity.transform.TransformType
import java.io.File

const val IMAGES_TO_PROCESS_DIRECTORY_PATH = "src/main/resources/images-to-process"
const val PROCESSED_IMAGES_DIRECTORY_PATH = "src/main/resources/processed-images"
const val EXTRACT_FEATURES_SCRIPT_DIRECTORY_PATH = "scripts"
const val EXTRACT_FEATURES_SCRIPT_NAME = "extract_features.sh"
const val CONSISTENT_POINT_NEIGHBOURS = 15
const val COHESION_PERCENTAGE = 0.3

const val RANSAC_NUMBER_OF_ITERATIONS = 2000
const val RANSAC_MAXIMUM_VALID_ERROR = 400
val RANSAC_TRANSFORM = TransformType.PERSPECTIVE



fun main() {
    println("Hello in the image similarity program!")
    val imageAFile = File(IMAGES_TO_PROCESS_DIRECTORY_PATH, "deser1.jpg")
    val imageBFile = File(IMAGES_TO_PROCESS_DIRECTORY_PATH, "deser2.jpg")


    val imageReader = ImageReader(File(IMAGES_TO_PROCESS_DIRECTORY_PATH))
    val imageWriter = ImageWriter(File(PROCESSED_IMAGES_DIRECTORY_PATH))
    val commonPointsProcessor = CommonPointsProcessor()
    val imageMerger = ImageMerger()
    val commonPointsPainter = CommonPointsPainter()

    val extractor = ImageFeatureExtractor(
        IMAGES_TO_PROCESS_DIRECTORY_PATH,
        PROCESSED_IMAGES_DIRECTORY_PATH,
        EXTRACT_FEATURES_SCRIPT_DIRECTORY_PATH,
        EXTRACT_FEATURES_SCRIPT_NAME
    )

    val featuresA: List<ImageFeatures> = extractor.extract(imageAFile.name).get()
    val featuresB: List<ImageFeatures> = extractor.extract(imageBFile.name).get()

    val commonPoints = commonPointsProcessor.findCommonPointsByCohesion(featuresA, featuresB)

    val mergedImages: MergedImages =
        imageMerger.merge(imageReader.read(imageAFile.name).get(), imageReader.read(imageBFile.name).get())

    commonPointsPainter.paint(mergedImages, commonPoints)

    imageWriter.write(
        mergedImages.merged,
        imageAFile.nameWithoutExtension + "_" + imageBFile.nameWithoutExtension + ".png"
    )
}
