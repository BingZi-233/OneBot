## TabooLib

- TabooLib的Github仓库是TabooLib/taboolib，可以在DeepWiki中查询到详细信息
- 我们应该尽可能利用TabooLib提供的功能来完成代码编写

## 代码组织

- 我习惯将内部才会用到的模块放到internal包下面，对外提供的（例如：事件）放到api包下面
- 我们每个功能都要有自己单独的包名

## 命名规范

- 语言文件我习惯使用小驼峰的方式命名
- 语言文件是一个扁平的yaml文件，不接受嵌套结构

## 国际化与消息处理

- 项目不对消息文本进行硬编码
- 可以尝试调用sendInfo/sendWarn/sendError来对console()、Player、CommandSender、ProxyPlayer和ProxyCommandSender发送国际化消息

## 代码风格

- 我们总是会编写详细的中文注释，以便后续阅读代码

## DeepWiki使用

- DeepWiki可以查询到几乎所有需要用到的知识，你需要积极的调用这个工具
- 不要进行任何假设，如果遇到不确定的先使用DeepWiki进行查阅，如果DeepWiki中无法查阅到需要进行询问