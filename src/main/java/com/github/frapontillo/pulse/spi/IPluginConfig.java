package com.github.frapontillo.pulse.spi;

import com.google.gson.JsonElement;

/**
 * Interface for {@link IPlugin} configuration classes.
 *
 * @author Francesco Pontillo
 */
public interface IPluginConfig<T> {
    /**
     * Delegate building the actual configuration to the instance {@link
     * #buildFromJsonElement(JsonElement)} method.
     *
     * @param obj  The instance that will be used to convert from JSON.
     * @param json The root {@link JsonElement} to convert.
     * @param <T>  Generic type of the input instance.
     *
     * @return The converted object, as the same type as the input instance.
     */
    static <T extends IPluginConfig<T>> T buildFromJson(T obj, JsonElement json) {
        return obj.buildFromJsonElement(json);
    }

    T buildFromJsonElement(JsonElement json);
}
