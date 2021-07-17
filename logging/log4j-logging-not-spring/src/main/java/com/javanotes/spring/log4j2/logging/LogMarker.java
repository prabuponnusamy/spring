package com.javanotes.spring.log4j2.logging;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class LogMarker
{

    private static final Logger logger = LogManager.getLogger(LogMarker.class);

    private static final Marker INSERT_MARKER = MarkerManager.getMarker("Insert marker");

    private static final Marker UPDATE_MARKER = MarkerManager.getMarker("Insert marker");

    private static final Marker DELETE_MARKER = MarkerManager.getMarker("Insert marker");

    public static void main(final String[] args)
    {
        final LogMarker logMarker = new LogMarker();
        logMarker.insertRecord();
    }

    public void insertRecord()
    {
        logger.info(INSERT_MARKER, "Inserting record");
    }

}
