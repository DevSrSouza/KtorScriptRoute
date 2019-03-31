import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
}

val kotlin_version by extra(plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion)
val ktor_version by extra("1.1.3")

group = "br.com.devsrsouza"
version = "0.0.1-SNAPSHOT"

subprojects {
    plugins.apply("org.jetbrains.kotlin.jvm")

    repositories {
        jcenter()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}
