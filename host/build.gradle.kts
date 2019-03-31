val ktor_version = rootProject.ext["ktor_version"]

dependencies {
    
    compile(kotlin("stdlib-jdk8"))
    compile(project(":script"))
    compile("io.ktor:ktor-server-netty:$ktor_version")

    //SCRIPT
    compile(kotlin("scripting-jvm-host"))
    compile(kotlin("scripting-compiler-embeddable"))
    compile(kotlin("compiler"))
}
