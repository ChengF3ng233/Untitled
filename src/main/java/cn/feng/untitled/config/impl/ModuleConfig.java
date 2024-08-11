package cn.feng.untitled.config.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.config.Config;
import cn.feng.untitled.config.ConfigManager;
import cn.feng.untitled.module.Module;
import cn.feng.untitled.util.misc.Logger;
import cn.feng.untitled.value.Value;
import cn.feng.untitled.value.impl.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

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
                if (!valueObject.has(value.name)) continue;
                JsonElement valueElement = valueObject.get(value.name);

                if (value instanceof BoolValue bv) {
                    bv.value = valueElement.getAsBoolean();
                } else if (value instanceof NumberValue nv) {
                    nv.value = valueElement.getAsDouble();
                } else if (value instanceof ModeValue mv) {
                    mv.value = valueElement.getAsString();
                } else if (value instanceof StringValue sv) {
                    sv.value = valueElement.getAsString();
                } else if (value instanceof ColorValue cv) {
                    JsonObject colorObject = valueElement.getAsJsonObject();
                    cv.rainbow.value = colorObject.get("rainbow").getAsBoolean();
                    cv.fade.value = colorObject.get("fade").getAsBoolean();
                    cv.setColor(colorObject.get("hue").getAsFloat(), colorObject.get("saturation").getAsFloat(), colorObject.get("brightness").getAsFloat(), colorObject.get("opacity").getAsFloat());
                    cv.speed.value = colorObject.get("speed").getAsDouble();
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
                    valueObject.addProperty(bv.name, bv.value);
                } else if (value instanceof NumberValue nv) {
                    valueObject.addProperty(nv.name,nv.value);
                } else if (value instanceof ModeValue mv) {
                    valueObject.addProperty(mv.name, mv.value);
                } else if (value instanceof StringValue sv) {
                    valueObject.addProperty(sv.name, sv.value);
                } else if (value instanceof ColorValue cv) {
                    JsonObject colorObject = new JsonObject();
                    colorObject.addProperty("rainbow", cv.rainbow.value);
                    colorObject.addProperty("fade", cv.fade.value);
                    colorObject.addProperty("hue", cv.hue.value);
                    colorObject.addProperty("saturation", cv.saturation.value);
                    colorObject.addProperty("brightness", cv.brightness.value);
                    colorObject.addProperty("opacity", cv.opacity.value);
                    colorObject.addProperty("speed", cv.speed.value);
                    valueObject.add(cv.name, colorObject);
                }
            }
            moduleObject.add("value", valueObject);
            config.add(module.name, moduleObject);
        }

        return config;
    }
}
