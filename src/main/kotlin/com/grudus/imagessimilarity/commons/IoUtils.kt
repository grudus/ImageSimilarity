package com.grudus.imagessimilarity.commons

fun fileExtension(path: String): String = path.substring(path.lastIndexOf(".") + 1)
