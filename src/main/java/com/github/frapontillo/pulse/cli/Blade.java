package com.github.frapontillo.pulse.cli;

import java.io.FileNotFoundException;

/**
 * A simple implementation of {@link AbstractBlade} with default parameters.
 *
 * @author Francesco Pontillo
 */
public class Blade extends AbstractBlade<BladeParameters> {
    public Blade(String[] args) throws ClassNotFoundException, FileNotFoundException {
        super(args);
    }

    @Override protected BladeParameters getNewBladeParameters() {
        return new BladeParameters();
    }
}
