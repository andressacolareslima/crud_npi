# Frontend em Vue + TypeScript

Aplicação Vue 3 para gerenciar categorias e livros do backend da biblioteca.

## Requisitos

- Node.js 18+
- Backend em execução em `http://localhost:8080`

## Como executar

```bash
npm install
npm run dev -- --host 0.0.0.0
```

A aplicação fica disponível em `http://localhost:5173`.

## Proxy da API

O Vite foi configurado para encaminhar requisições a `/api` para o backend em `http://localhost:8081`.

## Build

```bash
npm run build
```
