package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.commons.filenameWithoutExtension
import com.grudus.imagessimilarity.io.*
import io.vavr.control.Try
import java.io.File

class ImageFeatureExtractor(
    imagesToProcessDirectoryPath: String,
    scriptDirectoryPath: String,
    scriptName: String
) {

    private val imageReader = ImageReader(File(imagesToProcessDirectoryPath))
    private val imageWriter = ImageWriter(File(imagesToProcessDirectoryPath))
    private val pngAlphaConverter = PngAlphaConverter()
    private val featureExtractor: FeatureExtractor =
        ExecuteScriptFeatureExtractor(File(scriptDirectoryPath), scriptName)


    fun extract(imagePath: String): Try<Any> {
        println("Preparing to read file $imagePath ...")
        val filename = filenameWithoutExtension(imagePath)

        return imageReader.read(imagePath)
            .onSuccess { println("Image $imagePath was successfully read. Preparing to remove alpha channels ...") }
            .map { img -> pngAlphaConverter.removeAlpha(img) }
            .onSuccess { println("Alpha channels were successfully removed. Preparing to save $filename as png without alpha ...") }
            .flatMap { img -> imageWriter.write(img, "$filename.png") }
            .onSuccess { println("File $filename.png successfully saved. Preparing to extract features from image ...") }
            .flatMap { file -> featureExtractor.extract(file) }
    }

}