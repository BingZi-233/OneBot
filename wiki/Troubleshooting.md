# 故障排除指南

本指南帮助你诊断和解决OneBot插件的常见问题。

## 🔍 诊断步骤

### 1. 基础检查
在报告问题之前，请先进行基础检查：

```bash
# 检查插件状态
/onebot status

# 检查连接状态  
/onebot help

# 查看服务器日志
tail -f logs/latest.log
```

### 2. 启用调试模式
编辑 `plugins/OneBot/config.yml`：

```yaml
debug:
  enabled: true
  log_raw_messages: true
  log_actions: true
```

重新加载配置：
```bash
/onebot reload
/onebot restart
```

## ❌ 常见问题

### 问题1: 插件无法加载

#### 症状
- 服务器启动时看不到OneBot加载信息
- 执行`/onebot`命令提示命令不存在
- 插件列表中没有OneBot

#### 可能原因
1. **Java版本不兼容**
2. **服务器类型不支持**
3. **插件文件损坏**
4. **依赖冲突**

#### 解决方法

**检查Java版本**:
```bash
java -version
```
确保使用Java 8或更高版本。

**检查服务器兼容性**:
- 确保使用Bukkit/Spigot/Paper服务器
- 检查服务器版本是否支持

**重新下载插件**:
- 从官方渠道重新下载插件jar文件
- 检查文件完整性

**检查冲突**:
```bash
# 查看服务器启动日志
grep -i "onebot\|error\|exception" logs/latest.log
```

---

### 问题2: 连接失败

#### 症状
- `/onebot status` 显示"未连接"
- 控制台显示连接错误
- 无法发送测试消息

#### 可能原因
1. **Bot框架未启动**
2. **URL配置错误**
3. **端口被占用或防火墙阻拦**
4. **认证配置错误**

#### 解决方法

**检查Bot框架**:
```bash
# 检查go-cqhttp是否运行
ps aux | grep go-cqhttp

# 检查端口占用
netstat -tlnp | grep :5700
```

**验证URL配置**:
```yaml
# 常见正确配置
onebot:
  url: "ws://localhost:5700"  # go-cqhttp
  url: "ws://localhost:8080"  # mirai
  url: "ws://localhost:3001"  # LLOneBot
```

**测试网络连接**:
```bash
# 测试WebSocket连接
curl -i -N -H "Connection: Upgrade" \
     -H "Upgrade: websocket" \
     -H "Sec-WebSocket-Key: test" \
     -H "Sec-WebSocket-Version: 13" \
     http://localhost:5700/
```

**检查防火墙**:
```bash
# Linux
sudo ufw status
sudo iptables -L

# Windows  
netsh advfirewall show allprofiles
```

---

### 问题3: 消息发送失败

#### 症状
- `/onebot test` 命令返回失败
- API调用没有响应
- 群消息或私聊消息无法发送

#### 可能原因
1. **Bot没有发言权限**
2. **被风控限制**
3. **API调用频率过高**
4. **消息格式错误**

#### 解决方法

**检查Bot权限**:
- 确保Bot在目标群中有发言权限
- 检查Bot是否被禁言
- 确认Bot与目标用户是好友关系

**避免风控**:
- 降低消息发送频率
- 避免发送重复内容
- 使用更自然的消息格式

**检查消息格式**:
```kotlin
// 正确的消息格式
OneBotAPI.sendGroupMessage(123456L, "Hello World")

// 避免特殊字符导致的问题
val message = "测试消息".replace("[", "\\[").replace("]", "\\]")
```

---

### 问题4: 事件不触发

#### 症状
- 发送消息给Bot没有任何反应
- 事件监听器没有被调用
- 控制台没有事件日志

#### 可能原因
1. **事件监听器注册失败**
2. **事件被取消**
3. **插件未正确加载**
4. **Bot配置问题**

#### 解决方法

**检查事件注册**:
```kotlin
// 确保使用正确的注解
@SubscribeEvent
fun onMessage(event: GroupMessageEvent) {
    console().sendMessage("收到事件: ${event.message}")
}
```

**检查事件优先级**:
```kotlin
// 使用MONITOR优先级查看所有事件
@SubscribeEvent(priority = EventPriority.MONITOR)
fun debugEvent(event: OneBotEvent) {
    console().sendMessage("Debug: ${event::class.simpleName}")
}
```

**验证插件加载**:
```bash
/pl | grep OneBot
```

---

### 问题5: 配置不生效

