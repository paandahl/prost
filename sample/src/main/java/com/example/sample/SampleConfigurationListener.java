package com.example.sample;

import com.tripfinger.commons.prost.RequestHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Configuration listener that initializes the Prost RequestHandler
 * with our sample REST handler class.
 */
public class SampleConfigurationListener implements ServletContextListener {
    
    private RequestHandler requestHandler = new RequestHandler();

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Register our sample REST handler class with the Prost framework
        requestHandler.setRestHandler(SampleRestHandler.class);
        
        System.out.println("Prost Sample Application initialized successfully!");
        System.out.println("Available endpoints:");
        System.out.println("  GET  /hello");
        System.out.println("  GET  /hello/{name}");
        System.out.println("  POST /echo");
        System.out.println("  GET  /status");
        System.out.println("  GET  /greet/{firstName}/{lastName}");
        System.out.println("  POST /user");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Prost Sample Application shut down.");
    }
}