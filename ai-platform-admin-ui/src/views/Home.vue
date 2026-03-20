<template>
  <div class="home-container">
    <h1>欢迎来到 AI 能力平台统一管理后台</h1>
    <p>这是一个企业级大模型与 MCP 工具调度系统。在这里可以可视化的配置与查看所有资源的监控报表。</p>
    
    <el-row :gutter="20" style="margin-top: 30px;">
      <el-col :span="8">
        <el-card shadow="hover">
          <h3>注册的应用数</h3>
          <p style="font-size:24px; color:#409EFF">0</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <h3>对接的大模型数</h3>
          <p style="font-size:24px; color:#67C23A">0</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <h3>挂载的 MCP 服务</h3>
          <p style="font-size:24px; color:#E6A23C">0</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
// 仪表盘占位
</script>

<style scoped>
.home-container {
  padding: 20px;
}
</style>
