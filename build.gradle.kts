import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.70"
    kotlin("plugin.scripting") version "1.2.70"
}

group = "br.com.devsrsouza"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
}

dependencies {
    val ktor_version = "0.9.4"
    val kx_html_version = "0.6.10"
    compile(kotlin("stdlib-jdk8"))
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:$kx_html_version")
    compile("io.ktor:ktor-server-netty:$ktor_version")
    compile("io.ktor:ktor-server-core:$ktor_version")
    compile("io.ktor:ktor-html-builder:$ktor_version")

    //SCRIPT
    compile(kotlin("scripting-common"))
    compile(kotlin("scripting-jvm"))
    compile(kotlin("scripting-jvm-host"))
    compile(kotlin("scripting-misc"))
    compile(kotlin("scripting-compiler-embeddable"))
    compile(kotlin("compiler"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}
