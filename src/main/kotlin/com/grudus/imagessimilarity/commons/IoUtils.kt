package com.grudus.imagessimilarity.commons

fun fileExtension(path: String): String = path.substring(path.lastIndexOf(".") + 1)
fun filenameWithoutExtension(path: String): String = path.substring(0, path.lastIndexOf("."))
