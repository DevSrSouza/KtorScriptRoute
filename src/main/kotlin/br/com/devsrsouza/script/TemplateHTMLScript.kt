package br.com.devsrsouza.script

import io.ktor.html.Template
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.html.HTML
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.annotations.KotlinScriptCompilationConfigurator
import kotlin.script.experimental.annotations.KotlinScriptFileExtension
import kotlin.script.experimental.api.ScriptCompilationConfigurator
import kotlin.script.experimental.api.ScriptCompileConfiguration
import kotlin.script.experimental.api.ScriptCompileConfigurationProperties

const val scriptExtension = "html.kts"

@KotlinScript("Ktor DSL Route")
@KotlinScriptFileExtension(scriptExtension)
@KotlinScriptCompilationConfigurator(TemplateHTMLConfigurator::class)
abstract class ComponentScript(html: HTML) : HTML(html.attributes, html.consumer, html.namespace)

class TemplateHTMLConfigurator : ScriptCompilationConfigurator {
    override val defaultConfiguration: ScriptCompileConfiguration = TemplateHTMLCompileConfiguration
}

object TemplateHTMLCompileConfiguration : ScriptCompileConfiguration(listOf(
        ScriptCompileConfigurationProperties.defaultImports to listOf("kotlinx.html.*")
))

class ScriptTemplate(val componentScriptBuilder: suspend (HTML) -> ComponentScript) : Template<HTML> { //val componentScript: ComponentScript
    override fun HTML.apply() {
        runBlocking {
            componentScriptBuilder(this@apply).let {
                this@apply.manifest = it.manifest
                this@apply.attributes.putAll(it.attributes)
            }
        }
    }
}

/*
@KotlinScript("Ktor DSL Route")
@KotlinScriptFileExtension(scriptExtension)
@KotlinScriptCompilationConfigurator(TemplateHTMLConfigurator::class)
abstract class TemplateHTMLScript : Template<HTML> {
    @ScriptBody abstract override fun HTML.apply()
}*/
