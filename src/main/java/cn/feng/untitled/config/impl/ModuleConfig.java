package cn.feng.untitled.config.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.config.Config;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.value.Value;
import cn.feng.untitled.value.impl.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class ModuleConfig extends Config {
    public ModuleConfig() {
        super("module", "Module.json");
    }

    @Override
    public void loadConfig(JsonObject config) {
        if (!configFile.exists()) return;

        for (Module module : Client.instance.moduleManager.moduleList) {
            if (!config.has(module.name)) continue;
            JsonObject moduleObject = config.get(module.name).getAsJsonObject();

            if (moduleObject.has("enabled") && module.enabled != moduleObject.get("enabled").getAsBoolean() && !module.locked) module.toggle();
            if (moduleObject.has("key")) module.key = moduleObject.get("key").getAsInt();

            if (!moduleObject.has("value")) continue;
            JsonObject valueObject = moduleObject.get("value").getAsJsonObject();

            for (Value<?> value : module.valueList) {
                if (!valueObject.has(value.getName())) continue;
                JsonElement valueElement = valueObject.get(value.getName());

                if (value instanceof BoolValue bv) {
                    bv.setValue(valueElement.getAsBoolean());
                } else if (value instanceof NumberValue nv) {
                    nv.setValue(valueElement.getAsDouble());
                } else if (value instanceof ModeValue mv) {
                    mv.setValue(valueElement.getAsString());
                } else if (value instanceof StringValue sv) {
                    sv.setValue(valueElement.getAsString());
                } else if (value instanceof ColorValue cv) {
                    JsonObject colorObject = valueElement.getAsJsonObject();
                    cv.rainbow.setValue(colorObject.get("rainbow").getAsBoolean());
                    cv.fade.setValue(colorObject.get("fade").getAsBoolean());
                    cv.setValue(colorObject.get("hue").getAsFloat(), colorObject.get("saturation").getAsFloat(), colorObject.get("brightness").getAsFloat(), colorObject.get("opacity").getAsFloat());
                    cv.speed.setValue(colorObject.get("speed").getAsDouble());
                }
            }
        }
    }

    @Override
    public JsonObject saveConfig() {
        JsonObject config = new JsonObject();

        for (Module module : Client.instance.moduleManager.moduleList) {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("enabled", module.enabled);
            moduleObject.addProperty("key", module.key);

            JsonObject valueObject = new JsonObject();

            for (Value<?> value : module.valueList) {
                if (value instanceof BoolValue bv) {
                    valueObject.addProperty(bv.getName(), bv.getValue());
                } else if (value instanceof NumberValue nv) {
                    valueObject.addProperty(nv.getName(), nv.getValue());
                } else if (value instanceof ModeValue mv) {
                    valueObject.addProperty(mv.getName(), mv.getValue());
                } else if (value instanceof StringValue sv) {
                    valueObject.addProperty(sv.getName(), sv.getValue());
                } else if (value instanceof ColorValue cv) {
                    JsonObject colorObject = new JsonObject();
                    colorObject.addProperty("rainbow", cv.rainbow.getValue());
                    colorObject.addProperty("fade", cv.fade.getValue());
                    colorObject.addProperty("hue", cv.hue.getValue());
                    colorObject.addProperty("saturation", cv.saturation.getValue());
                    colorObject.addProperty("brightness", cv.brightness.getValue());
                    colorObject.addProperty("opacity", cv.opacity.getValue());
                    colorObject.addProperty("speed", cv.speed.getValue());
                    valueObject.add(cv.getName(), colorObject);
                }
            }
            moduleObject.add("value", valueObject);
            config.add(module.name, moduleObject);
        }

        return config;
    }
}
