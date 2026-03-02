package com.github.klee0kai.thekey.stone.ksp.psi

import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.Disposable
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.psi.KtPsiFactory
import java.lang.AutoCloseable

class InMemoryPsiParser(
    val fileCode: String,
) : AutoCloseable {
    val disposable: Disposable = Disposer.newDisposable()

    val configuration = CompilerConfiguration()
    val environment = KotlinCoreEnvironment.createForProduction(
        disposable,
        configuration,
        EnvironmentConfigFiles.JVM_CONFIG_FILES
    )

    val project = environment.project
    val psiFactory = KtPsiFactory(project, markGenerated = false)

    val ktFile = psiFactory.createFile(fileCode)

    val document = ktFile.containingFile.viewProvider.document

    override fun close() {
        Disposer.dispose(disposable)
    }

}


fun PsiElement.fileNumber(): Int {
    val document = containingFile.viewProvider.document

    if (document != null) {
        val line = document.getLineNumber(textOffset)
        val column = textOffset - document.getLineStartOffset(line)

        return column + 1
    }
    return -1
}
