package com.example.sample;

import com.tripfinger.commons.prost.model.HttpResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Simple test to validate the sample REST handler methods work correctly.
 * This tests the static methods directly without requiring servlet setup.
 */
public class SampleRestHandlerTest {

    @Test
    public void testHelloMethod() {
        HttpResponse response = SampleRestHandler.hello();
        
        assertEquals(200, response.status);
        assertEquals("Hello from Prost framework!", response.body);
        assertEquals("application/json", response.contentType);
    }

    @Test
    public void testHelloNameMethod() {
        HttpResponse response = SampleRestHandler.helloName("World");
        
        assertEquals(200, response.status);
        assertEquals("Hello, World!", response.body);
    }

    @Test
    public void testEchoMethod() {
        HttpResponse response = SampleRestHandler.echo("Test message");
        
        assertEquals(200, response.status);
        assertEquals("Echo: Test message", response.body);
    }

    @Test
    public void testEchoMethodWithEmptyBody() {
        HttpResponse response = SampleRestHandler.echo("");
        
        assertEquals(400, response.status);
        assertEquals("Request body cannot be empty", response.body);
    }

    @Test
    public void testStatusMethod() {
        HttpResponse response = SampleRestHandler.status();
        
        assertEquals(200, response.status);
        assertEquals("application/json", response.contentType);
        assertEquals("{\"status\": \"running\", \"framework\": \"Prost\", \"version\": \"sample\"}", response.body);
    }

    @Test
    public void testGreetFullNameMethod() {
        HttpResponse response = SampleRestHandler.greetFullName("John", "Doe");
        
        assertEquals(200, response.status);
        assertEquals("Greetings, John Doe!", response.body);
    }

    @Test
    public void testCreateUserMethod() {
        HttpResponse response = SampleRestHandler.createUser("testuser");
        
        assertEquals(201, response.status);
        assertTrue("Response should contain username", response.body.contains("testuser"));
        assertTrue("Response should contain created field", response.body.contains("\"created\": true"));
        assertTrue("Response should contain id field", response.body.contains("\"id\":"));
    }

    @Test
    public void testCreateUserMethodWithEmptyBody() {
        HttpResponse response = SampleRestHandler.createUser("");
        
        assertEquals(400, response.status);
        assertEquals("Username is required", response.body);
    }
}