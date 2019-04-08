# KtorScriptRoute (WIP)
Script route for Ktor ([Kotlin Scripting](https://github.com/Kotlin/KEEP/blob/scripting/proposals/scripting-support.md))

### Instalation on Ktor
```kotlin
install(KtorScriptRoute) {
   scriptFolder = File("route/")
}

routing {
    ktorScript("/route")
}
```

### Example
```kotlin
// home.ksr

runBlocking {
    call.respondHtml(HttpStatusCode.OK) {
        head {
            title("Homeeee")
        }
        body {
            div {
                h1 { +"Your Ip Andress: ${request.local.remoteHost}" }
            }
        }
    }
}
```
