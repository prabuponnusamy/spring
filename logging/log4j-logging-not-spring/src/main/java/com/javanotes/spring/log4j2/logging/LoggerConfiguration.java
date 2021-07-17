package com.javanotes.spring.log4j2.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerConfiguration
{

    private static final Logger logger = LogManager.getLogger(LoggerConfiguration.class);

    public static void main(final String[] args)
    {
        logger.info("Hello");
        logger.debug("Hello , debug");
    }
}
