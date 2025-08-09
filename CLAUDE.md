# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Prost is a featherlight REST framework for Kotlin/JVM servlets, designed for bridging server-side code with client applications. The framework uses annotations to expose REST endpoints and supports embedded Jetty for standalone applications.

**Note**: This repository is no longer actively maintained as development has shifted to Node.js.

## Development Commands

### Core Framework
- `mvn clean compile` - Compile the prost framework
- `mvn test` - Run unit tests using JUnit
- `mvn package` - Build JAR artifact
- `mvn install` - Install to local Maven repository

### Sample Application  
- `cd sample && mvn clean compile` - Compile the sample web application
- `cd sample && mvn exec:java` - Run the sample web server (starts on port 8080)
- `cd sample && mvn package` - Build executable fat JAR with all dependencies
- `java -jar sample/target/prost-sample-1.0.0.jar` - Run the packaged sample application

## Architecture

### Core Components

**RequestHandler** (`src/main/kotlin/com/tripfinger/commons/prost/RequestHandler.kt`)
- Main servlet that handles HTTP requests and routes them to annotated methods
- Supports URL parameter extraction, method guards, and file uploads
- Manages authorization through pluggable `Authorizer` interface

**Annotations** (`src/main/kotlin/com/tripfinger/commons/prost/annotations/`)
- `@RestMethod(path, method)` - Marks methods as REST endpoints
- `@Guard` - Requires authorization (class or method level)
- `@Open` - Explicitly marks methods as public (bypasses guards)  
- `@UrlParam(name)` - Maps URL parameters to method arguments

**Model Classes** (`src/main/kotlin/com/tripfinger/commons/prost/model/`)
- `HttpResponse` - Standardized response wrapper with status, body, contentType
- `HttpMethod` - Enum for supported HTTP methods (GET, POST, DELETE)
- `Authorizer` - Interface for custom authorization logic

### Framework Usage Pattern

1. Create Kotlin object with `@RestMethod` annotated static methods
2. Initialize `RequestHandler` and register your REST handler class
3. Deploy as servlet or use embedded Jetty (see sample application)

### Sample Application Structure

The `sample/` directory demonstrates a complete web application using prost with embedded Jetty:
- `SampleRestHandler.kt` - Example REST endpoints (hello, status, echo)  
- `WebServerApp.kt` - Main class that starts embedded Jetty server
- Includes endpoints: GET /hello, GET /hello/:name, GET /status, POST /echo

## Key Dependencies

- Kotlin 1.9.20 (compile to JVM 1.8 bytecode)
- Servlet API 2.5 (provided scope)
- Apache Commons FileUpload (for multipart handling)
- JUnit 4.12 (testing)
- Jetty 9.4.53 (sample application only)