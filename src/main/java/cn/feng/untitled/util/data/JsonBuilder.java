package cn.feng.untitled.util.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * @author ChengFeng
 * @since 2024/8/11
 **/
public class JsonBuilder {
    private final JsonObject object = new JsonObject();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonBuilder property(String key, String value) {
        object.addProperty(key, value);
        return this;
    }

    public JsonBuilder property(String key, Number value) {
        object.addProperty(key, value);
        return this;
    }

    public JsonBuilder property(String key, boolean value) {
        object.addProperty(key, value);
        return this;
    }

    public JsonBuilder property(String key, char value) {
        object.addProperty(key, value);
        return this;
    }

    public String get() {
        return gson.toJson(object);
    }
}
