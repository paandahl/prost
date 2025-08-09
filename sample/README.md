# Prost Framework Sample Web Application

This sample demonstrates how to use the prost framework to create a web application with an embedded Jetty server.

## Overview

The prost framework is a featherlight REST framework for Kotlin/JVM Servlets. This sample shows how to:
- Start an embedded servlet container (Jetty)
- Register a servlet that uses the prost framework
- Create REST endpoints using prost annotations
- Handle different HTTP methods and URL parameters

## Project Structure

```
sample/
├── pom.xml                          # Maven configuration with dependencies
├── README.md                        # This file
└── src/main/kotlin/com/tripfinger/commons/prost/sample/
    ├── WebServerApp.kt              # Main application class
    └── SampleRestHandler.kt         # REST endpoints handler
```

## Prerequisites

- Java 8 or higher
- Maven 3.6 or higher

## Building the Application

1. Navigate to the sample directory:
   ```bash
   cd sample
   ```

2. Build the application:
   ```bash
   mvn clean compile
   ```

3. (Optional) Create a fat JAR:
   ```bash
   mvn package
   ```

## Running the Application

### Option 1: Using Maven Exec Plugin

```bash
mvn exec:java
```

### Option 2: Using the Fat JAR

```bash
java -jar target/prost-sample-1.0.0.jar
```

### Option 3: From IDE

Run the `main` method in `WebServerApp.kt`.

## Testing the Endpoints

Once the server is running, you can test the endpoints using curl or your browser:

### 1. Simple Hello World
```bash
curl http://localhost:8080/hello
```
Response: `Hello, World! 🌍`

### 2. Personalized Greeting
```bash
curl http://localhost:8080/hello/Alice
```
Response: `Hello, Alice! 👋`

### 3. Server Status (JSON response)
```bash
curl http://localhost:8080/status
```
Response: JSON with server status and available endpoints

### 4. Echo Endpoint (POST)
```bash
curl -X POST -d "This is a test message" http://localhost:8080/echo
```
Response: JSON with the echoed message and timestamp

### Browser Testing

You can also test GET endpoints directly in your browser:
- http://localhost:8080/hello
- http://localhost:8080/hello/YourName
- http://localhost:8080/status

## Framework Features Demonstrated

### REST Method Annotations
```kotlin
@RestMethod("/hello")                    // Simple GET endpoint
@RestMethod("/hello/:name")              // GET with URL parameter
@RestMethod(method = HttpMethod.POST, value = "/echo")  // POST endpoint
```

### URL Parameters
The framework automatically extracts URL parameters and passes them as method arguments:
```kotlin
@RestMethod("/hello/:name")
fun helloName(name: String): HttpResponse
```

### HTTP Response Handling
```kotlin
val response = HttpResponse()
response.status = 200
response.body = "Hello, World!"
response.contentType = "application/json"
```

### Different HTTP Methods
The framework supports GET, POST, DELETE, and other HTTP methods through the `HttpMethod` enum.

## Server Configuration

- **Port**: 8080 (configurable in `WebServerApp.kt`)
- **Context Path**: `/` (root)
- **Servlet Pattern**: `/*` (handles all requests)

## Stopping the Server

Press `Ctrl+C` in the terminal where the server is running.

## Customization

### Adding New Endpoints

1. Add new methods to `SampleRestHandler.kt` with `@RestMethod` annotations
2. Restart the server
3. The new endpoints will be automatically available

### Changing the Port

Modify the port number in `WebServerApp.kt`:
```kotlin
val server = Server(8080)  // Change to desired port
```

### Adding Authorization

The prost framework supports method guards and authorization. See the main framework documentation for details.

## Dependencies

- **prost**: 0.2 (the REST framework)
- **kotlin-stdlib**: 1.9.20
- **jetty-server**: 9.4.53.v20231009 (embedded server)
- **jetty-servlet**: 9.4.53.v20231009 (servlet support)
- **servlet-api**: 2.5

## Learn More

For more information about the prost framework, see the main project README at the repository root.