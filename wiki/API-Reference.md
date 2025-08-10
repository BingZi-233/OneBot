# API 参考文档

OneBot插件提供了丰富的API接口，方便其他插件集成使用。

## API 概览

### 静态API接口
所有API都通过 `OneBotAPI` 对象提供，无需实例化即可使用。

```kotlin
import online.bingzi.onebot.api.OneBotAPI
```

## 🔌 连接管理

### isConnected()
检查WebSocket连接状态

```kotlin
val connected = OneBotAPI.isConnected()
if (connected) {
    // 执行需要连接的操作
}
```

**返回值**: `Boolean` - 是否已连接

---

## 💬 消息API

### sendPrivateMessage()
发送私聊消息

```kotlin
// 基础用法
OneBotAPI.sendPrivateMessage(userId, message)

// 完整参数
OneBotAPI.sendPrivateMessage(
    userId = 123456789L,
    message = "Hello!",
    autoEscape = false  // 是否转义CQ码
)
```

**参数**:
- `userId: Long` - 目标用户QQ号
- `message: String` - 消息内容
- `autoEscape: Boolean` - 是否作为纯文本发送（默认false）

**返回值**: `Boolean` - 是否发送成功

### sendGroupMessage()
发送群消息

```kotlin
// 基础用法
OneBotAPI.sendGroupMessage(groupId, message)

// 带CQ码的消息
OneBotAPI.sendGroupMessage(groupId, "[CQ:at,qq=123456] 你好！")

// 纯文本消息（转义CQ码）
OneBotAPI.sendGroupMessage(groupId, "这是[纯文本]", autoEscape = true)
```

**参数**:
- `groupId: Long` - 目标群号
- `message: String` - 消息内容
- `autoEscape: Boolean` - 是否作为纯文本发送（默认false）

**返回值**: `Boolean` - 是否发送成功

### deleteMessage()
撤回消息

```kotlin
val success = OneBotAPI.deleteMessage(messageId)
```

**参数**:
- `messageId: Int` - 消息ID

**返回值**: `Boolean` - 是否撤回成功

---

## 👥 群管理API

### banGroupMember()
禁言群成员

```kotlin
// 禁言10分钟
OneBotAPI.banGroupMember(groupId, userId, 600)

// 解除禁言
OneBotAPI.banGroupMember(groupId, userId, 0)

// 永久禁言（某些Bot支持）
OneBotAPI.banGroupMember(groupId, userId, -1)
```

**参数**:
- `groupId: Long` - 群号
- `userId: Long` - 用户QQ号
- `duration: Int` - 禁言时长（秒），0为解除禁言

**返回值**: `Boolean` - 是否操作成功

### kickGroupMember()
踢出群成员

```kotlin
// 踢出成员
OneBotAPI.kickGroupMember(groupId, userId)

// 踢出并拒绝加群请求
OneBotAPI.kickGroupMember(groupId, userId, rejectAddRequest = true)
```

**参数**:
- `groupId: Long` - 群号  
- `userId: Long` - 用户QQ号
- `rejectAddRequest: Boolean` - 是否拒绝此人的加群请求（默认false）

**返回值**: `Boolean` - 是否操作成功

---

## 📋 信息获取API

### getFriendList()
获取好友列表

```kotlin
val friendListJson = OneBotAPI.getFriendList()
if (friendListJson != null) {
    // 处理JSON数据
    val friendList = parseJsonToFriendList(friendListJson)
}
```

**返回值**: `String?` - 好友列表的JSON字符串，失败返回null

**JSON格式**:
```json
{
  "status": "ok",
  "retcode": 0,
  "data": [
    {
      "user_id": 123456,
      "nickname": "好友昵称",
      "remark": "备注名称"
    }
  ]
}
```

### getGroupList()
获取群列表

```kotlin
val groupListJson = OneBotAPI.getGroupList()
if (groupListJson != null) {
    // 处理JSON数据
    val groupList = parseJsonToGroupList(groupListJson)
}
```

**返回值**: `String?` - 群列表的JSON字符串，失败返回null

**JSON格式**:
```json
{
  "status": "ok", 
  "retcode": 0,
  "data": [
    {
      "group_id": 123456,
      "group_name": "群名称",
      "member_count": 100,
      "max_member_count": 500
    }
  ]
}
```

### getGroupMemberList()
获取群成员列表

```kotlin
val memberListJson = OneBotAPI.getGroupMemberList(groupId)
if (memberListJson != null) {
    // 处理JSON数据
    val memberList = parseJsonToMemberList(memberListJson)
}
```

