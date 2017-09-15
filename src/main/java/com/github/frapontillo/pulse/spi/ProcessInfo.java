package com.github.frapontillo.pulse.spi;

/**
 * Process information.
 *
 * @author Francesco Pontillo
 */
public class ProcessInfo {
    private String name;
    private String logs;

    /**
     * Get the name of the process.
     *
     * @return The name of the process.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the process.
     *
     * @param name The name of the process.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the log path for the process.
     *
     * @return The log path for the process.
     */
    public String getLogs() {
        return logs;
    }

    /**
     * Set the log path for the process.
     *
     * @param logs The log path for the process.
     */
    public void setLogs(String logs) {
        this.logs = logs;
    }
}
