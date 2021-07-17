package com.javanotes.spring.log4j2.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App
{
    private static final Logger LOGGER = LogManager.getLogger(App.class);

    public static void main( final String[] args )
    {
        LOGGER.info("Entering main method");
        final App app = new App();
        app.doIt();
        LOGGER.trace("Existing main method");
    }

    public void doIt()
    {
        LOGGER.traceEntry("Entering do it");
        LOGGER.traceExit("Exiting do it");
    }
}
