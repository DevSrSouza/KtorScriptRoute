package br.com.devsrsouza.ktorscriptroute.host

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(KtorScriptRoute) {
            scriptFolder = File("route/")
        }

        routing {
            ktorScript("/route")
        }
    }.start(wait = false)
}
