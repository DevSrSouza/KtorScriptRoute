package br.com.devsrsouza.script

import java.io.File
import java.lang.RuntimeException
import java.net.URLClassLoader
import kotlin.reflect.full.primaryConstructor
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.api.resultOrNull
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.JvmDependency
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.JvmScriptCompiler
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

suspend fun load(server: ScriptServer) {
    println("Starting load scripts")

    val routePath = File("route/").apply { if (!exists()) mkdirs() }

    val routes: MutableMap<String, SourceCode> = mutableMapOf()

    val configuration = createJvmCompilationConfigurationFromTemplate<ComponentScript>()

    val host = BasicJvmScriptingHost()
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

        val rwd = host.compiler.invoke(source, configuration)
        val result = rwd.resultOrNull()
        if (result != null) {
            server.registerScriptRoute(route, ScriptTemplate {
                val kclass = result.getClass(null)
                val constr = kclass.resultOrNull()?.primaryConstructor
                return@ScriptTemplate if(constr != null) {
                    constr.call(it) as ComponentScript
                } else {
                    for (report in kclass.reports) {
                        report.exception?.printStackTrace()
                    }
                    throw RuntimeException()
                }
            })
            //server.log.info("$route loaded")
            println("$route loaded")
        } else {
            rwd.reports.forEach {
                println(it.message)
            }
        }
    }
}