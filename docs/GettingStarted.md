## 配置开发环境
1. 自行添加mc自带依赖库，删除lwjgl2
2. 添加如下jvm参数:
- `--add-opens
  java.base/jdk.internal.access=ALL-UNNAMED`
- `--add-opens
  java.base/java.nio=ALL-UNNAMED`
- `--add-opens
  java.base/jdk.internal.misc=ALL-UNNAMED`

## 创建一个Module
以下是一个示例：

```java
package cn.feng.untitled.module.impl.movement;

import cn.feng.untitled.module.Module;
import cn.feng.untitled.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

/**
 * @author ChengFeng
 * @since 2024/7/28
 **/
public class ToggleSprint extends Module {

    /**
     * Module类有多种构造器
     * @see Module
     */
    public ToggleSprint() {
        super("ToggleSprint", ModuleCategory.Movement, Keyboard.getKeyIndex("V"));
    }

    @Override
    public void onEnable() {
        // 你的代码
    }

    @Override
    public void onDisable() {
        // 你的代码
    }
}
```

## 使用Event
客户端内置了一个EventBus

```java
import cn.feng.untitled.Client;
import cn.feng.untitled.event.api.SubscribeEvent;
import cn.feng.untitled.event.impl.UpdateEvent;

public class YourClass {
    /**
     * 使用 EventBus 注册实例
     */
    public void selfRegister() {
        Client.instance.eventBus.register(this);
    }

    /**
     * 监听事件
     */
    @SubscribeEvent
    private void onUpdate(UpdateEvent event) {
        // 你的代码
    }
}
```

## 常见问题解决
### *Q:提示某些OpenGL方法不存在*
A:删除lwjgl2依赖
### *Q:提示某些类中的getter/setter不存在*
A:启用lombok注解，IDEA会自动提示你
### *Q:怎么构建/怎么用启动器启动*
A:b站搜索全民写端，工件导出时依赖库只需添加lib/included目录下的jar