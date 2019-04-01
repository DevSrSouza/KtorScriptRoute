package br.com.devsrsouza.ktorscriptroute.script

import io.ktor.request.ApplicationRequest
import io.ktor.response.ApplicationResponse
import kotlin.script.experimental.annotations.KotlinScript

const val scriptExtension = "ksr"

@KotlinScript("Ktor Script Route", scriptExtension, KtorRouteCompilationConfiguration::class)
abstract class KtorScript(val request: ApplicationRequest, val response: ApplicationResponse) {
    val req get() = request
    val res get() = response
}
