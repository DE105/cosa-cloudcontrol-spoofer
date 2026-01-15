# CosaSpoof

## 项目简介

一个基于 YukiHookAPI 的 LSPosed 模块，用于 Hook ColorOS 的 `com.oplus.cosa`（应用增强服务），伪装 `ro.boot.prjname`，从而让应用增强服务按“目标机型”拉取对应的云控配置。

> 这里的“云控”可理解为游戏调度/策略配置（如性能策略、帧率策略等）。通常在同一 SoC 平台内，选择更合适的云控配置能让游戏体验更接近该平台的最佳状态。

## 需求背景

- ColorOS/realmeUI 的应用增强服务（`com.oplus.cosa`）会根据 `ro.boot.prjname` 拉取对应机型的云控配置
- 不同机型的云控（游戏调度）配置可能有差异（如风驰，FAS等）
- 在同一 SoC 平台下，希望切换到另一机型的云控配置，以获得更合适的调度策略与游戏体验

## 功能需求

### 核心功能

| 功能 | 描述 |
|------|------|
| 属性伪装 | Hook `SystemProperties.get()` 方法，当获取 `ro.boot.prjname` 时返回伪装值 |
| 作用域限制 | 仅对 `com.oplus.cosa`（应用增强服务）生效，不影响系统其他部分 |
| 可配置伪装值 | 模块设置页提供输入框，可自定义 `ro.boot.prjname` 伪装值（留空使用默认值） |

### 技术实现

- **框架**: YukiHookAPI 1.3.1+
- **平台**: LSPosed
- **语言**: Kotlin
- **最低 API**: 27 (Android 8.1)
- **目标 API**: 36

## Hook 点

```kotlin
// 目标类
android.os.SystemProperties

// 目标方法
get(String key) -> String
get(String key, String def) -> String

// 伪装逻辑
if (key == "ro.boot.prjname") {
    return FAKE_PRJNAME  // 返回伪装值
}
```

> 补充：部分版本的应用增强服务会把系统属性缓存到自身 `SharedPreferences`（例如 `prop_and_oplus_feature_sp.xml`），本模块同时会拦截 `SharedPreferences.getString/getAll` 对 `ro.boot.prjname` 的读取，确保缓存存在时也能生效。

## 配置项

| 配置 | 值 | 说明 |
|------|-----|------|
| `FAKE_PRJNAME` | `24831` | 目标机型的项目号 |

> 说明：默认伪装值为 `24831`，可在模块设置页输入框中修改并保存；修改后需重启应用增强服务生效。

## 使用方法

1. 编译安装 APK
2. 在 LSPosed 中激活模块
3. 勾选作用域：`com.oplus.cosa`
4. 重启应用增强服务或重启手机

## 开发构建说明

- 本项目已内置 Xposed API 编译桩：`app/libs/xposed-api-82.jar`，用于避免部分环境无法访问 Xposed Maven 源导致的构建失败
- 依赖 `drawabletoolbox` 来自 JitPack，`settings.gradle.kts` 已加入 `https://jitpack.io`

## 项目结构

```
CosaSpoof/
├── app/
│   └── src/main/java/com/spoof/cosa/
│       ├── hook/
│       │   └── HookEntry.kt    # Hook 入口
│       └── ui/
│           └── MainActivity.kt  # 模块激活状态界面
├── settings.gradle.kts
└── README.md
```

## 注意事项

- 仅影响应用增强服务，不影响系统 OTA 更新
- 修改 `FAKE_PRJNAME` 可切换不同机型的云控
- 调试时 `isDebug = true`，发布时改为 `false`

## 许可证

MIT License
