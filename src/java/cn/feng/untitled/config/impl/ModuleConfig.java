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
    public void loadConfig() {
        if (!configFile.exists()) return;
        try {
            JsonObject config = ConfigManager.gson.fromJson(new FileReader(configFile), JsonObject.class);
            for (Module module : Client.instance.moduleManager.moduleList) {
                if (!config.has(module.name)) continue;
                JsonObject moduleObject = config.get(module.name).getAsJsonObject();

                if (moduleObject.has("enabled") && module.enabled != moduleObject.get("enabled").getAsBoolean()) module.toggle();
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
                        cv.setValue(colorObject.get("hue").getAsFloat(), colorObject.get("saturation").getAsFloat(), colorObject.get("brightness").getAsFloat());
                    }

                }
            }
        } catch (FileNotFoundException e) {
            Logger.error("Failed to load " + name + " config.");
        }
    }

    @Override
    public void saveConfig() {
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
                    colorObject.addProperty("hue", cv.hue);
                    colorObject.addProperty("saturation", cv.saturation);
                    colorObject.addProperty("brightness", cv.brightness);
                    valueObject.add(cv.name, colorObject);
                }
            }
            moduleObject.add("value", valueObject);
            config.add(module.name, moduleObject);
        }

        try {
            PrintWriter pw = new PrintWriter(configFile);
            pw.write(ConfigManager.gson.toJson(config));
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            Logger.error("Failed to write " + name + " config.");
        }
    }
}
