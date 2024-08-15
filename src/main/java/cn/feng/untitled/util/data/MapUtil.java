package cn.feng.untitled.util.data;

import java.util.Map;

/**
 * @author ChengFeng
 * @since 2024/7/29
 **/
public class MapUtil {
    public static boolean isValid(Map<?, ?> map, Class<?> keyClass, Class<?> valueClass) {
        boolean valid = true;

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!(entry.getKey().getClass() == keyClass) || !(entry.getValue().getClass() == valueClass)) {
                valid = false;
                break;
            }
        }

        return valid;
    }
}
