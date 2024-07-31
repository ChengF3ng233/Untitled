package cn.feng.untitled.module;

import cn.feng.untitled.event.api.SubscribeEvent;
import cn.feng.untitled.event.impl.KeyEvent;
import cn.feng.untitled.module.impl.movement.ToggleSprint;
import cn.feng.untitled.module.impl.client.ClickGUI;
import cn.feng.untitled.ui.hud.Widget;
import cn.feng.untitled.util.exception.ModuleNotFoundException;
import cn.feng.untitled.util.exception.ValueLoadException;
import cn.feng.untitled.value.Value;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ModuleManager {
    public List<Module> moduleList;

    public ModuleManager() {
        this.moduleList = new ArrayList<>();
    }

    /**
     * Register module
     * @param module module
     */
    private void register(Module module) {
        register(module, module.getClass().getDeclaredFields());
    }

    /**
     * Register widget as a module
     * @param widget widget
     */
    public void register(Widget widget) {
        Module widgetModule = new Module(widget.name, ModuleCategory.WIDGET);
        widgetModule.enabled = widget.defaultOn;
        register(widgetModule, widget.getClass().getDeclaredFields());
    }

    private void register(Module module, Field[] classFields) {
        this.moduleList.add(module);

        for (Field field : classFields) {
            field.setAccessible(true);
            if (field.getType() == Value.class) {
                try {
                    module.valueList.add((Value<?>) field.get(module));
                } catch (IllegalAccessException e) {
                    throw new ValueLoadException("Failed to load " + module.name + ", " + field.getName());
                }
            }
        }
    }

    public void registerModules() {
        register(new ToggleSprint());
        register(new ClickGUI());
    }

    public Module getModule(Class<? extends Module> klass) {
        for (Module module : moduleList) {
            if (klass == module.getClass()) return module;
        }

        throw new ModuleNotFoundException(klass.getName());
    }

    public Module getModule(Widget widget) {
        for (Module module : moduleList) {
            if (widget.name.equals(module.name)) return module;
        }

        throw new ModuleNotFoundException(widget.name);
    }

    @SubscribeEvent
    private void onKey(KeyEvent event) {
        moduleList.stream().filter(module -> module.key == event.key).forEach(Module::toggle);
    }
}
