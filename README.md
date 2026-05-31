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
