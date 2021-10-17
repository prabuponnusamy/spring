package com.javanotes.spring.log4j2.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogException
{

    private static final Logger logger = LogManager.getLogger();

    public static void main(final String[] args)
    {
        final String name = "Prabu";
        logger.info("Name is {}", name);
        try {
            name.substring(6);
        } catch (final Exception e) {
            logger.atError().withThrowable(e).log("Not able to find index of {} in {}", "z", name);
        }
    }
}
