# 企业微信排单助手

基于 Xposed 框架的企业微信定时群发工具。

## 功能特性

- ✅ 定时群发消息（支持文字、图片、视频、表情、视频号）
- ✅ 排单队列管理（自动解析 `hwsd HH:MM` 指令）
- ✅ 消息间隔控制（默认 30 秒，可配置）
- ✅ 15位随机撤销码
- ✅ 多群发送方案
- ✅ 停止/恢复功能（停止期间的消息自动跳过）

## 安装要求

- Android 10+
- LSPosed 框架
- 企业微信 5.0.7+

## 快速开始

### 1. 下载 APK

从 [Releases](../../releases) 页面下载最新的 APK。

或者查看 [Actions](../../actions) 页面，下载自动编译的版本。

### 2. 安装

```bash
adb install app-debug.apk
```

### 3. 在 LSPosed 中激活

1. 打开 LSPosed Manager
2. 找到"企业微信排单助手"
3. 勾选"企业微信"
4. 重启企业微信

### 4. 使用

在企业微信排单群发送：

```
hwsd 18:00
```

然后发送要排单的消息即可。

## 指令列表

| 指令 | 说明 |
|------|------|
| `hwsd HH:MM` | 创建排单任务 |
| `停止` | 暂停排单 |
| `开始` | 恢复排单 |
| `状态` | 查看任务状态 |
| `撤销码` | 撤销指定消息 |
| `延迟N分钟` | 延迟任务 |
| `间隔N秒` | 修改消息间隔 |

详细使用说明请查看 [INSTALL.md](INSTALL.md)。

## 技术架构

- **框架**: LSPosed / Xposed
- **语言**: Kotlin
- **数据库**: Room (SQLite)
- **定时任务**: WorkManager
- **UI**: Jetpack Compose

## 开发

```bash
# 克隆仓库
git clone https://github.com/你的用户名/wework-scheduler.git

# 用 Android Studio 打开项目

# 编译
./gradlew assembleDebug
```

## 注意事项

1. 本工具仅供学习交流使用
2. 请勿用于违法违规用途
3. 数据本地存储，卸载会丢失

## License

MIT

---

**祝使用愉快！🦐**
