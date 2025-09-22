<template>
  <div class="food-page">
    <div class="page-header">
      <h2>食物库</h2>
      <p>浏览和搜索食物营养信息</p>
    </div>
    
    <div class="content-placeholder">
      <el-empty description="食物库功能开发中...">
        <el-button type="primary">搜索食物</el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup lang="ts">
// 食物库页面逻辑
</script>

<style scoped>
.food-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h2 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 8px;
}

.page-header p {
  color: #606266;
  font-size: 14px;
}

.content-placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}
</style>