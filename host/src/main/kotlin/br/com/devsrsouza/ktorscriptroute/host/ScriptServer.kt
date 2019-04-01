package br.com.devsrsouza.ktorscriptroute.host

import io.ktor.application.*
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger

class ScriptRoute(val route: String, val builder: RouteBuilder)

fun main() {
    val server = ScriptServer()
}

class ScriptServer {

    val engine = embeddedServer(Netty, port = 8080) {
        // CONFIGURE
    }

    val route: Route
        get() = engine.application.run { featureOrNull(Routing) ?: install(Routing) }

    val log: Logger
        get() = engine.application.log

    val scriptRoutes: MutableList<ScriptRoute> = mutableListOf()

    init {
        engine.start(wait = false)

        runBlocking {
            load(this@ScriptServer)
        }
    }

    fun registerScriptRoute(route: String, builder: RouteBuilder) {
        registerScriptRoute(ScriptRoute(route, builder))
    }

    fun registerScriptRoute(scriptRoute: ScriptRoute) {
        scriptRoutes.add(scriptRoute)
        route.get(scriptRoute.route) {
            scriptRoute.builder(call)
        }
    }
}

