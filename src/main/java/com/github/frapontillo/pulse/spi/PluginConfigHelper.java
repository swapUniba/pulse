package com.github.frapontillo.pulse.spi;

import com.github.frapontillo.pulse.util.ISO8601DateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.Date;

/**
 * Helper to convert {@link IPluginConfig} implementations from {@link JsonElement}s.
 *
 * @author Francesco Pontillo
 */
public class PluginConfigHelper {
    private static final Gson gson =
            new GsonBuilder().registerTypeAdapter(Date.class, new ISO8601DateDeserializer())
                             .create();

    /**
     * Build an instance of {@link IPluginConfig} from a {@link JsonElement}.
     *
     * @param json     The {@link JsonElement} to convert.
     * @param classOfT The class of the object to convert into.
     * @param <T>      The generic type of the object to convert into.
     *
     * @return An object of type {@link T} converted from the input JSON.
     */
    public static <T extends IPluginConfig> T buildFromJson(JsonElement json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}
