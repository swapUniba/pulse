package com.github.frapontillo.pulse.util;

import java.lang.management.ManagementFactory;

/**
 * @author Francesco Pontillo
 */
public class ProcessUtil {
    public static Integer getPid() {
        return Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
    }
}
