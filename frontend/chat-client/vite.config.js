import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/ws': {
        target: 'http://localhost:8080', // Spring Boot backend
        ws: true,                       // Enable WebSocket proxy
        changeOrigin: true
      },
      '/api': 'http://localhost:8080'   // optional, for REST API
    }
  }
});