package com.grudus.imagessimilarity.io

import io.vavr.control.Try
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImageWriter(private val processedImagesDirectory: File) {

    fun write(image: BufferedImage, filename: String): Try<File> {
        val file = File(processedImagesDirectory, filename)

        return Try.of { ImageIO.write(image, "png", file) }
            .map { file }
    }
}
