package com.github.frapontillo.pulse.cli;

import com.beust.jcommander.Parameter;
import com.github.frapontillo.pulse.util.StringUtil;

/**
 * Command line parameters for the {@link AbstractBlade} runner, all optionals:
 * <ul>
 * <li>-c or --config, path to the configuration file; if not specified, the config file will be
 * read from the standard input</li>
 * <li>-l or --log, file path to save logs to</li>
 * </ul>
 *
 * @author Francesco Pontillo
 */
public class BladeParameters {

    @Parameter(names = {"-c", "--config"},
            description = "Path to the configuration file") private String file;

    @Parameter(names = {"-l", "--log"},
            description = "Path and name of the file log to create") private String log;

    public String getFile() {
        return file;
    }

    public String getLog() {
        return log;
    }

    public boolean hasFile() {
        return !StringUtil.isNullOrEmpty(file);
    }
}
