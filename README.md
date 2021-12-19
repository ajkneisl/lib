[![](https://jitpack.io/v/ajkneisl/lib.svg)](https://jitpack.io/#ajkneisl/lib)
<h1 align="center">lib</h1>
kotlin library :)

## How to use using Gradle.
```groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.ajkneisl:lib:Tag'
}
```

## webhooks
```kotlin
import dev.ajkneisl.lib.discord.DiscordWebhook
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking { 
        DiscordWebhook("webhook url").sendMessage("Hello! :)")
    }
}

```

### ratelimit
webhooks automatically handle rate limit :)

if you want to change the log level of the rate limit notifications: (by default `Level.TRACE`)
```kotlin
import dev.ajkneisl.lib.discord.DiscordWebhook
import org.slf4j.event.Level

fun main() {
    DiscordWebhook.RATE_LIMIT_LOG_LEVEL = Level.TRACE
}
```

### integrating with library
to make your webhook work with other functions of this library (ex: logging functions):
```kotlin
import dev.ajkneisl.lib.Lib
import dev.ajkneisl.lib.discord.DiscordWebhook

fun main() {
    val myWebhook = DiscordWebhook("webhook url")
    
    Lib.DEFAULT_WEBHOOK = myWebhook
}
```
