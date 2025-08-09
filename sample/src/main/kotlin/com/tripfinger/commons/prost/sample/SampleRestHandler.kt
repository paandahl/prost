package com.tripfinger.commons.prost.sample

import com.tripfinger.commons.prost.annotations.RestMethod
import com.tripfinger.commons.prost.model.HttpMethod
import com.tripfinger.commons.prost.model.HttpResponse

/**
 * Sample REST handler demonstrating the prost framework.
 * This object contains REST endpoints that will be exposed by the web server.
 */
object SampleRestHandler {

    /**
     * Simple Hello World endpoint.
     * GET /hello
     */
    @JvmStatic
    @RestMethod("/hello")
    fun hello(): HttpResponse {
        return HttpResponse(200, "Hello, World! 🌍")
    }

    /**
     * Personalized greeting endpoint with URL parameter.
     * GET /hello/:name
     */
    @JvmStatic
    @RestMethod("/hello/:name")
    fun helloName(name: String): HttpResponse {
        return HttpResponse(200, "Hello, $name! 👋")
    }

    /**
     * Server status endpoint.
     * GET /status
     */
    @JvmStatic
    @RestMethod("/status")
    fun status(): HttpResponse {
        val response = HttpResponse()
        response.status = 200
        response.body = """
            {
                "status": "OK",
                "message": "Prost framework is running!",
                "framework": "prost",
                "version": "0.2",
                "endpoints": [
                    "GET /hello",
                    "GET /hello/:name",
                    "GET /status",
                    "POST /echo"
                ]
            }
        """.trimIndent()
        response.contentType = "application/json"
        return response
    }

    /**
     * Echo endpoint that returns the request body.
     * POST /echo
     */
    @JvmStatic
    @RestMethod(method = HttpMethod.POST, value = "/echo")
    fun echo(body: String): HttpResponse {
        val response = HttpResponse()
        response.status = 200
        response.body = """
            {
                "echo": "$body",
                "timestamp": "${System.currentTimeMillis()}",
                "message": "This is your message echoed back!"
            }
        """.trimIndent()
        response.contentType = "application/json"
        return response
    }
}