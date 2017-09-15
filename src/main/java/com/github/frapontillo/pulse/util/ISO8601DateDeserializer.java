package com.github.frapontillo.pulse.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * GSON deserializer for ISO-8601 standard dates into Java {@link Date}s.
 *
 * @author Francesco Pontillo
 */
public class ISO8601DateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String value = json.getAsString();
        if (value == null) {
            return null;
        }
        return new DateTime(value).toDate();
    }
}
