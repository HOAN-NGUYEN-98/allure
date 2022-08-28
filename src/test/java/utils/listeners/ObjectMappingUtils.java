package utils.listeners;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class ObjectMappingUtils {
    public ObjectMappingUtils() {
    }

    public static String parseModelToJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.serializeNulls().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public static <T> Object parseJsonToModel(String json, Class<T> c) {
        JsonDeserializer<T> deserializer = new JsonDeserializer<T>() {
            public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return json.isJsonNull() ? null : (new Gson()).fromJson(json, typeOfT);
            }
        };
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(c, deserializer);
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json, c);
    }

    public static String parseModelToJsonExcludingField(Object o, final String field) {
        Gson gson = (new GsonBuilder()).addSerializationExclusionStrategy(new ExclusionStrategy() {
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().toLowerCase().contains(field);
            }

            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).serializeNulls().create();
        return gson.toJson(o);
    }
}

