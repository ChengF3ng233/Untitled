# Untitled
#### 小孩子不懂事，写着玩的。

---
## 已实现的功能
- 客户端基本框架（命令系统、事件系统、配置系统、模块系统、HUD系统、参数系统）
- 跨版本
- 使用java17以支持跨版本到1.21
- 升级到lwjgl3

---
## 待实现的功能
- 渲染优化
- 依赖库更新
- 编写客户端json以减小客户端体积
- HUD组件实现
- 命令实现
- 绘制ClickGUI

---
## 开发指南
在`File--Settings--Build,Execution,Deployment--Compiler--Java Compiler--Override compiler parameters per module`
添加`Untitled-main`(Module名称)，并添加如下jvm参数:
- `--add-opens
java.base/jdk.internal.access=ALL-UNNAMED`

- `--add-opens
java.base/java.nio=ALL-UNNAMED`

- `--add-opens
java.base/jdk.internal.misc=ALL-UNNAMED`

在运行配置中添加如下jvm参数:
- `-Djava.library.path=native`(自行配置运行环境，native可替换为你自己存放dll的文件夹)
