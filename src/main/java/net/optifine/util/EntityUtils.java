package net.optifine.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.src.Config;

import java.util.HashMap;
import java.util.Map;

public class EntityUtils {
    private static final Map<Class, Integer> mapIdByClass = new HashMap();
    private static final Map<String, Integer> mapIdByName = new HashMap();
    private static final Map<String, Class> mapClassByName = new HashMap();

    static {
        for (int i = 0; i < 1000; ++i) {
            Class oclass = EntityList.getClassFromID(i);

            if (oclass != null) {
                String s = EntityList.getStringFromID(i);

                if (s != null) {
                    if (mapIdByClass.containsKey(oclass)) {
                        Config.warn("Duplicate entity class: " + oclass + ", id1: " + mapIdByClass.get(oclass) + ", id2: " + i);
                    }

                    if (mapIdByName.containsKey(s)) {
                        Config.warn("Duplicate entity name: " + s + ", id1: " + mapIdByName.get(s) + ", id2: " + i);
                    }

                    if (mapClassByName.containsKey(s)) {
                        Config.warn("Duplicate entity name: " + s + ", class1: " + mapClassByName.get(s) + ", class2: " + oclass);
                    }

                    mapIdByClass.put(oclass, i);
                    mapIdByName.put(s, i);
                    mapClassByName.put(s, oclass);
                }
            }
        }
    }

    public static int getEntityIdByClass(Entity entity) {
        return entity == null ? -1 : getEntityIdByClass(entity.getClass());
    }

    public static int getEntityIdByClass(Class cls) {
        Integer integer = mapIdByClass.get(cls);
        return integer == null ? -1 : integer;
    }

    public static int getEntityIdByName(String name) {
        Integer integer = mapIdByName.get(name);
        return integer == null ? -1 : integer;
    }

    public static Class getEntityClassByName(String name) {
        Class oclass = mapClassByName.get(name);
        return oclass;
    }
}
