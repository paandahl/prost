package com.tripfinger.commons.prost.sample

import com.tripfinger.commons.prost.RequestHandler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

/**
 * Main application class that starts an embedded Jetty server
 * with the prost framework REST handler.
 */
class WebServerApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val app = WebServerApp()
            app.start()
        }
    }

    fun start() {
        // Create Jetty server on port 8080
        val server = Server(8080)

        // Create servlet context
        val context = ServletContextHandler(ServletContextHandler.SESSIONS)
        context.contextPath = "/"

        // Configure the prost RequestHandler
        val requestHandler = RequestHandler()
        requestHandler.setRestHandler(SampleRestHandler::class.java)

        // Add the servlet to handle all requests
        val servletHolder = ServletHolder(requestHandler)
        context.addServlet(servletHolder, "/*")

        // Set the handler and start the server
        server.handler = context

        try {
            server.start()
            println("🚀 Prost web server started successfully!")
            println("📍 Server running at: http://localhost:8080")
            println("🔍 Try: http://localhost:8080/hello")
            println("🔍 Try: http://localhost:8080/hello/YourName")
            println("🔍 Try: http://localhost:8080/status")
            println("🛑 Press Ctrl+C to stop the server")
            
            server.join()  // Wait for the server to be stopped
        } catch (e: Exception) {
            println("❌ Failed to start server: ${e.message}")
            e.printStackTrace()
        } finally {
            server.destroy()
        }
    }
}