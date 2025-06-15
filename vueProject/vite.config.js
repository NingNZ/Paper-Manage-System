import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import utils from './scripts/utils'
import fs from 'fs'
import path from 'path'
// import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    // vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server:{
    host: '0.0.0.0', 
    https: {
      key: fs.readFileSync(path.resolve("D:/code/Paper-Manage-System/vueProject/cert", 'localhost-key.pem')),
      cert: fs.readFileSync(path.resolve("D:/code/Paper-Manage-System/vueProject/cert", 'localhost.pem')),
    },   
    proxy:{
      '/api': {
        target: utils.urlnoProxy, // 你的 Spring Boot 后端地址
        changeOrigin: true,
        secure: false, // 如果是自签名证书，需加这行
        rewrite: path => path.replace(/^\/api/, '')
      }
    }
  }
})
