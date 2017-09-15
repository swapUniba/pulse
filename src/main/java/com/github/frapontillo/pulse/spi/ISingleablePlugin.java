package com.github.frapontillo.pulse.spi;

/**
 * Simple abstract class, child of {@link IPlugin}, that exposes a method for processing single
 * elements, beyond regular {@link rx.Observable} streams.
 * <p>
 * This kind of class can be useful when writing a meta-implementation of an {@link IPlugin} that
 * may rely on one or more different implementations to delegate the actual calls to.
 * <p>
 * This "simpler" plugin emits objects of the same class as the input ones.
 *
 * @author Francesco Pontillo
 */
public abstract class ISingleablePlugin<InputOutput, Parameter extends IPluginConfig<Parameter>>
        extends IPlugin<InputOutput, InputOutput, Parameter> {

    /**
     * Process a single element according to the task of this {@link IPlugin}.
     * This method may be re-used by actual implementations of {@link #getOperator(IPluginConfig)},
     * or different overridden methods.
     *
     * @param object The input element, of generic class {@link InputOutput}.
     *
     * @return The processed input element, eventually modified.
     */
    public abstract InputOutput singleItemProcess(InputOutput object);

}
