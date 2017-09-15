package com.github.frapontillo.pulse.spi;

import com.google.gson.JsonElement;

/**
 * A basic void configuration for plugins that have no use for configurations.
 *
 * @author Francesco Pontillo
 */
public class VoidConfig implements IPluginConfig<VoidConfig> {
    @Override public VoidConfig buildFromJsonElement(JsonElement json) {
        return PluginConfigHelper.buildFromJson(json, VoidConfig.class);
    }
}
