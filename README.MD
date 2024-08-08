# Untitled
***我的练手项目***

---
## 基本内容
- 客户端框架（命令系统、事件系统、配置系统、模块系统、HUD系统、参数系统）
- 客户端优化
- 跨版本(1.7-1.21)
- java17
- lwjgl3

---
## 待实现的功能
- 启动界面
- 修复netty不能用直接缓冲区的问题
- 更新依赖

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

---
## 常见问题解决
### Q:提示某些OpenGL方法不存在

A:删除lwjgl2依赖

### Q:提示某些类中的getter/setter不存在

A:启用lombok注解，IDEA会自动提示你

### Q:怎么构建/怎么用启动器启动

A:b站搜索全民写端，工件导出时依赖库只需添加lib/included目录下的jar