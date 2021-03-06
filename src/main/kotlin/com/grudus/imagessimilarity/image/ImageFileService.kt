package com.grudus.imagessimilarity.image

import com.grudus.imagessimilarity.Config
import com.grudus.imagessimilarity.commons.WrongExtensionException
import com.grudus.imagessimilarity.commons.fileExtension
import io.vavr.control.Try
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.nio.file.Paths
import javax.imageio.ImageIO

class ImageFileService(
    private val imagesToProcessDirectory: File,
    private val processedImagesDirectory: File
) {
    constructor(config: Config) : this(
        File(config.imagesToProcessDirectoryPath),
        File(config.processedImagesDirectoryPath)
    )

    private val availableExtensions = listOf("png", "jpg", "JPG", "jpeg")

    fun write(image: BufferedImage, filename: String): Try<File> {
        val file = File(processedImagesDirectory, filename)

        return Try.of { ImageIO.write(image, "png", file) }
            .map { file }
    }


    fun read(path: String): Try<BufferedImage> {
        if (!hasCorrectExtension(path)) {
            return Try.failure(WrongExtensionException("File $path must have on of $availableExtensions extension"))
        }

        val file = if (Paths.get(path).isAbsolute)
            File(path)
        else File(imagesToProcessDirectory, path)

        if (!file.exists()) {
            return Try.failure(FileNotFoundException("Cannot find file ${file.absolutePath}"))
        }

        return Try.of { ImageIO.read(FileInputStream(file)) }
            .flatMap { img ->
                if (img == null) Try.failure(IllegalStateException("Cannot read file due to unknown error"))
                else Try.success(img)
            }
    }


    private fun hasCorrectExtension(path: String): Boolean = availableExtensions.contains(fileExtension(path))

}
