package br.com.devsrsouza.ktorscriptroute.host

import br.com.devsrsouza.ktorscriptroute.host.RouteBuilder
import io.ktor.application.Application
import io.ktor.application.ApplicationFeature
import io.ktor.util.AttributeKey
import kotlinx.coroutines.runBlocking
import java.io.File

class ScriptRoute(val route: String, val script: RouteBuilder)

class KtorScriptRoute(val scriptFolder: File) {

    val scripts = mutableListOf<ScriptRoute>()

    class KtorScriptRouteOptions {

        var scriptFolder: File = File("route")

    }

    companion object Feature : ApplicationFeature<Application, KtorScriptRouteOptions, KtorScriptRoute> {
        override val key = AttributeKey<KtorScriptRoute>("KtorScriptRoute")

        override fun install(pipeline: Application, configure: KtorScriptRouteOptions.() -> Unit): KtorScriptRoute {
            val options = KtorScriptRouteOptions().apply(configure)
            with(options) {
                val ktorScript = KtorScriptRoute(options.scriptFolder)

                runBlocking {
                    ktorScript.loadScripts()
                }

                return ktorScript
            }
        }

    }
}