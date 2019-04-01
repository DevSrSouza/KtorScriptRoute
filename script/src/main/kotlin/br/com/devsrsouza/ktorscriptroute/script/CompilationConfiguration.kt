package br.com.devsrsouza.ktorscriptroute.script

import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.dependencies
import kotlin.script.experimental.jvm.JvmDependency
import kotlin.script.experimental.jvm.util.classpathFromClassloader

object KtorRouteCompilationConfiguration : ScriptCompilationConfiguration({
    defaultImports(ktorImports + ktorHtmlImports + kotlinxHtmlImrpots)
    dependencies(
            classpathFromClassloader(KtorRouteCompilationConfiguration::class.java.classLoader)
                    ?.apply { forEach { println(it) } }
                    ?.map { JvmDependency(it) }
                    ?: emptyList()
    )
})