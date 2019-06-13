package com.grudus.imagessimilarity

import com.grudus.imagessimilarity.ai.Algorithm
import com.grudus.imagessimilarity.commons.Percent

class Config {
    val imagesToProcessDirectoryPath = "src/main/resources/images-to-process"
    val processedImagesDirectoryPath = "src/main/resources/processed-images"
    val extractFeaturesScriptDirectoryPath = "scripts"
    val extractFeaturesScriptName = "extract_features.sh"
    val consistentPointNeighbours: Percent = 0.1
    val cohesionPercentage: Percent = 0.5

    val ransacNumberOfIterations = 2000
    val ransacMaximumValidError: Percent = 0.05
    val ransacMinDistance: Percent = 0.01
    val ransacMaxDistance: Percent = 0.25

    val algorithm = Algorithm.RANSAC_AFFINE

}
