package com.grudus.imagessimilarity.image

import io.vavr.control.Try
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.TimeUnit

class ExecuteScriptFeatureExtractor(
    private val workingDirectory: File,
    private val scriptName: String,
    private val processedImageDirectory: File,
    private val secondsWait: Long = 60
) : FeatureExtractor {


    override fun extract(file: File): Try<File> {
        return executeScript(file)
            .flatMap { getProcessedFile(file) }
    }

    private fun executeScript(file: File): Try<Void> =
        Try.of {
            ProcessBuilder("./$scriptName", file.name)
                .directory(workingDirectory)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
        }.flatMap { process ->
            val success = process.waitFor(secondsWait, TimeUnit.SECONDS)
            if (success && process.exitValue() == 0) Try.success(null)
            else Try.failure(CannotExecuteExtractFeaturesScriptException("Return code: ${process.exitValue()}"))
        }

    private fun getProcessedFile(file: File): Try<File> {
        val processedFile = File(processedImageDirectory, "${file.name}.haraff.sift")

        return if (processedFile.exists())
            Try.success(processedFile)
        else
            Try.failure(FileNotFoundException("Cannot find file with extracted features in ${processedFile.absolutePath}"))
    }
}
