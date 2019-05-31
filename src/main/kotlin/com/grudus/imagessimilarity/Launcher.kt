package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.io.ImageReader
import com.grudus.imagessimilarity.io.ImageWriter
import com.grudus.imagessimilarity.io.PngAlphaConverter
import java.io.File

const val IMAGES_TO_PROCESS_DIRECTORY_PATH = "src/main/resources/images-to-process"
const val PROCESSED_IMAGES_DIRECTORY_PATH = "src/main/resources/images-to-process"

fun main() {
    println("Hello in the image similarity program!")

    val imageReader = ImageReader(File(IMAGES_TO_PROCESS_DIRECTORY_PATH))
    val imageWriter = ImageWriter(File(IMAGES_TO_PROCESS_DIRECTORY_PATH))
    val pngAlphaConverter = PngAlphaConverter()

    imageReader.read("directory.png")
        .onSuccess { img -> println("Has alpha? ${pngAlphaConverter.hasAlphaChannel(img)}") }
        .map { img -> pngAlphaConverter.removeAlpha(img) }
        .onSuccess { img -> println("Has alpha? ${pngAlphaConverter.hasAlphaChannel(img)}") }
        .map { img -> imageWriter.write(img, "directory2.png") }
        .onSuccess { img -> println("SUCCESS") }
        .onFailure { e -> println("Failure!"); e.printStackTrace() }
}
