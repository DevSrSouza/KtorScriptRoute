package br.com.devsrsouza.ktorscriptroute.host

import br.com.devsrsouza.ktorscriptroute.script.KtorScript
import br.com.devsrsouza.ktorscriptroute.script.scriptExtension
import io.ktor.application.ApplicationCall
import java.lang.RuntimeException
import kotlin.reflect.full.primaryConstructor
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.api.resultOrNull
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

typealias RouteBuilder = suspend (ApplicationCall) -> KtorScript

suspend fun KtorScriptRoute.loadScripts() {
    println("Starting load scripts")

    val routeFolder = scriptFolder.apply { if (!exists()) mkdirs() }

    val routes: MutableMap<String, SourceCode> = mutableMapOf()

    val configuration = createJvmCompilationConfigurationFromTemplate<KtorScript>()

    val host = BasicJvmScriptingHost()
    //server.log.info("Creating a script host")
    println("Creating a script host")

    for (file in routeFolder.walkTopDown().filter { it.name.endsWith(".$scriptExtension") }) {
        val route = file.absolutePath.removePrefix(routeFolder.absolutePath)
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
            scripts.add(ScriptRoute(route) { call ->
                val kclass = result.getClass(null)
                val constr = kclass.resultOrNull()?.primaryConstructor
                return@ScriptRoute if(constr != null) {
                    constr.call(call) as KtorScript
                } else {
                    printReports(kclass.reports)
                    throw RuntimeException()
                }
            })
            //server.log.info("$route loaded")
            println("$route loaded")
        } else {
            printReports(rwd.reports)
        }
    }
}

fun printReports(reports: List<ScriptDiagnostic>) {
    for (diag in reports) {
        val file = diag.sourcePath
        val line = diag.location?.start?.line
        val column = diag.location?.start?.col
        val sourceLocation = if(file != null && line != null)
            "$file(line: $line ${if(column != null) ", column: $column" else ""})"
        else ""
        val message = diag.severity.toString() + ": $sourceLocation" + diag.message
        println(message)
        diag.exception?.printStackTrace()
    }
}
