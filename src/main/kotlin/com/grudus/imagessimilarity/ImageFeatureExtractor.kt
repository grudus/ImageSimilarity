package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.commons.filenameWithoutExtension
import com.grudus.imagessimilarity.features.ImageFeatures
import com.grudus.imagessimilarity.features.ImageFeaturesReader
import com.grudus.imagessimilarity.image.ExecuteScriptFeatureExtractor
import com.grudus.imagessimilarity.image.FeatureExtractor
import com.grudus.imagessimilarity.image.ImageFileService
import com.grudus.imagessimilarity.image.PngAlphaConverter
import io.vavr.control.Try
import java.io.File

class ImageFeatureExtractor(private val config: Config) {

    private val imageFileService = ImageFileService(File(config.imagesToProcessDirectoryPath), File(config.imagesToProcessDirectoryPath))
    private val pngAlphaConverter = PngAlphaConverter()
    private val imageFeaturesReader = ImageFeaturesReader()
    private val featureExtractor: FeatureExtractor =
        ExecuteScriptFeatureExtractor(
            workingDirectory = File(config.extractFeaturesScriptDirectoryPath),
            scriptName = config.extractFeaturesScriptName,
            processedImageDirectory = File(config.processedImagesDirectoryPath)
        )


    fun extract(imagePath: String, useExistingFeatures: Boolean = true): Try<List<ImageFeatures>> {
        val filename = filenameWithoutExtension(imagePath)

        if (useExistingFeatures) {
            val existingFeatures = File(config.processedImagesDirectoryPath, "$filename.png.haraff.sift")
            if (existingFeatures.exists()) {
                return imageFeaturesReader.read(existingFeatures)
            }
        }

        return imageFileService.read(imagePath)
            .map { img -> pngAlphaConverter.removeAlpha(img) }
            .flatMap { img -> imageFileService.write(img, "$filename.png") }
            .flatMap { file -> featureExtractor.extract(file) }
            .flatMap { file -> imageFeaturesReader.read(file) }
    }

}
