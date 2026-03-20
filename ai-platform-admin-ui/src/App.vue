<template>
  <el-config-provider>
    <router-view></router-view>
  </el-config-provider>
</template>

<script setup lang="ts">
// 根组件只做路由占位
</script>

<style>
/* 全局样式初始化 */
html, body, #app {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  background-color: #f5f7fa;
}
</style>
