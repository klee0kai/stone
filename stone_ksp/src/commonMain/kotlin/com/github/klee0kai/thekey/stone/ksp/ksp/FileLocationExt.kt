package com.github.klee0kai.thekey.stone.ksp.ksp

import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.Location
import java.io.File

fun Location.findText(
    count: Int = Int.MAX_VALUE,
): String {
    if (this is FileLocation) {
        val file = File(filePath)
        val lines = file.readLines()

        return lines
            .drop(lineNumber - 1)
            .take(count)
            .joinToString("\n")
    }
    return ""
}


fun Location.fileText(): String {
    if (this is FileLocation) {
        val file = File(filePath)
        val lines = file.readLines()

        return lines.joinToString("\n")
    }
    return ""
}

