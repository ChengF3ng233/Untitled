# Untitled
***小孩子不懂事，写着玩的。***

---
## 基本内容
- 客户端基本框架（命令系统、事件系统、配置系统、模块系统、HUD系统、参数系统）
- 客户端优化
- 跨版本(1.7-1.21)
- java17
- lwjgl3

---
## 待实现的功能
- 启动界面（不是原神）
- 修复netty不能用直接缓冲区的问题

---
## 开发指南
1. 自行添加mc自带依赖库，删除lwjgl2
2. 添加如下jvm参数:
- `--add-opens
java.base/jdk.internal.access=ALL-UNNAMED`

- `--add-opens
java.base/java.nio=ALL-UNNAMED`

- `--add-opens
java.base/jdk.internal.misc=ALL-UNNAMED`
