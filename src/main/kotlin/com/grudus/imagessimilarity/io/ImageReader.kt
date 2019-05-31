package com.grudus.imagessimilarity.io

import com.grudus.imagessimilarity.commons.WrongExtensionException
import com.grudus.imagessimilarity.commons.fileExtension
import io.vavr.control.Try
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import javax.imageio.ImageIO

class ImageReader(private val imagesToProcessDirectory: File) {
    private val availableExtensions = listOf("png")

    fun read(path: String): Try<BufferedImage> {
        if (!hasCorrectExtension(path)) {
            return Try.failure(WrongExtensionException("File $path must have on of $availableExtensions extension"))
        }

        val file = File(imagesToProcessDirectory, path)

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
