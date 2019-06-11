package com.grudus.imagessimilarity.commons

import org.ejml.simple.SimpleMatrix


fun singleColumnMatrix(vararg rows: Double): SimpleMatrix {
    val rowsArray: Array<DoubleArray> = rows.map { doubleArrayOf(it) }.toTypedArray()
    return SimpleMatrix(rowsArray)
}