**参数**:
- `groupId: Long` - 群号

**返回值**: `String?` - 群成员列表的JSON字符串，失败返回null

**JSON格式**:
```json
{
  "status": "ok",
  "retcode": 0, 
  "data": [
    {
      "group_id": 123456,
      "user_id": 789012,
      "nickname": "群昵称",
      "card": "群名片", 
      "sex": "male",
      "age": 20,
      "area": "地区",
      "join_time": 1234567890,
      "last_sent_time": 1234567890,
      "level": "活跃等级",
      "role": "member",
      "unfriendly": false,
      "title": "专属头衔",
      "title_expire_time": 1234567890,
      "card_changeable": true,
      "shut_up_timestamp": 0
    }
  ]
}
```

---

## 🛠️ 高级API使用

### 异步处理
所有API调用都是同步的，如果需要异步处理，可以使用TabooLib的任务系统：

```kotlin
import taboolib.common.platform.function.submit

submit(async = true) {
    val success = OneBotAPI.sendGroupMessage(groupId, "异步发送的消息")
    if (success) {
        console().sendMessage("消息发送成功")
    }
}
```

### 错误处理
API调用可能失败，建议添加适当的错误处理：

```kotlin
try {
    val success = OneBotAPI.sendPrivateMessage(userId, message)
    if (!success) {
        // 处理发送失败的情况
        logger.warning("消息发送失败: userId=$userId")
    }
} catch (e: Exception) {
    // 处理异常情况
    logger.error("API调用异常: ${e.message}")
}
```

### 批量操作
对于批量操作，建议添加延时避免频率限制：

```kotlin
import taboolib.common.platform.function.submit

val userIds = listOf(123456L, 789012L, 345678L)
userIds.forEachIndexed { index, userId ->
    submit(delay = index * 20L) { // 每个操作间隔1秒
        OneBotAPI.sendPrivateMessage(userId, "群发消息")
    }
}
```

## 📝 实用工具函数

### JSON解析示例
由于API返回的是JSON字符串，这里提供一些解析示例：

```kotlin
import com.google.gson.Gson
import com.google.gson.JsonObject

// 解析API响应
fun parseApiResponse(jsonString: String): ApiResponse? {
    return try {
        val json = JsonParser.parseString(jsonString).asJsonObject
        val status = json.get("status").asString
        val retcode = json.get("retcode").asInt
        val data = json.get("data")
        
        if (status == "ok" && retcode == 0) {
            ApiResponse(true, data)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

data class ApiResponse(
    val success: Boolean,
    val data: JsonElement?
)
```

### 消息构建工具
构建复杂消息的工具函数：

```kotlin
object MessageBuilder {
    fun buildAtMessage(userId: Long, text: String): String {
        return "[CQ:at,qq=$userId] $text"
    }
    
    fun buildImageMessage(imageFile: String): String {
        return "[CQ:image,file=$imageFile]"
    }
    
    fun buildReplyMessage(messageId: Int, text: String): String {
        return "[CQ:reply,id=$messageId]$text"
    }
}

// 使用示例
val message = MessageBuilder.buildAtMessage(123456L, "你好！")
OneBotAPI.sendGroupMessage(groupId, message)
```

## 🔒 权限和安全

### API权限检查
在调用管理类API前，建议检查权限：

```kotlin
fun safeBanMember(groupId: Long, userId: Long, duration: Int): Boolean {
    // 检查连接状态
    if (!OneBotAPI.isConnected()) {
        return false
    }
    
    // 检查是否为管理员操作
    if (!hasPermission("onebot.admin")) {
        return false
    }
    
    return OneBotAPI.banGroupMember(groupId, userId, duration)
}
```

### 频率限制
避免API调用过于频繁：

```kotlin
object RateLimiter {
    private val lastCallTime = mutableMapOf<String, Long>()
    
    fun canCall(key: String, cooldown: Long = 1000): Boolean {
        val now = System.currentTimeMillis()
        val last = lastCallTime[key] ?: 0
        
        return if (now - last > cooldown) {
            lastCallTime[key] = now
            true
        } else {
            false
        }
    }
}

// 使用示例
if (RateLimiter.canCall("sendMessage", 500)) {
    OneBotAPI.sendGroupMessage(groupId, message)
}
```

## 📚 更多示例

查看以下文档获取更多API使用示例：
- [基础机器人示例](Examples-Basic-Bot.md)
- [群管理机器人示例](Examples-Group-Manager.md) 
- [插件集成指南](Plugin-Integration.md)