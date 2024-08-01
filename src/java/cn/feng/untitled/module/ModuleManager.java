package cn.feng.untitled.module;

import cn.feng.untitled.event.api.SubscribeEvent;
import cn.feng.untitled.event.impl.KeyEvent;
import cn.feng.untitled.module.impl.client.HUD;
import cn.feng.untitled.module.impl.movement.ToggleSprint;
import cn.feng.untitled.module.impl.client.ClickGUI;
import cn.feng.untitled.ui.widget.Widget;
import cn.feng.untitled.util.data.compare.CompareMode;
import cn.feng.untitled.util.data.compare.ModuleComparator;
import cn.feng.untitled.util.exception.ModuleNotFoundException;
import cn.feng.untitled.util.exception.ValueLoadException;
import cn.feng.untitled.util.misc.Logger;
import cn.feng.untitled.value.Value;
import cn.feng.untitled.value.impl.BoolValue;

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
        Module widgetModule = new Module(widget.name, ModuleCategory.Widget);
        widgetModule.enabled = widget.defaultOn;
        this.moduleList.add(widgetModule);

        for (Field field : widget.getClass().getDeclaredFields()) {
            addValue(field, widgetModule, widget);
        }
    }

    private void addValue(Field field, Module module, Object obj) {
        field.setAccessible(true);
        if (field.getType().getSuperclass() == Value.class) {
            try {
                module.valueList.add((Value<?>) field.get(obj));
            } catch (IllegalAccessException e) {
                throw new ValueLoadException("Failed to load " + module.name + ", " + field.getName());
            }
        }
    }

    private void register(Module module, Field[] classFields) {
        this.moduleList.add(module);

        for (Field field : classFields) {
            addValue(field, module, module);
        }
    }

    public void registerModules() {
        register(new ToggleSprint());
        register(new ClickGUI());
        register(new HUD());

        for (int i = 0; i < 10; i++) {
            Module test = new Module("Test" + i, ModuleCategory.Client);
            register(test);
            for (int j = 0; j < 4; j++) {
                test.valueList.add(new BoolValue("BoolValue" + j, false));
            }
        }
    }

    public Module getModule(Class<? extends Module> klass) {
        for (Module module : moduleList) {
            if (klass == module.getClass()) return module;
        }

        throw new ModuleNotFoundException(klass.getName());
    }

    public List<Module> getModuleByCategory(ModuleCategory category) {
        List<Module> list = new ArrayList<>(moduleList.stream().filter(it -> it.category == category).toList());
        if (!list.isEmpty()) {
            list.sort(new ModuleComparator(CompareMode.Alphabet));
        }
        return list;
    }

    public List<Module> getModuleByState(boolean enabled) {
        List<Module> list = new ArrayList<>(moduleList.stream().filter(it -> it.enabled = enabled).toList());
        if (!list.isEmpty()) {
            list.sort(new ModuleComparator(CompareMode.Length));
        }
        return list;
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
