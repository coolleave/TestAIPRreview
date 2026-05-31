# AIPR Review Test Project

这是一个用于测试 AIPRreview 代码评审助手的最小全栈工程。

- `backend`: Spring Boot REST API
- `frontend`: Vue 3 + Vite 前端

## Backend

```powershell
cd backend
mvn spring-boot:run
```

默认端口：`8080`

## Frontend

```powershell
cd frontend
npm install
npm run dev
```

默认端口：`5173`

## Test API

```text
GET http://localhost:8080/api/reviews/health
GET http://localhost:8080/api/reviews/sample
```

## Risk Review Fixtures

`task` 分支包含一组故意写入的风险代码，用于测试代码评审助手。

- 高风险：命令执行、路径遍历、硬编码 token、硬编码管理员密码
- 中风险：开放重定向、弱随机验证码、敏感信息写入浏览器存储
- 低风险：过度宽松 CORS、控制台输出敏感信息、公开可变静态集合

这些代码只用于评审测试，不应部署到真实环境。
