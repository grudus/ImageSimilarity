package com.grudus.imagessimilarity.commons

import java.awt.image.BufferedImage

fun fileExtension(path: String): String = path.substring(path.lastIndexOf(".") + 1)
fun filenameWithoutExtension(path: String): String = path.substring(0, path.lastIndexOf("."))

fun BufferedImage.size(): Int = Math.sqrt(Math.pow(width.toDouble(), 2.0) + Math.pow(height.toDouble(), 2.0)).toInt()
