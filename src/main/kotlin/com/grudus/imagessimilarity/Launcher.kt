package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.commons.Percent
import com.grudus.imagessimilarity.commons.size
import com.grudus.imagessimilarity.features.CommonFeaturesFinder
import com.grudus.imagessimilarity.features.ImageFeatures
import com.grudus.imagessimilarity.image.*
import com.grudus.imagessimilarity.transform.TransformType
import java.io.File

const val IMAGES_TO_PROCESS_DIRECTORY_PATH = "src/main/resources/images-to-process"
const val PROCESSED_IMAGES_DIRECTORY_PATH = "src/main/resources/processed-images"
const val EXTRACT_FEATURES_SCRIPT_DIRECTORY_PATH = "scripts"
const val EXTRACT_FEATURES_SCRIPT_NAME = "extract_features.sh"
const val CONSISTENT_POINT_NEIGHBOURS: Percent = 0.1
const val COHESION_PERCENTAGE: Percent = 0.5

const val RANSAC_NUMBER_OF_ITERATIONS = 2000
const val RANSAC_MAXIMUM_VALID_ERROR: Percent = 0.05
const val RANSAC_MIN_DISTANCE: Percent = 0.01
const val RANSAC_MAX_DISTANCE: Percent = 0.25
val RANSAC_TRANSFORM = TransformType.AFFINE


fun main() {
    println("Hello in the image similarity program!")
    val imageAFile = File(IMAGES_TO_PROCESS_DIRECTORY_PATH, "stop.jpg")
    val imageBFile = File(IMAGES_TO_PROCESS_DIRECTORY_PATH, "stop2.jpg")

    val imageFileService = ImageFileService(File(IMAGES_TO_PROCESS_DIRECTORY_PATH), File(PROCESSED_IMAGES_DIRECTORY_PATH))
    val commonPointsProcessor = CommonPointsProcessor()
    val imageMerger = ImageMerger()
    val commonPointsPainter = CommonPointsPainter()
    val commonFeaturesFinder = CommonFeaturesFinder()


    val extractor = ImageFeatureExtractor(
        IMAGES_TO_PROCESS_DIRECTORY_PATH,
        PROCESSED_IMAGES_DIRECTORY_PATH,
        EXTRACT_FEATURES_SCRIPT_DIRECTORY_PATH,
        EXTRACT_FEATURES_SCRIPT_NAME
    )

    val featuresA: List<ImageFeatures> = extractor.extract(imageAFile.name).get()
    val featuresB: List<ImageFeatures> = extractor.extract(imageBFile.name).get()
    println("\n##############\nFeatures: ${featuresA.size},${featuresB.size}")

    val commonFeatures = commonFeaturesFinder.findByDistance(featuresA, featuresB)
    println("Common points: ${commonFeatures.size}")

    val image1Size = imageFileService.read(imageAFile.name).map { it.size() }.get()
    val image2Size = imageFileService.read(imageBFile.name).map { it.size() }.get()

    println("Image 1 size: $image1Size, Image 2 size: $image2Size")

    val commonPoints = commonPointsProcessor.findCommonPointsByRansac(commonFeatures, Math.max(image1Size, image2Size))

    println("Common points after transform: ${commonPoints.size}")

    val mergedImages: MergedImages =
        imageMerger.merge(imageFileService.read(imageAFile.name).get(), imageFileService.read(imageBFile.name).get())

    commonPointsPainter.paint(mergedImages, commonPoints)

    imageFileService.write(
        mergedImages.merged,
        imageAFile.nameWithoutExtension + "_" + imageBFile.nameWithoutExtension + ".png"
    )


}
