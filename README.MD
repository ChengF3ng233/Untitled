# Untitled
***小孩子不懂事，写着玩的。***

---
## 基本内容
- 客户端基本框架（命令系统、事件系统、配置系统、模块系统、HUD系统、参数系统）
- 跨版本
- 使用java17以支持跨版本到1.21
- 升级到lwjgl3

---
## 待实现的功能（按优先级排列）
- 矢量文字渲染
- Splash Screen
- 多配置
- 命令实现
- Text组件
- ~~绘制ClickGUI~~
- ~~Arraylist~~

---
## 开发指南
1. 自行添加mc自带依赖库
2. 添加如下jvm参数:
- `--add-opens
java.base/jdk.internal.access=ALL-UNNAMED`

- `--add-opens
java.base/java.nio=ALL-UNNAMED`

- `--add-opens
java.base/jdk.internal.misc=ALL-UNNAMED`