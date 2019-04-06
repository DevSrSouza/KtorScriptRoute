package br.com.devsrsouza.ktorscriptroute.script

import io.ktor.application.ApplicationCall
import kotlin.script.experimental.annotations.KotlinScript

const val scriptExtension = "ksr"

@KotlinScript("Ktor Script Route", scriptExtension, KtorRouteCompilationConfiguration::class)
abstract class KtorScript(val call: ApplicationCall) {
    val request get() = call.request
    val response get() = call.response
    val req get() = request
    val res get() = response
}
