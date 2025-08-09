# prost

Notice: This repository is no longer being maintained, as I have switched my server side efforts towards node.js.

A featherlight REST-framework for Kotlin/JVM Servlets. Perfect for bridging server side code with an AngularJS client. Runs well on Google App Engine.

## Configuration

We publish releases to the central maven repository. Add a dependency to your pom.xml:

```xml
<dependency>
  <groupId>com.tripfinger.commons</groupId>
  <artifactId>prost</artifactId>
  <version>0.2</version>
</dependency>
```

## Usage

Create a normal Kotlin object, and use @RestMethod-annotations to expose your API.

```kotlin
object RestHandler {

    @JvmStatic
    @RestMethod("/hello")
    fun hello(): HttpResponse {
        return HttpResponse(200, "Hello, World!")
    }
}
```
    
Register your class with the prost RequestHandler, f.ex. through a context-listener in web.xml:

```xml
<listener>
  <listener-class>MyConfigurationListener</listener-class>
</listener>
```

Example of context-listener:

```kotlin
class ConfigurationListener : ServletContextListener {
    private val requestHandler = RequestHandler()

    override fun contextInitialized(event: ServletContextEvent) {
        requestHandler.setRestHandler(MyRestHandlers::class.java)
    }
  
    override fun contextDestroyed(servletContextEvent: ServletContextEvent) {}
}
```

Finally, map requests to the prosts RequestHandler in your web.xml:

```xml
<servlet>
  <servlet-name>requestHandler</servlet-name>
  <servlet-class>com.tripfinger.commons.prost.RequestHandler</servlet-class>
</servlet>
<servlet-mapping>
  <servlet-name>requestHandler</servlet-name>
  <url-pattern>/*</url-pattern>
</servlet-mapping>
```
