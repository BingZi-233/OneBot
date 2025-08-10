# 快速配置指南

在5分钟内完成OneBot插件的配置和使用。

## ⚡ 5分钟快速上手

### 步骤1: 安装插件 (1分钟)
1. 下载 `OneBot-x.x.x.jar` 放入 `plugins/` 目录
2. 重启服务器
3. 确认插件加载成功

### 步骤2: 选择Bot框架 (2分钟)
推荐使用 **go-cqhttp**（最简单）:

1. 下载 [go-cqhttp](https://github.com/Mrs4s/go-cqhttp/releases)
2. 运行一次生成配置文件
3. 修改 `config.yml`:
   ```yaml
   account:
     uin: 你的QQ号
     password: '你的QQ密码'
   
   servers:
     - ws-reverse:
         universal: ws://127.0.0.1:5700/
   ```
4. 重新运行go-cqhttp

### 步骤3: 配置插件连接 (1分钟)
```bash
# 应用go-cqhttp预设
/onebot preset apply go-cqhttp

# 启动连接
/onebot connect
```

### 步骤4: 测试功能 (1分钟)
```bash
# 检查状态
/onebot status

# 发送测试消息
/onebot test private 你的QQ号 Hello
/onebot test group 测试群号 Hello Group
```

✅ 完成！现在你的机器人已经可以工作了。

## 🎯 常用配置场景

### 场景1: go-cqhttp (推荐新手)
```bash
/onebot preset apply go-cqhttp
/onebot restart
```

**特点**: 配置简单，功能完整，稳定性好

### 场景2: Mirai
```bash
/onebot preset apply mirai  
/onebot restart
```

**特点**: Java生态，插件丰富，适合开发者

### 场景3: LLOneBot (QQNT)
```bash
/onebot preset apply llonebot
/onebot restart  
```

**特点**: 基于新版QQ，免密码登录

### 场景4: 自定义配置
编辑 `plugins/OneBot/config.yml`:
```yaml
onebot:
  url: "ws://your-bot-server:port"
  token: "your-token"
  custom_path: "/your/path"
  headers:
    Authorization: "Bearer your-token"
```

## 🔧 配置选项说明

### 基础配置
| 配置项 | 说明 | 示例 |
|--------|------|------|
| `url` | Bot服务器地址 | `ws://localhost:5700` |
| `token` | 认证令牌 | `your-access-token` |
| `custom_path` | 自定义路径 | `/onebot/v11/ws` |

### 连接配置  
| 配置项 | 说明 | 默认值 |
|--------|------|-------|
| `reconnect_interval` | 重连间隔(秒) | `5` |
| `max_reconnect_attempts` | 最大重连次数 | `-1` (无限) |
| `connect_timeout` | 连接超时(毫秒) | `10000` |

### 调试配置
| 配置项 | 说明 | 默认值 |
|--------|------|-------|
| `debug.enabled` | 启用调试 | `false` |
| `debug.log_raw_messages` | 记录原始消息 | `false` |
| `debug.log_actions` | 记录API调用 | `false` |

## 📱 Bot框架配置

### go-cqhttp 配置
```yaml
# go-cqhttp config.yml
account:
  uin: 123456789
  password: 'your-password'
  encrypt: false
  status: 0
  relogin:
    delay: 3
    interval: 3
    max-times: 0

heartbeat:
  interval: 5

message:
  post-format: string
  ignore-invalid-cqcode: false
  force-fragment: false
  fix-url: false
  proxy-rewrite: ""
  report-self-message: false
  remove-reply-at: false
  extra-reply-data: false
  skip-mime-scan: false

output:
  log-level: warn
  log-aging: 15
  log-force-new: true
  log-colorful: true
  debug: false

default-middlewares: &default
  access-token: ""
  filter: ""
  rate-limit:
    enabled: false
    frequency: 1
    bucket: 1

database:
  leveldb:
    enable: true

servers:
  - ws-reverse:
      universal: ws://127.0.0.1:5700/
      reconnect-interval: 3000
      middlewares:
        <<: *default
```

### Mirai + mirai-api-http
```yaml
# mirai-api-http setting.yml
adapters:
  - ws
debug: false
enableVerify: true
verifyKey: your-verify-key
singleMode: false
cacheSize: 4096
adapterSettings:
  ws:
    host: localhost
    port: 8080
    reservedSyncId: -1
```

### NoneBot2 配置
```python
# .env.prod
ENVIRONMENT=prod
HOST=127.0.0.1
PORT=8080
ONEBOT_WS_URLS=["ws://127.0.0.1:5700"]
```

## ⚙️ 高级配置示例

### 多Bot配置
```yaml
# 支持多个Bot实例
onebot:
  url: "ws://localhost:5700"
  # 可以通过不同端口连接多个Bot
  
# 或者使用负载均衡
onebot:
  url: "ws://load-balancer:8080"
  extra_params:
    instance_id: "server1"
```

### 安全配置
```yaml
onebot:
  url: "wss://secure-bot.example.com:443"
  token: "strong-random-token"
  headers:
    X-API-Key: "api-key"
    Origin: "https://trusted-domain.com"
    User-Agent: "Minecraft-Server/1.0"
```

### 集群配置
```yaml
onebot:
  url: "ws://bot-cluster.internal:5700"
  extra_params:
    cluster_node: "node1"
    server_id: "minecraft-1"
  headers:
    X-Cluster-Auth: "cluster-secret"
```

## 🚀 性能优化配置

### 高并发配置
```yaml
events:
  enabled: true
  thread_pool_size: 8  # 增加线程池大小

onebot:
  reconnect_interval: 3  # 减少重连间隔
  connect_timeout: 5000  # 减少连接超时
```

### 低延迟配置
```yaml
debug:
  log_raw_messages: false  # 关闭不必要的日志
  log_actions: false

onebot:
  extra_params:
    heartbeat_interval: 30000  # 增加心跳间隔
```

## 📋 配置验证

### 验证配置语法
```bash
# 检查YAML语法
python -c "import yaml; print('配置文件语法正确' if yaml.safe_load(open('plugins/OneBot/config.yml')) else '语法错误')"
```

### 测试连接
```bash
# 测试WebSocket连接
curl -i -N \
  -H "Connection: Upgrade" \
  -H "Upgrade: websocket" \
  -H "Sec-WebSocket-Key: test" \
  -H "Sec-WebSocket-Version: 13" \
  http://localhost:5700/
```

### 验证Bot权限
1. 确保Bot在目标群中
2. 检查Bot是否被禁言
3. 测试发送消息权限

## 🔄 配置迁移

### 从旧版本升级
1. 备份当前配置
2. 查看更新日志中的配置变更
3. 使用新版本重新生成配置
4. 迁移自定义设置

### 跨服务器迁移
```bash
# 复制配置文件
scp plugins/OneBot/config.yml user@new-server:/path/to/plugins/OneBot/

# 或者使用预设快速配置
/onebot preset apply go-cqhttp
```

## 🆘 常见配置错误

### 错误1: YAML格式错误
```yaml
# 错误
onebot:
url: "ws://localhost:5700"  # 缺少缩进

# 正确
onebot:
  url: "ws://localhost:5700"
```

### 错误2: 端口冲突
```yaml
# 检查端口是否被占用
netstat -tlnp | grep :5700

# 使用不同端口
onebot:
  url: "ws://localhost:5701"
```

### 错误3: 认证配置错误
```yaml
# 确保token格式正确
onebot:
  token: "abc123"  # 使用引号包围

# Mirai需要特殊配置
onebot:
  token: "verify-key"
  mirai: true
  bot_id: 123456789
```

## 📚 下一步

配置完成后，建议阅读：
- [API参考文档](API-Reference.md) - 了解可用的API
- [事件处理指南](Events-Guide.md) - 学习事件监听
- [基础机器人示例](Examples-Basic-Bot.md) - 查看完整示例
- [故障排除指南](Troubleshooting.md) - 解决常见问题