package com.tripfinger.commons.prost

import com.tripfinger.commons.prost.model.Authorizer
import com.tripfinger.commons.prost.model.HttpMethod
import com.tripfinger.commons.prost.model.HttpResponse
import org.junit.Before
import org.junit.Test
import java.util.*

import org.junit.Assert.assertEquals

class RequestHandlerTest {

    companion object {
        @JvmStatic
        protected val requestHandler = RequestHandler()
    }

    @Before
    fun setUp() {
        RequestHandler.restHandlers = HashMap()
        RequestHandler.authorizer = null
        RequestHandler.guardedMethods = HashSet()
    }

    @Test
    fun testRequestHandler() {
        requestHandler.setRestHandler(RestHandler::class.java)

        val parameters = HashMap<String, String>()
        var pathElements = listOf("apple")
        var response = requestHandler.handleRequest(pathElements, parameters)
        assertEquals(200, response.status)
        assertEquals(String.format("{\"status\": %d, \"message\": \"%s\", \"id\": \"null\"}", 200, "Apple"), response.body)

        pathElements = listOf("hello", "Boy")
        response = requestHandler.handleRequest(pathElements, parameters)
        assertEquals(200, response.status)
        assertEquals("Hello, Boy", response.body)

        pathElements = listOf("bye", "Mama")
        response = requestHandler.handleRequest(pathElements, "3")
        assertEquals(200, response.status)
        assertEquals("Bye, bye, bye, Mama", response.body)
    }

    private class SimpleAuthorizer : Authorizer {
        override fun isAuthorized(): Boolean {
            return false
        }
    }

    @Test
    fun testHttpMethodGuards() {
        requestHandler.setRestHandler(RestHandler::class.java)
        requestHandler.setAuthorizer(SimpleAuthorizer())
        requestHandler.addMethodGuard(HttpMethod.GET)

        val parameters = HashMap<String, String>()
        val pathElements = listOf("hello", "Boy")
        val response = requestHandler.handleRequest(pathElements, parameters)
        assertEquals(401, response.status)
    }

    @Test
    fun testMethodGuards() {
        requestHandler.setRestHandler(RestHandler::class.java)
        requestHandler.setAuthorizer(SimpleAuthorizer())

        val parameters = HashMap<String, String>()
        val pathElements = listOf("apple2")
        val response = requestHandler.handleRequest(pathElements, parameters)
        assertEquals(401, response.status)
    }

    @Test
    fun testClassGuards() {
        requestHandler.setRestHandler(GuardedRestHandler::class.java)
        requestHandler.setAuthorizer(SimpleAuthorizer())

        val parameters = HashMap<String, String>()
        var pathElements = listOf("apple")
        var response = requestHandler.handleRequest(pathElements, parameters)
        assertEquals(200, response.status)

        pathElements = listOf("hello", "Boy")
        response = requestHandler.handleRequest(pathElements, parameters)
        assertEquals(401, response.status)
    }
}