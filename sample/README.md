# Prost Framework Sample Application

This directory contains a simple sample application that demonstrates the basic usage of the Prost REST framework for Kotlin/JVM.

## About Prost

Prost is a featherlight REST framework for Kotlin/JVM Servlets, perfect for bridging server-side code with client applications. It runs well on Google App Engine and other servlet containers.

## What This Sample Demonstrates

The sample application shows how to:

1. **Create REST endpoints** using the `@RestMethod` annotation
2. **Handle different HTTP methods** (GET, POST)
3. **Work with path parameters** (e.g., `/user/{id}`)
4. **Process request bodies** for POST requests
5. **Return different response types** (plain text, JSON)
6. **Set up the RequestHandler** to process requests

## Project Structure

```
sample/
├── pom.xml                              # Maven configuration
├── README.md                            # This file
└── src/main/kotlin/com/tripfinger/commons/prost/sample/
    ├── SampleRestHandler.kt             # REST endpoint definitions
    └── SampleApplication.kt             # Main application demonstrating usage
```

## Sample Endpoints

The sample includes the following REST endpoints:

| Method | Path | Description |
|--------|------|-------------|
| GET | `/hello` | Simple greeting message |
| GET | `/hello/{name}` | Personalized greeting |
| GET | `/user/{id}` | Get user information (JSON) |
| POST | `/message/{recipient}` | Send a message |
| GET | `/endpoints` | List all available endpoints |

## Building and Running

### Prerequisites

- Java 8 or higher
- Maven 3.x

### Steps

1. **Navigate to the sample directory:**
   ```bash
   cd sample
   ```

2. **Install the parent prost library** (if not available in Maven Central):
   ```bash
   cd ..
   mvn clean install
   cd sample
   ```

3. **Build the sample:**
   ```bash
   mvn clean compile
   ```

4. **Run the sample application:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.tripfinger.commons.prost.sample.SampleApplication"
   ```

   Alternatively, if you have the compiled classes:
   ```bash
   java -cp target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q) com.tripfinger.commons.prost.sample.SampleApplication
   ```

### Expected Output

When you run the sample, you should see output similar to:

```
Prost Framework Sample Application
==================================
Initializing Prost Sample Application...
REST handlers registered successfully!
Available endpoints:
  GET  /hello
  GET  /hello/{name}
  GET  /user/{id}
  POST /message/{recipient}
  GET  /endpoints

=== Demonstrating Prost Framework Usage ===

1. Testing GET /hello
   Response: 200 - Hello from Prost framework!

2. Testing GET /hello/World
   Response: 200 - Hello, World!

3. Testing GET /user/42
   Response: 200 - {"id": "42", "name": "Sample User 42", "email": "user42@example.com"}

4. Testing POST /message/Alice
   Response: 200 - Message sent to Alice: Hello from the sample app!

5. Testing GET /endpoints
   Response: 200
   Body: {
       "endpoints": [
           {"method": "GET", "path": "/hello", "description": "Simple greeting"},
           {"method": "GET", "path": "/hello/{name}", "description": "Personalized greeting"},
           {"method": "GET", "path": "/user/{id}", "description": "Get user information"},
           {"method": "POST", "path": "/message/{recipient}", "description": "Send a message"},
           {"method": "GET", "path": "/endpoints", "description": "List all endpoints"}
       ]
   }

6. Testing GET /nonexistent
   Response: 404 - Resource not found: [nonexistent]

=== Sample completed successfully! ===
```

## Using Prost in a Real Servlet Environment

In a production servlet environment, you would typically:

1. **Create a ServletContextListener** to initialize your REST handlers:
   ```kotlin
   class MyContextListener : ServletContextListener {
       private val requestHandler = RequestHandler()
       
       override fun contextInitialized(event: ServletContextEvent) {
           requestHandler.setRestHandler(MyRestHandlers::class.java)
       }
       
       override fun contextDestroyed(event: ServletContextEvent) {}
   }
   ```

2. **Configure the servlet in web.xml:**
   ```xml
   <servlet>
       <servlet-name>requestHandler</servlet-name>
       <servlet-class>com.tripfinger.commons.prost.RequestHandler</servlet-class>
   </servlet>
   <servlet-mapping>
       <servlet-name>requestHandler</servlet-name>
       <url-pattern>/*</url-pattern>
   </servlet-mapping>
   
   <listener>
       <listener-class>com.yourpackage.MyContextListener</listener-class>
   </listener>
   ```

## Key Concepts

### REST Method Annotation

Use `@RestMethod` to mark methods as REST endpoints:

```kotlin
@RestMethod("/path")                    // GET by default
@RestMethod("/path", HttpMethod.POST)   // Explicit HTTP method
@RestMethod("/path/:param")             // Path parameter
```

### Path Parameters

Use `:paramName` in the path and add corresponding parameters to your method:

```kotlin
@RestMethod("/user/:id")
fun getUser(id: String): HttpResponse { ... }
```

### HTTP Response

Return an `HttpResponse` object with status, body, and content type:

```kotlin
val response = HttpResponse()
response.status = 200
response.body = "Hello, World!"
response.contentType = "text/plain"
return response
```

## More Information

For more details about the Prost framework, see the main project README and documentation.