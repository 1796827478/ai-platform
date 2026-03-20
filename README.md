# 通用 AI 能力平台 (AI Platform)

本工程是一个企业级、支持多租户、多模型的统一 AI 能力底座。旨在将大语言模型 (LLM) 和各种外部工具 (MCP Servers) 进行集中式管理、鉴权和动态调度，然后以统一的 RESTful / SSE 接口赋能给公司内部的其他业务系统。

## 模块架构与功能说明

本系统采用标准的 Maven 多模块物理隔离架构，共包含 4 个核心微服务模块：

### 1. `ai-platform-common` (公共组件与基础设施层)
- **定位**：全站的基础依赖被控层，供其他所有模块复用，自身不独立启动。
- **功能**：
  - MyBatis-Plus 自动生成的 Entity（实体类）、Mapper（数据访问层）和基本的 Service 层接口。
  - 全局统一的异常处理机制 (`GlobalExceptionHandler`)与统一响应封装 (`Result<T>`)。
  - `IdType.ASSIGN_ID` 雪花算法的主键策略配置，确保对 MySQL、达梦等多种异构数据库的完美接驳。
  - 整合 Redis 缓存基础能力。
  - 整合各种 Hutool 等常用开发工具。

### 2. `ai-platform-core` (核心 AI 计算与路由引擎层)
- **定位**：封装着大模型动态调度算法的心脏模块。它依赖 Common，同时被 OpenAPI 数据面深深依赖。
- **功能**：
  - 核心依赖了 `spring-ai-starter-model-openai` 和 `spring-ai-starter-mcp-client`。
  - 负责根据外部业务子系统的 AppID 和 请求的 SessionId，**动态路由**至对应的 LLM 模型配置（无论是千问、OpenAI还是本地化智谱模型）。
  - 负责**动态挂载 MCP Server 工具池**。大模型引擎能随着用户提问，实时从数据库校验调用方的应用权限，并用被授权的 MCP 自动组装出包含 `SSE/Stdio` 回调的工具清单。

### 3. `ai-platform-admin` (后台管理控制面)
- **定位**：系统的“大脑控制室”，是一个独立的 Spring Boot 应用。主要面向运维团队与管理员使用。
- **端口**：`8080`
- **功能**：
  - 专门负责平台自身的“增删改查”类非密集型业务管理 API。
  - 例如：录入新上线的大模型账号秘钥 (`ai_model_config`)、新建接入方业务系统信息 (`ai_app_info`)、登记内部研发的各类 MCP Server 工具端点 (`ai_mcp_server`)。
  - 根据商业规划和业务需要，通过分配绑定权限界面（操作 `ai_app_resource_rel` 关系表），给下属业务子系统划定它可用哪些 AI 和 哪些工具。

### 4. `ai-platform-openapi` (对外业务调用数据面)
- **定位**：系统的高并发对外网关，是一个独立的、内嵌 Reactor 反应式的 Spring Boot Web 应用。
- **端口**：`8081`
- **功能**：
  - 系统唯一对外暴漏底层 AI 算力的出口。通过极其精简的 SSE (Server-Sent Events) REST API，为公司诸如 `DCCS-OMS` 或 `CRM` 等外部系统赋能流式 AI 生成打字机效果。
  - 提供强身份请求阻断校验。以极小开销在内存校验 AppKey 鉴权、频率限制与防刷风控。
  - 接管并保存每次用户的问答流水到 `ai_chat_session` 和 `ai_chat_message` 审计表，充当日志库为日后的预料分析或 Finetune (模型微调) 圈养数据池。

---

## 后续工作开展与排期计划

当前骨架建设与 Maven 依赖问题已彻底扫清跑通，数据库代码生成等底座工作也已完成落位。接下来的实质性研发任务分布在三大阶段：

### 🏆 阶段一：打造“后台大脑”（面向 `ai-platform-admin`）
1. **落地控制类 CRUD API**：封装刚生成的原生 Mybatis-Plus Service，给出一套标准的后端管理接口供后续做管理后台前端页面对接。
2. **梳理核心业务概念流程配置**：支持以 API 发送创建并存储诸如“App密钥”、“模型网关Token”与“MCP 端点URL”等底层字典核心信息。

### 🚀 阶段二：打磨最硬核的“动态 AI 引擎”（面向 `ai-platform-core`）
1. **大模型的“动态工厂模式”**：不再将 OpenAi 的秘钥硬编码写死在 `application.yml` 里，而是开发一个基于 `ConcurrentHashMap` 的 `ChatClientFactory`，从 DB 取出对应的 Token 与 Base URL 在堆内动态新建实例。
2. **MCP 工具池的“权限动态挂载”**：重装被废掉的懒加载模式，实现基于被调用系统身份进行过滤拦截的 `AsyncMcpToolCallbackProvider` 组装器代码，避免超权调用敏感应用接口。
3. **完善流式断联的兜底封装**：解决原生 Spring AI 在与 LLM 网络断线后的 500 硬错误，重写一层面向前端的高可用错误映射协议栈。

### 🌐 阶段三：全面开放网关并对接业务微服务（面向 `ai-platform-openapi`）
1. **开发 OpenAPI 对内调用端点**：开发并压测形如 `/api/v1/openapi/chat/stream` 的高性能接入点。
2. **安全切面挂载**：引入基于 HTTP Header 的签名比对及 JWT 防伪造 Interceptor 架构鉴权。
3. **集成联调实战演练**：挑选公司内部一个实际的（如基于 Vue 发起的）业务子系统客户端发起直连请求，验证多应用多模型并配有自动调取 MCP 并下发消息记录和工具行为轨迹是否顺畅可靠。
