package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.commons.filenameWithoutExtension
import com.grudus.imagessimilarity.features.ImageFeatures
import com.grudus.imagessimilarity.features.ImageFeaturesReader
import com.grudus.imagessimilarity.image.*
import io.vavr.control.Try
import java.io.File

class ImageFeatureExtractor(
    imagesToProcessDirectoryPath: String,
    processedImagesDirectoryPath: String,
    scriptDirectoryPath: String,
    scriptName: String
) {

    private val imageReader = ImageReader(File(imagesToProcessDirectoryPath))
    private val imageWriter = ImageWriter(File(imagesToProcessDirectoryPath))
    private val pngAlphaConverter = PngAlphaConverter()
    private val imageFeaturesReader = ImageFeaturesReader()
    private val featureExtractor: FeatureExtractor =
        ExecuteScriptFeatureExtractor(
            workingDirectory = File(scriptDirectoryPath),
            scriptName = scriptName,
            processedImageDirectory = File(processedImagesDirectoryPath)
        )


    fun extract(imagePath: String): Try<List<ImageFeatures>> {
        println("Preparing to read file $imagePath ...")
        val filename = filenameWithoutExtension(imagePath)

        return imageReader.read(imagePath)
            .onSuccess { println("Image $imagePath was successfully read. Preparing to remove alpha channels ...") }
            .map { img -> pngAlphaConverter.removeAlpha(img) }
            .onSuccess { println("Alpha channels were successfully removed. Preparing to save $filename as png without alpha ...") }
            .flatMap { img -> imageWriter.write(img, "$filename.png") }
            .onSuccess { println("File $filename.png successfully saved. Preparing to extract features from image ...") }
            .flatMap { file -> featureExtractor.extract(file) }
            .onSuccess { file -> println("Features successfully extracted to file ${file.absolutePath}. Preparing to read features ...") }
            .flatMap { file -> imageFeaturesReader.read(file) }
            .onSuccess { features -> println("${features.size} features successfully read") }
    }

}
