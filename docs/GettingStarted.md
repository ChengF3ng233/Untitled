# Getting Started
目录：<br>
[配置开发环境](#配置开发环境-)<br>
[创建一个Module](#创建一个module)<br>
[使用Event](#使用event)<br>
[文本渲染](#文本渲染)<br>
[调用mc](#调用minecraftgetminecraft)<br>
[进阶教程](#进阶教程)<br>
[常见问题](#常见问题解决)

## 配置开发环境 
1. 添加如下jvm参数:
   `--add-opens java.base/jdk.internal.access=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/jdk.internal.misc=ALL-UNNAMED`
2. 从你的`.minecraft`文件夹复制`assets`文件夹到你的运行配置的Working Directory里面

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
     * 反注册
     */
    public void selfUnregister() {
        Client.instance.eventBus.unregister(this);
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

## 文本渲染
客户端内置了两种文本渲染器，分别为：
1. AWTFontRenderer
2. NanoFontRenderer

*注：<br>
AWTFont使用graphics绘制纹理贴图后渲染到屏幕上，优点是渲染快、占用低，缺点是加载慢，不灵活，字体大小样式不能随时调整。<br>
NanoFont为矢量绘制，优点是加载快、较灵活，发光效果效率高。缺点是大量渲染时占用较高。<br>
**需要特别注意的是，任何NanoVG相关操作都需要使用NanoUtil封装的方法或者NanoVG类中的方法执行，包括`Scale`, `Translate`, `Scissor`等<br>
除Widget外，所有NanoVG的绘制必须包含在NanoVG.beginFrame()和NanoVG.endFrame()之间<br>
想要进一步了解NanoVG在本项目中的使用，请转到[这里](Advanced.md)***
#### 使用AWTFontRenderer
```java
import cn.feng.untitled.ui.font.awt.FontLoader;

public void render() {
    // 遇到中文会自动使用支持中文的字体渲染
    FontLoader.greyCliff(16).drawString("Hello, graphics!你好世界！", textX, textY, textColor);
}
```
#### 使用NanoFontRenderer

```java
import cn.feng.untitled.ui.font.nano.NanoFontLoader;
import cn.feng.untitled.ui.font.nano.NanoFontRenderer;

public void render() {
    // 遇到中文或emoji会自动切换字体
    NanoFontRenderer fontRenderer = NanoFontLoader.script;
    fontRenderer.setSize(16);
    fontRenderer.drawString("Hello, NanoVG!你好世界！😀");
}
```
## 调用Minecraft.getMinecraft()
请让你的类继承 MinecraftInstance

## 进阶教程
想进一步了解此项目，请转到[这里](Advanced.md)

## 常见问题解决
### *Q:提示某些OpenGL方法不存在*
A:删除lwjgl2依赖
### *Q:提示某些类中的getter/setter不存在*
A:启用lombok注解，在编译项目时IDEA会自动提示你（右下角）
### *Q:怎么构建/怎么用启动器启动*
A:看[我的b站](https://space.bilibili.com/538684595)