package br.com.devsrsouza.script

import io.ktor.html.Template
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.html.HTML
import java.io.File
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.JvmDependency
import kotlin.script.experimental.jvm.javaHome
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvm.util.classpathFromClassloader

const val scriptExtension = "html.kts"

@KotlinScript("Ktor DSL Route", scriptExtension, TemplateHTMLCompileConfiguration::class)
abstract class ComponentScript(html: HTML) : HTML(html.attributes, html.consumer, html.namespace)

object TemplateHTMLCompileConfiguration : ScriptCompilationConfiguration({
    defaultImports(listOf("kotlinx.html.*"))
    dependencies(
            classpathFromClassloader(TemplateHTMLCompileConfiguration::class.java.classLoader)
                    ?.apply { forEach { println(it) } }
                    ?.map { JvmDependency(it) }
                    ?: emptyList()
    )
    jvm {
        javaHome(File("/usr/lib/jvm/java-8-openjdk-amd64/"))
    }
})

class ScriptTemplate(val componentScriptBuilder: suspend (HTML) -> ComponentScript) : Template<HTML> {
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
