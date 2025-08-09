package com.tripfinger.commons.prost.sample

import com.tripfinger.commons.prost.RequestHandler
import com.tripfinger.commons.prost.annotations.RestMethod
import com.tripfinger.commons.prost.model.HttpMethod
import com.tripfinger.commons.prost.model.HttpResponse

/**
 * Sample REST handler demonstrating the basic usage of the Prost framework.
 * 
 * This object contains various REST endpoints that show how to:
 * - Create simple GET endpoints
 * - Handle path parameters
 * - Process POST requests with body content
 * - Return different types of responses
 */
object SampleRestHandler {

    /**
     * Simple GET endpoint that returns a greeting message.
     * URL: /hello
     */
    @JvmStatic
    @RestMethod("/hello")
    fun sayHello(): HttpResponse {
        return HttpResponse(200, "Hello from Prost framework!")
    }

    /**
     * GET endpoint with path parameter.
     * URL: /hello/{name}
     * Example: /hello/John -> "Hello, John!"
     */
    @JvmStatic
    @RestMethod("/hello/:name")
    fun sayHelloToName(name: String): HttpResponse {
        return HttpResponse(200, "Hello, $name!")
    }

    /**
     * GET endpoint that returns JSON data about a user.
     * URL: /user/{id}
     * Example: /user/123 -> Returns user information in JSON format
     */
    @JvmStatic
    @RestMethod("/user/:id")
    fun getUserInfo(id: String): HttpResponse {
        val response = HttpResponse()
        response.status = 200
        response.body = """{"id": "$id", "name": "Sample User $id", "email": "user$id@example.com"}"""
        response.contentType = "application/json"
        return response
    }

    /**
     * POST endpoint that accepts a message and returns a response.
     * URL: /message/{recipient}
     * Body: Plain text message
     * Example: POST /message/Alice with body "Hello there" -> "Message sent to Alice: Hello there"
     */
    @JvmStatic
    @RestMethod(method = HttpMethod.POST, value = "/message/:recipient")
    fun sendMessage(recipient: String, messageBody: String): HttpResponse {
        val response = HttpResponse()
        response.status = 200
        response.body = "Message sent to $recipient: $messageBody"
        return response
    }

    /**
     * GET endpoint that returns a list of available endpoints.
     * URL: /endpoints
     */
    @JvmStatic
    @RestMethod("/endpoints")
    fun listEndpoints(): HttpResponse {
        val endpoints = """
        {
            "endpoints": [
                {"method": "GET", "path": "/hello", "description": "Simple greeting"},
                {"method": "GET", "path": "/hello/{name}", "description": "Personalized greeting"},
                {"method": "GET", "path": "/user/{id}", "description": "Get user information"},
                {"method": "POST", "path": "/message/{recipient}", "description": "Send a message"},
                {"method": "GET", "path": "/endpoints", "description": "List all endpoints"}
            ]
        }
        """.trimIndent()
        
        val response = HttpResponse()
        response.status = 200
        response.body = endpoints
        response.contentType = "application/json"
        return response
    }
}