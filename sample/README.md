# Prost Sample Application

This sample application demonstrates the basic usage of the Prost REST framework for Java Servlets.

## Overview

Prost is a featherlight REST framework for Java Servlets that makes it easy to expose REST APIs. This sample shows how to:

- Create REST endpoints using `@RestMethod` annotations
- Handle different HTTP methods (GET, POST)
- Use URL parameters
- Return JSON responses

## Project Structure

```
sample/
├── README.md                    # This file
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/sample/
│       │       └── SampleRestHandler.java    # Sample REST endpoints
│       └── webapp/
│           └── WEB-INF/
│               └── web.xml                    # Servlet configuration
└── pom.xml                                    # Maven configuration
```

## REST Endpoints

The sample provides these endpoints:

- `GET /hello` - Returns a simple greeting
- `GET /hello/{name}` - Returns a personalized greeting
- `POST /echo` - Echoes back the request body
- `GET /status` - Returns application status

## How to Build and Run

1. **Prerequisites**: Make sure you have Java 7+ and Maven installed.

2. **Build the sample**:
   ```bash
   cd sample
   mvn clean compile
   ```

3. **Deploy to a servlet container**:
   - Build the WAR file: `mvn package`
   - Deploy `target/prost-sample.war` to your servlet container (Tomcat, Jetty, etc.)
   - Or run with an embedded server if configured

4. **Test the endpoints**:
   ```bash
   # Simple greeting
   curl http://localhost:8080/prost-sample/hello
   
   # Personalized greeting
   curl http://localhost:8080/prost-sample/hello/World
   
   # Echo endpoint
   curl -X POST -d "Hello from client" http://localhost:8080/prost-sample/echo
   
   # Status check
   curl http://localhost:8080/prost-sample/status
   ```

## Key Concepts Demonstrated

### 1. REST Method Annotation
```java
@RestMethod("/hello")
public static HttpResponse hello() {
    return new HttpResponse(200, "Hello from Prost!");
}
```

### 2. URL Parameters
```java
@RestMethod("/hello/:name")
public static HttpResponse helloName(String name) {
    return new HttpResponse(200, "Hello, " + name + "!");
}
```

### 3. POST Endpoints with Body
```java
@RestMethod(method = HttpMethod.POST, value = "/echo")
public static HttpResponse echo(String body) {
    return new HttpResponse(200, "Echo: " + body);
}
```

### 4. JSON Responses
The framework automatically sets the content type to `application/json` by default.

## Next Steps

- Explore the main prost repository for more advanced features
- Add authentication using `@Guard` annotation
- Implement file upload handling
- Add custom response headers and status codes