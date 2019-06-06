package com.grudus.imagessimilarity.image

import java.awt.Color
import java.awt.image.BufferedImage


class PngAlphaConverter {

    fun hasAlphaChannel(bufferedImage: BufferedImage): Boolean =
        bufferedImage.colorModel.hasAlpha()

    fun removeAlpha(img: BufferedImage): BufferedImage {
        val copy = BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_RGB)
        val g2d = copy.createGraphics()
        g2d.color = Color.WHITE
        g2d.fillRect(0, 0, copy.width, copy.height)
        g2d.drawImage(img, 0, 0, null)
        g2d.dispose()
        return copy
    }

}