#### 症状
- 修改配置后没有变化
- 预设配置无法应用
- 重载配置失败

#### 可能原因
1. **YAML格式错误**
2. **缓存问题**
3. **权限问题**
4. **配置路径错误**

#### 解决方法

**验证YAML格式**:
```bash
# 使用在线YAML验证器
# 或使用命令行工具
python -c "import yaml; yaml.safe_load(open('plugins/OneBot/config.yml'))"
```

**检查文件权限**:
```bash
ls -la plugins/OneBot/config.yml
chmod 644 plugins/OneBot/config.yml
```

**强制重载**:
```bash
/onebot reload
/stop
# 重启服务器
```

---

## 🛠️ 高级诊断

### 网络诊断
```bash
# 检查WebSocket连接
nc -zv localhost 5700

# 抓包分析
tcpdump -i lo port 5700

# 查看网络延迟
ping localhost
```

### 内存和性能
```bash
# 检查Java内存使用
jstat -gc [PID]

# 查看线程信息
jstack [PID] | grep -i onebot

# 监控系统资源
top -p [PID]
```

### 日志分析
```bash
# 过滤OneBot相关日志
grep -i onebot logs/latest.log

# 查看错误信息
grep -i "error\|exception\|failed" logs/latest.log | grep -i onebot

# 分析连接日志
grep -E "(connect|disconnect|websocket)" logs/latest.log
```

## 📊 性能监控

### 监控脚本示例
```bash
#!/bin/bash
# monitor-onebot.sh

echo "=== OneBot 状态监控 ==="
echo "时间: $(date)"

# 检查插件状态
echo "插件状态:"
screen -S minecraft -X eval 'stuff "onebot status\015"'

# 检查内存使用
echo "内存使用:"
ps -p $(pgrep -f minecraft) -o pid,ppid,cmd,%mem,%cpu

# 检查网络连接
echo "网络连接:"
netstat -an | grep :5700

echo "========================"
```

### 定期健康检查
```kotlin
// 添加到你的插件中
@ScheduleTask(period = 20 * 60) // 每分钟检查
fun healthCheck() {
    if (!OneBotAPI.isConnected()) {
        console().sendMessage("§c[Health] OneBot连接断开，尝试重连...")
        OneBotManager.instance.restart()
    }
}
```

## 🆘 获取帮助

### 收集诊断信息
在寻求帮助时，请提供以下信息：

1. **环境信息**:
   - 服务器类型和版本
   - Java版本
   - OneBot插件版本
   - 使用的Bot框架

2. **配置信息**:
   ```bash
   # 脱敏后的配置文件
   cat plugins/OneBot/config.yml | sed 's/token:.*/token: [HIDDEN]/'
   ```

3. **日志信息**:
   ```bash
   # 最近的错误日志
   grep -A5 -B5 -i "onebot.*error" logs/latest.log
   ```

4. **网络信息**:
   ```bash
   # 网络连接状态
   netstat -tlnp | grep -E ":(5700|8080|3001)"
   ```

### 提交Issue模板
```markdown
## 问题描述
[详细描述遇到的问题]

## 环境信息
- 服务器: Spigot 1.19.4
- Java: OpenJDK 17
- OneBot: v1.0.0
- Bot框架: go-cqhttp v1.0.0

## 配置信息
```yaml
onebot:
  url: "ws://localhost:5700"
  # ... 其他配置
```

## 日志信息
[相关日志信息]

## 复现步骤
1. ...
2. ...
3. ...

## 期望结果
[期望的正常行为]

## 实际结果  
[实际发生的异常行为]
```

### 联系方式
- 📧 **GitHub Issues**: [提交Issue](https://github.com/BingZi-233/OneBot/issues)
- 💬 **讨论区**: [GitHub Discussions](https://github.com/BingZi-233/OneBot/discussions)
- 📖 **文档**: [Wiki](https://github.com/BingZi-233/OneBot/wiki)

## 🔧 预防措施

### 定期维护
- 定期更新插件和Bot框架
- 监控日志文件大小
- 检查配置文件语法
- 测试备用连接方案

### 最佳实践
- 使用稳定版本的Bot框架
- 配置适当的重连参数
- 启用必要的日志记录
- 定期备份配置文件

### 监控告警
```bash
# 简单的监控脚本
#!/bin/bash
if ! curl -s http://localhost:5700/get_status > /dev/null; then
    echo "OneBot连接异常" | mail -s "Alert" admin@example.com
fi
```