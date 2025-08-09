package com.tripfinger.commons.prost.sample

import com.tripfinger.commons.prost.RequestHandler
import com.tripfinger.commons.prost.model.HttpMethod

/**
 * Sample application demonstrating how to set up and use the Prost REST framework.
 * 
 * This example shows how to:
 * 1. Create a RequestHandler instance
 * 2. Register REST handlers
 * 3. Process requests programmatically (simulating servlet behavior)
 */
object SampleApplication {

    private val requestHandler = RequestHandler()

    /**
     * Initialize the application by setting up the REST handlers.
     */
    fun initialize() {
        println("Initializing Prost Sample Application...")
        
        // Register our sample REST handler with the request handler
        requestHandler.setRestHandler(SampleRestHandler::class.java)
        
        println("REST handlers registered successfully!")
        println("Available endpoints:")
        println("  GET  /hello")
        println("  GET  /hello/{name}")
        println("  GET  /user/{id}")
        println("  POST /message/{recipient}")
        println("  GET  /endpoints")
    }

    /**
     * Demonstrate making requests to the registered endpoints.
     */
    fun demonstrateUsage() {
        println("\n=== Demonstrating Prost Framework Usage ===\n")
        
        // Test simple GET request
        println("1. Testing GET /hello")
        val response1 = requestHandler.handleRequest("/hello", HttpMethod.GET, null, emptyMap(), null)
        println("   Response: ${response1.status} - ${response1.body}")
        
        // Test GET request with path parameter
        println("\n2. Testing GET /hello/World")
        val response2 = requestHandler.handleRequest("/hello/World", HttpMethod.GET, null, emptyMap(), null)
        println("   Response: ${response2.status} - ${response2.body}")
        
        // Test GET request for user info
        println("\n3. Testing GET /user/42")
        val response3 = requestHandler.handleRequest("/user/42", HttpMethod.GET, null, emptyMap(), null)
        println("   Response: ${response3.status} - ${response3.body}")
        
        // Test POST request
        println("\n4. Testing POST /message/Alice")
        val response4 = requestHandler.handleRequest("/message/Alice", HttpMethod.POST, "Hello from the sample app!", emptyMap(), null)
        println("   Response: ${response4.status} - ${response4.body}")
        
        // Test endpoints listing
        println("\n5. Testing GET /endpoints")
        val response5 = requestHandler.handleRequest("/endpoints", HttpMethod.GET, null, emptyMap(), null)
        println("   Response: ${response5.status}")
        println("   Body: ${response5.body}")
        
        // Test non-existent endpoint
        println("\n6. Testing GET /nonexistent")
        val response6 = requestHandler.handleRequest("/nonexistent", HttpMethod.GET, null, emptyMap(), null)
        println("   Response: ${response6.status} - ${response6.body}")
    }

    /**
     * Main function to run the sample application.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        println("Prost Framework Sample Application")
        println("==================================")
        
        // Initialize the framework
        initialize()
        
        // Demonstrate usage
        demonstrateUsage()
        
        println("\n=== Sample completed successfully! ===")
        println("\nIn a real servlet environment, you would:")
        println("1. Deploy this as a WAR file to a servlet container")
        println("2. Configure the RequestHandler servlet in web.xml")
        println("3. Map URL patterns to the servlet")
        println("4. The framework would automatically handle HTTP requests")
    }
}