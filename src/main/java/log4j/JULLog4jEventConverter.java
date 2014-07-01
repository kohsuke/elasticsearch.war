/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable
 * law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 * for the specific language governing permissions and limitations under the License.
 */
package log4j;

import java.util.Properties;
import java.util.logging.LogRecord;

import log4j.JULLevelConverter;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

/**
 * Converter class that can convert a {@link LogRecord} instance into a log4j {@link LoggingEvent}.
 * 
 * @author psmith
 */
public class JULLog4jEventConverter {
private static final String UNKNOWN_LOGGER_NAME = "unknown.jul.logger";

    /**
     * A default implementation of a converter that converts:
     * <li> FINEST -> TRACE</li>
     * <li> FINER, FINE -> DEBUG</li>
     * <li> INFO -> INFO</li>
     * <li> WARNING -> WARN</li>
     * <li> SEVERE -> ERROR</li>
     * <li> ALL -> ALL</li>
     * <li> OFF -> OFF</li>
     * Anything else is defaulted to DEBUG
     */
    private static final class DefaultLevelConverter implements JULLevelConverter {

        public Level convertJuliLevel(java.util.logging.Level juliLevel) {

            if (juliLevel.equals(java.util.logging.Level.FINEST)) {
                return Level.TRACE;
            } else if (juliLevel.equals(java.util.logging.Level.FINER)) {
                return Level.DEBUG;
            } else if (juliLevel.equals(java.util.logging.Level.FINE)) {
                return Level.DEBUG;
            } else if (juliLevel.equals(java.util.logging.Level.INFO)) {
                return Level.INFO;
            } else if (juliLevel.equals(java.util.logging.Level.WARNING)) {
                return Level.WARN;
            } else if (juliLevel.equals(java.util.logging.Level.SEVERE)) {
                return Level.ERROR;
            } else if (juliLevel.equals(java.util.logging.Level.ALL)) {
                return Level.ALL;
            } else if (juliLevel.equals(java.util.logging.Level.OFF)) {
                return Level.OFF;
            }
            return Level.DEBUG;

        }

        public java.util.logging.Level convertLog4jLevel(Level log4jLevel) {
            if (log4jLevel.equals(Level.TRACE)) {
                return java.util.logging.Level.FINEST;
            } else if (log4jLevel.equals(Level.DEBUG)) {
                return java.util.logging.Level.FINER;
            } else if (log4jLevel.equals(Level.INFO)) {
                return java.util.logging.Level.INFO;
            } else if (log4jLevel.equals(Level.WARN)) {
                return java.util.logging.Level.WARNING;
            } else if (log4jLevel.equals(Level.ERROR)) {
                return java.util.logging.Level.SEVERE;
            } else if (log4jLevel.equals(Level.FATAL)) {
                return java.util.logging.Level.SEVERE;
            } else if (log4jLevel.equals(Level.ALL)) {
                return java.util.logging.Level.ALL;
            } else if (log4jLevel.equals(Level.OFF)) {
                return java.util.logging.Level.OFF;
            }
            return java.util.logging.Level.FINE;
        }
    }

    private final LoggerRepository repository;
    private final JULLevelConverter levelConverter;

    public static final JULLevelConverter DEFAULT_LEVEL_CONVERTER = new DefaultLevelConverter();

    /**
     * Creates a default converter implementation that uses the default {@link LoggerRepository},
     * ad the default {@link JuliLevelConverter}
     */
    public JULLog4jEventConverter() {
        this(LogManager.getLoggerRepository());
    }

    public JULLog4jEventConverter(LoggerRepository repository) {
        this(repository, DEFAULT_LEVEL_CONVERTER);
    }

    public JULLog4jEventConverter(LoggerRepository repository, JULLevelConverter levelConverter) {
        this.repository = repository;
        this.levelConverter = levelConverter;
    }

    /**
     * Converts a {@link LogRecord} class into a {@link LoggingEvent} instance mapping the
     * loggernames identically.
     * 
     * @param record
     *            to convert
     * @return converted {@link LoggingEvent}
     */
    public LoggingEvent convert(LogRecord record) {
        String loggerName = record.getLoggerName();
        if (loggerName == null) {
    	    loggerName = UNKNOWN_LOGGER_NAME;
        }
        Logger logger = repository.getLogger(loggerName);
        String sourceClassName = record.getSourceClassName();
        String sourceMethodName = record.getSourceMethodName();
        LocationInfo locationInfo = new LocationInfo("?", sourceClassName, sourceMethodName, "?");
        String ndc = null;
        // TODO get thread name properly.
        String threadName = String.valueOf(record.getThreadID());
        ThrowableInformation throwableInformation = record.getThrown() == null ? null
                : new ThrowableInformation(record.getThrown());
        LoggingEvent event = new LoggingEvent(loggerName, logger, record.getMillis(),
                levelConverter.convertJuliLevel(record.getLevel()), record.getMessage(),
                threadName, throwableInformation, ndc, locationInfo, new Properties()

        );

        return event;
    }
}