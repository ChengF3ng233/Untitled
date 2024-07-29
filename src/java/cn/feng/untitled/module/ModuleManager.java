package cn.feng.untitled.module;

import cn.feng.untitled.event.api.SubscribeEvent;
import cn.feng.untitled.event.impl.KeyEvent;
import cn.feng.untitled.module.impl.movement.ToggleSprint;
import cn.feng.untitled.module.impl.render.ClickGUI;
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

    private void register(Module module) {
        this.moduleList.add(module);

        for (Field field : module.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType() == Value.class) {
                try {
                    module.valueList.add((Value<?>) field.get(module));
                } catch (IllegalAccessException e) {
                    throw new ValueLoadException("Failed to load " + module.name  + ", " + field.getName());
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

    @SubscribeEvent
    private void onKey(KeyEvent event) {
        moduleList.stream().filter(module -> module.key == event.key).forEach(Module::toggle);
    }
}
