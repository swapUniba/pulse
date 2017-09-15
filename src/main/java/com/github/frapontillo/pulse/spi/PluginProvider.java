package com.github.frapontillo.pulse.spi;

import java.util.ServiceLoader;

/**
 * Basic provider for Pulse {@link IPlugin} implementations.
 * This provider simply retrieves the first {@link IPlugin} implementation whose name matches the
 * asked one.
 * <p>
 * TODO: the provider can be improved by implementing a criteria based system.
 *
 * @author Francesco Pontillo
 */
public class PluginProvider {
    private final static ServiceLoader<IPlugin> serviceLoader;

    static {
        serviceLoader = ServiceLoader.load(IPlugin.class);
    }

    /**
     * Get an actual instance of {@link IPlugin} by looking for its name among the candidates.
     *
     * @param name        The name of the plugin to look for.
     * @param <Input>     The class of the elements that flow into the plugin.
     * @param <Output>    The class of the elements that flow out of the plugin.
     * @param <Parameter> The class of the parameter object that configures the plugin.
     * @param <Plugin>    The class of the returned plugin.
     *
     * @return An instance of {@link Plugin}.
     * @throws ClassNotFoundException When no plugin implementation could be found for the specific
     *                                name.
     */
    public static <Input, Output, Parameter extends IPluginConfig<Parameter>, Plugin extends
            IPlugin<Input, Output, Parameter>> Plugin getPlugin(
            String name) throws ClassNotFoundException {
        for (IPlugin implementation : serviceLoader) {
            if (implementation.getName().equals(name)) {
                return (Plugin) implementation;
            }
        }
        throw new ClassNotFoundException(
                "Can't found a valid implementation for plugin named \"" + name + "\".");
    }
}
