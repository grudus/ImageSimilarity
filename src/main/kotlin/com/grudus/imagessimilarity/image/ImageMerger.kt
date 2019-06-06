package com.grudus.imagessimilarity.image

import java.awt.Graphics2D
import java.awt.image.BufferedImage

class ImageMerger {

    fun merge(topImage: BufferedImage, bottomImage: BufferedImage): MergedImages {
        val width = Math.max(topImage.width, bottomImage.width)
        val height = topImage.height + bottomImage.height

        val merged = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        val g2d: Graphics2D = merged.createGraphics()

        g2d.drawImage(topImage, 0, 0, null)
        g2d.drawImage(bottomImage, 0, topImage.height, null)

        g2d.dispose()

        return MergedImages(topImage, bottomImage, merged)
    }
}
