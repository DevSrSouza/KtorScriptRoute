val ktor_version = rootProject.ext["ktor_version"]

dependencies {
    compile(kotlin("stdlib-jdk8"))

    val kx_html_version = "0.6.10"
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:$kx_html_version")
    compile("io.ktor:ktor-server-core:$ktor_version")
    compile("io.ktor:ktor-html-builder:$ktor_version")

    //SCRIPT
    compile(kotlin("scripting-common"))
    compile(kotlin("scripting-jvm"))
}
