package br.com.devsrsouza.ktorscriptroute.host

import io.ktor.application.call
import io.ktor.application.feature
import io.ktor.http.HttpStatusCode
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.application
import io.ktor.routing.route

fun Route.ktorScript(path: String) {
    val scriptRoute = application.feature(KtorScriptRoute) // early require

    route(path) {
        handle {
            val script = scriptRoute.scripts.find { it.route == call.request.path().toLowerCase() }

            if (script != null) script.script(call)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}