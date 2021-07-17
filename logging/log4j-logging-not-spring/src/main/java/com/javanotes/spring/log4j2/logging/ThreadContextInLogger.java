package com.javanotes.spring.log4j2.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.UUID;

public class ThreadContextInLogger
{

    private static final Logger LOGGER = LogManager.getLogger(ThreadContextInLogger.class);

    public static void main(final String[] args)
    {
        ThreadContext.put("id", UUID.randomUUID().toString());
        LOGGER.debug("UID from the response {}", ThreadContext.get("id"));
        ThreadContext.clearAll();
    }
}
