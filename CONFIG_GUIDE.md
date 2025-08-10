# OneBot 插件配置指南

## 基础配置

### 标准配置
```yaml
onebot:
  url: "ws://localhost:8080"
  token: "your-access-token"
  custom_path: ""
  extra_params: {}
  headers: {}
```

## 常见Bot配置示例

### 1. go-cqhttp
```yaml
onebot:
  url: "ws://localhost:5700"
  token: ""  # go-cqhttp 通常不需要token
  custom_path: ""
```

### 2. Mirai + mirai-api-http
```yaml
onebot:
  url: "ws://localhost:8080" 
  token: "your-verify-key"
  bot_id: 123456789  # 你的机器人QQ号
  mirai: true
  custom_path: "/all"
```

### 3. NoneBot2
```yaml
onebot:
  url: "ws://localhost:8080"
  token: "your-access-token"
  custom_path: "/onebot/v11/ws"
  headers:
    Authorization: "Bearer your-access-token"
```

### 4. LLOneBot (基于QQNT)
```yaml
onebot:
  url: "ws://localhost:3001"
  token: ""
  custom_path: ""
```

### 5. OpenShamrock (基于Xposed)
```yaml
onebot:
  url: "ws://localhost:5800"
  token: ""
  custom_path: ""
```

### 6. Koishi
```yaml
onebot:
  url: "ws://localhost:5140"
  token: "your-token"
  custom_path: "/onebot"
```

## 高级配置

### 自定义参数
```yaml
onebot:
  url: "ws://localhost:8080"
  extra_params:
    version: "11"
    platform: "minecraft"
    client_type: "taboolib"
```

### 自定义请求头
```yaml
onebot:
  url: "ws://localhost:8080"
  headers:
    User-Agent: "OneBot-Minecraft/1.0"
    X-Client-Role: "Universal"
    Authorization: "Bearer your-token"
```

### 安全配置
```yaml
onebot:
  url: "wss://your-secure-server.com:443"  # 使用WSS加密连接
  token: "your-secure-token"
  headers:
    X-API-Key: "your-api-key"
    Origin: "https://trusted-domain.com"
```

## 使用预设配置

插件提供了预设配置功能，可以快速应用常见Bot的配置：

### 查看可用预设
```
/onebot preset list
```

### 应用预设配置
```
/onebot preset apply go-cqhttp
/onebot preset apply mirai
/onebot preset apply nonebot2
```

### 自定义预设
在 `config.yml` 中添加你的预设：

```yaml
presets:
  my-custom:
    url: "ws://my-server:8080"
    path: "/custom/path"
    params:
      custom_param: "value"
    headers:
      Custom-Header: "value"
```

## 调试配置

```yaml
debug:
  enabled: true          # 启用调试模式
  log_raw_messages: true # 记录原始消息
  log_actions: true      # 记录API调用
```

## 连接设置

```yaml
onebot:
  reconnect_interval: 5        # 重连间隔（秒）
  max_reconnect_attempts: -1   # 最大重连次数（-1=无限）
  connect_timeout: 10000       # 连接超时（毫秒）
```

## 常见问题

### 1. 连接被拒绝
- 检查URL和端口是否正确
- 确认Bot服务是否已启动
- 检查防火墙设置

### 2. 认证失败
- 检查token是否正确
- 确认使用了正确的认证方式
- 某些Bot需要在headers中设置认证信息

### 3. 路径错误
- 不同的Bot可能需要不同的连接路径
- 使用预设配置或查看Bot文档

### 4. 消息收发异常
- 启用调试模式查看原始消息
- 检查Bot的OneBot协议版本兼容性
- 确认事件处理是否正确启用