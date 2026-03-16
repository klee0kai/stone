package com.github.klee0kai.thekey.stone.ksp.psi

import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.KSNode
import org.jetbrains.kotlin.com.intellij.psi.impl.PsiElementBase

fun PsiElementBase.isSamePlace(otherElement: KSNode): Boolean {
    val document = containingFile.containingFile.viewProvider.document
    val kspElementLine = ((otherElement.location as? FileLocation)?.lineNumber ?: 1) - 1

    val psiFunLineStart = document.getLineNumber(textRange.startOffset)
    val psiFunLineEnd = document.getLineNumber(textRange.endOffset)

    return kspElementLine in psiFunLineStart..psiFunLineEnd
}