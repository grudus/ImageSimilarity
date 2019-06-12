package com.grudus.imagessimilarity.transform

enum class TransformType(val numberOfPoints: Int) {
    AFFINE(3),
    PERSPECTIVE(4)
}
