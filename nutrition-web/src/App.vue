<script setup lang="ts">
// 主应用入口
</script>

<template>
  <div id="app">
    <router-view />
  </div>
</template>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}

#app {
  height: 100%;
}

/* Element Plus 样式覆盖 */
.el-header {
  padding: 0 !important;
}

.el-main {
  padding: 0 !important;
}

.el-footer {
  padding: 0 !important;
}
</style>
