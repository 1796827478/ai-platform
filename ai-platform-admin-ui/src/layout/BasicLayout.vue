<template>
  <el-container class="layout-container">
    <!-- 左侧侧边栏导航 -->
    <el-aside width="220px" class="aside">
      <div class="logo">
        <h2>🚀 统一 AI 平台</h2>
      </div>
      <el-menu
        active-text-color="#409EFF"
        background-color="#304156"
        class="el-menu-vertical"
        :default-active="activePath"
        text-color="#fff"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataBoard /></el-icon>
          <span>数据监控大屏</span>
        </el-menu-item>
        
        <el-sub-menu index="ai-base">
          <template #title>
            <el-icon><Cpu /></el-icon>
            <span>基础资源枢纽</span>
          </template>
          <el-menu-item index="/model-config">
            <el-icon><Key /></el-icon>
            <span>大模型网关配置</span>
          </el-menu-item>
          <el-menu-item index="/mcp-server">
            <el-icon><Connection /></el-icon>
            <span>MCP 挂载服务登记</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="ai-biz">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <span>业务权限管控</span>
          </template>
          <el-menu-item index="/app-info">
            <el-icon><UserFilled /></el-icon>
            <span>系统分发授权</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <!-- 右侧内容展示区 -->
    <el-container>
      <!-- 头部 Header -->
      <el-header class="header">
        <div class="header-breadcrumb">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">主页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ $route.meta.title || '当前页' }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-user">
          <el-avatar size="small" style="margin-right: 10px; background-color: #409EFF;">A</el-avatar>
          <span>超级管理员</span>
        </div>
      </el-header>

      <!-- 主要视图区 -->
      <el-main class="main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { DataBoard, Cpu, Key, Connection, Monitor, UserFilled } from '@element-plus/icons-vue'

const route = useRoute()
const activePath = computed(() => route.path)
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.aside {
  background-color: #304156;
  box-shadow: 2px 0 6px rgb(0 21 41 / 35%);
  z-index: 10;
  display: flex;
  flex-direction: column;
}
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  border-bottom: 1px solid #1f2d3d;
  font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
}
.el-menu-vertical {
  border-right: none;
  flex: 1;
}
.header {
  height: 60px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}
.header-user {
  display: flex;
  align-items: center;
  cursor: pointer;
}
.main {
  background-color: #f0f2f5;
  padding: 0;
}

/* 页面切换动画 */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all .3s;
}
.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}
.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
