package com.grudus.imagessimilarity.image

import com.grudus.imagessimilarity.features.CommonPoints
import java.awt.Color
import kotlin.random.Random

object CommonPointsPainter {



    fun paint(mergedImages: MergedImages, commonPoints: List<CommonPoints>) {
        val g2d = mergedImages.merged.graphics
        g2d.color = Color.RED
        val deltaHeight = mergedImages.top.height

        for (point in commonPoints) {
            g2d.color = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())

            g2d.drawLine(point.point1.x.toInt(),
                point.point1.y.toInt(),
                point.point2.x.toInt(), deltaHeight + point.point2.y.toInt()
                )
        }

        g2d.dispose()

    }
}
