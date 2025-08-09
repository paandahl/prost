package com.example.sample;

import com.tripfinger.commons.prost.annotations.RestMethod;
import com.tripfinger.commons.prost.model.HttpMethod;
import com.tripfinger.commons.prost.model.HttpResponse;

/**
 * Sample REST handler demonstrating basic usage of the Prost framework.
 * 
 * This class shows how to create REST endpoints using @RestMethod annotations,
 * handle different HTTP methods, use URL parameters, and return responses.
 */
public class SampleRestHandler {

    /**
     * Simple GET endpoint that returns a greeting.
     * Example: GET /hello
     */
    @RestMethod("/hello")
    public static HttpResponse hello() {
        return new HttpResponse(200, "Hello from Prost framework!");
    }

    /**
     * GET endpoint with URL parameter.
     * Example: GET /hello/World -> "Hello, World!"
     */
    @RestMethod("/hello/:name")
    public static HttpResponse helloName(String name) {
        return new HttpResponse(200, "Hello, " + name + "!");
    }

    /**
     * POST endpoint that echoes back the request body.
     * Example: POST /echo with body "test" -> "Echo: test"
     */
    @RestMethod(method = HttpMethod.POST, value = "/echo")
    public static HttpResponse echo(String body) {
        if (body == null || body.trim().isEmpty()) {
            return new HttpResponse(400, "Request body cannot be empty");
        }
        return new HttpResponse(200, "Echo: " + body);
    }

    /**
     * GET endpoint that returns application status.
     * Example: GET /status
     */
    @RestMethod("/status")
    public static HttpResponse status() {
        HttpResponse response = new HttpResponse();
        response.status = 200;
        response.body = "{\"status\": \"running\", \"framework\": \"Prost\", \"version\": \"sample\"}";
        response.contentType = "application/json";
        return response;
    }

    /**
     * GET endpoint with multiple URL parameters.
     * Example: GET /greet/John/Doe -> "Greetings, John Doe!"
     */
    @RestMethod("/greet/:firstName/:lastName")
    public static HttpResponse greetFullName(String firstName, String lastName) {
        return new HttpResponse(200, "Greetings, " + firstName + " " + lastName + "!");
    }

    /**
     * POST endpoint demonstrating JSON response format.
     * Example: POST /user with body "John" -> JSON response
     */
    @RestMethod(method = HttpMethod.POST, value = "/user")
    public static HttpResponse createUser(String body) {
        if (body == null || body.trim().isEmpty()) {
            return new HttpResponse(400, "Username is required");
        }
        
        String username = body.trim();
        String jsonResponse = String.format(
            "{\"id\": %d, \"username\": \"%s\", \"created\": true}", 
            System.currentTimeMillis() % 10000, 
            username
        );
        
        return new HttpResponse(201, jsonResponse);
    }
}