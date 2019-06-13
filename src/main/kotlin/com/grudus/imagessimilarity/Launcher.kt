package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.commons.size
import com.grudus.imagessimilarity.features.CommonFeaturesFinder
import com.grudus.imagessimilarity.features.CommonPoints
import com.grudus.imagessimilarity.features.ImageFeatures
import com.grudus.imagessimilarity.image.CommonPointsPainter
import com.grudus.imagessimilarity.image.ImageFileService
import com.grudus.imagessimilarity.image.ImageMerger
import com.grudus.imagessimilarity.image.MergedImages
import java.io.File


private val config = Config()
private val imageFileService = ImageFileService(config)
private val commonFeaturesFinder = CommonFeaturesFinder()
private val extractor = ImageFeatureExtractor(config)


fun main() {
    println("Hello in the image similarity program!")
    val imageAFile = File(config.imagesToProcessDirectoryPath, "ukulele.jpg")
    val imageBFile = File(config.imagesToProcessDirectoryPath, "ukulele2.jpg")


    val featuresA: List<ImageFeatures> = extractor.extract(imageAFile.name).get()
    val featuresB: List<ImageFeatures> = extractor.extract(imageBFile.name).get()
    println("\n##############\nFeatures: ${featuresA.size},${featuresB.size}")

    val commonFeatures = commonFeaturesFinder.findByDistance(featuresA, featuresB)
    println("Common points: ${commonFeatures.size}")

    val image1Size = imageFileService.read(imageAFile.name).map { it.size() }.get()
    val image2Size = imageFileService.read(imageBFile.name).map { it.size() }.get()

    println("Image 1 size: $image1Size, Image 2 size: $image2Size")

    val commonPoints =
        ImageSimilarityFactory.filterCommonPoints(config, Math.max(image1Size, image2Size), commonFeatures)

    println("Common points after transform: ${commonPoints.size}")

    mergeImagesAndPaintSimiliarPoints(imageAFile, imageBFile, commonPoints)


}

private fun mergeImagesAndPaintSimiliarPoints(
    imageAFile: File,
    imageBFile: File,
    commonPoints: List<CommonPoints>
) {

    val mergedImages: MergedImages =
        ImageMerger.merge(
            imageFileService.read(imageAFile.name).get(),
            imageFileService.read(imageBFile.name).get()
        )

    CommonPointsPainter.paint(mergedImages, commonPoints)

    imageFileService.write(
        mergedImages.merged,
        imageAFile.nameWithoutExtension + "_" + imageBFile.nameWithoutExtension + ".png"
    )
}
