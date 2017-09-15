package com.github.frapontillo.pulse.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility logger using Log4J.
 *
 * @author Francesco Pontillo
 */
public class PulseLogger {

    /**
     * Return a {@link Logger} with a given input name.
     * The logger will read its configuration file from <code>log4j2.json</code>.
     *
     * @param loggerName The name to assign to the {@link Logger}.
     *
     * @return The appropriate {@link Logger} implementation.
     */
    public static Logger getLogger(String loggerName) {
        return LogManager.getLogger(loggerName);
    }

    /**
     * Return a {@link Logger} from any {@link Class}, using the class package name.
     *
     * @param clazz The {@link Class} to retrieve the logger for.
     *
     * @return The appropriate {@link Logger} implementation.
     * @see PulseLogger#getLogger(String)
     */
    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getPackage().getName());
    }

    /**
     * Return a {@link Logger} from any object, using the object's class package name.
     *
     * @param context The object to retrieve the logger for.
     *
     * @return The appropriate {@link Logger} implementation.
     * @see PulseLogger#getLogger(String)
     */
    public static Logger getLogger(Object context) {
        return getLogger(context.getClass());
    }

}
