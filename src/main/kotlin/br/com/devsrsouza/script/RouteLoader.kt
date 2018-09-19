package br.com.devsrsouza.script

import java.io.File
import kotlin.script.experimental.api.ScriptEvaluationEnvironmentParams
import kotlin.script.experimental.api.ScriptSource
import kotlin.script.experimental.api.resultOrNull
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.DummyCompiledJvmScriptCache
import kotlin.script.experimental.jvm.JvmScriptCompiler
import kotlin.script.experimental.jvmhost.impl.KJVMCompilerImpl
import kotlin.script.experimental.util.ChainedPropertyBag

suspend fun load(server: ScriptServer) {
    println("Starting load scripts")

    val routePath = File("route/").apply { if (!exists()) mkdirs() }

    val routes: MutableMap<String, ScriptSource> = mutableMapOf()

    val host = JvmScriptCompiler(KJVMCompilerImpl(), DummyCompiledJvmScriptCache())
    //server.log.info("Creating a script host")
    println("Creating a script host")

    for (file in routePath.walkTopDown().filter { it.name.endsWith("html.kts") }) {
        val route = file.absolutePath.removePrefix(routePath.absolutePath)
                .removeSuffix(".$scriptExtension")
        routes.put(route, file.toScriptSource())
        //server.log.info("Route '$route' finded")
        println("Route '$route' finded")
    }

    for ((route, source) in routes) {
        println("$route loading")
        val configurator = TemplateHTMLConfigurator()
        val rwd = host.compile(source, configurator, configurator.defaultConfiguration)
        val result = rwd.resultOrNull()
        if (result != null) {
            server.registerScriptRoute(route, ScriptTemplate {
                result.instantiate(ChainedPropertyBag(listOf(
                        ScriptEvaluationEnvironmentParams.constructorArgs to it
                ))) as ComponentScript
            })
            //server.log.info("$route loaded")
            println("$route loaded")
        } else {
            rwd.reports.forEach {
                it.exception?.printStackTrace()
            }
        }
    }
}