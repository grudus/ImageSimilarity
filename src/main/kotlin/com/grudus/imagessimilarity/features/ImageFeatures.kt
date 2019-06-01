package com.grudus.imagessimilarity.features

import java.util.*

data class ImageFeatures(val point: Point, val features: ShortArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageFeatures

        if (point != other.point) return false
        if (!features.contentEquals(other.features)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = point.hashCode()
        result = 31 * result + features.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "$point: ${Arrays.toString(features)}"
    }
}


