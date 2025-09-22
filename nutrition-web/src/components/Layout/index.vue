<template>
  <el-container class="layout-container">
    <!-- 头部 -->
    <el-header class="layout-header">
      <AppHeader />
    </el-header>
    
    <!-- 主体内容 -->
    <el-main class="layout-main">
      <router-view />
    </el-main>
    
    <!-- 底部 -->
    <el-footer class="layout-footer">
      <AppFooter />
    </el-footer>
  </el-container>
</template>

<script setup lang="ts">
import AppHeader from './AppHeader.vue'
import AppFooter from './AppFooter.vue'
</script>

<style scoped>
.layout-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout-header {
  height: 60px !important;
  background-color: #fff;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.layout-main {
  flex: 1;
  background-color: #f5f7fa;
  overflow-y: auto;
}

.layout-footer {
  height: 50px !important;
  background-color: #fff;
  border-top: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>