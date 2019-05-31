package com.grudus.imagessimilarity.io

import io.vavr.control.Try
import java.io.File
import java.util.concurrent.TimeUnit

class ExecuteScriptFeatureExtractor(
    private val workingDirectory: File,
    private val scriptName: String,
    private val secondsWait: Long = 10
) : FeatureExtractor {


    override fun extract(file: File): Try<Any> {
        return executeScript(file)
    }

    private fun executeScript(file: File): Try<Any> =
        Try.of {
            ProcessBuilder("./$scriptName", file.name)
                .directory(workingDirectory)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
        }.flatMap { process ->
            val success = process.waitFor(secondsWait, TimeUnit.SECONDS)

            if (success && process.exitValue() == 0) Try.success(true)
            else Try.failure(CannotExecuteExtractFeaturesScriptException("Return code: ${process.exitValue()}"))
        }
}
