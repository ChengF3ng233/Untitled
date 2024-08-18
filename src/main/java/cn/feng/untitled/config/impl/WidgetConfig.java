package cn.feng.untitled.config.impl;

import cn.feng.untitled.Client;
import cn.feng.untitled.config.Config;
import cn.feng.untitled.ui.widget.Widget;
import com.google.gson.JsonObject;

/**
 * @author ChengFeng
 * @since 2024/8/18
 **/
public class WidgetConfig extends Config {
    public WidgetConfig() {
        super("widget", "Widget.json");
    }

    @Override
    public void loadConfig(JsonObject object) {
        for (Widget widget : Client.instance.uiManager.widgetList) {
            if (object.has(widget.name)) {
                JsonObject obj = object.get(widget.name).getAsJsonObject();
                widget.x = obj.get("x").getAsFloat();
                widget.y = obj.get("y").getAsFloat();
                widget.width = obj.get("width").getAsFloat();
                widget.height = obj.get("height").getAsFloat();
            }
        }
    }

    @Override
    public JsonObject saveConfig() {
        JsonObject object = new JsonObject();
        for (Widget widget : Client.instance.uiManager.widgetList) {
            JsonObject widgetObj = new JsonObject();
            widgetObj.addProperty("x", widget.x);
            widgetObj.addProperty("y", widget.y);
            widgetObj.addProperty("width", widget.width);
            widgetObj.addProperty("height", widget.height);
            object.add(widget.name, widgetObj);
        }
        return object;
    }
}
