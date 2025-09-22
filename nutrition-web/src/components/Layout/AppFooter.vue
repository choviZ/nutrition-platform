<template>
  <div class="app-footer">
    <div class="footer-content">
      <div class="copyright">
        <span>© 2024 营养平台. All rights reserved.</span>
      </div>
      <div class="footer-links">
        <a href="#" class="footer-link">关于我们</a>
        <span class="divider">|</span>
        <a href="#" class="footer-link">隐私政策</a>
        <span class="divider">|</span>
        <a href="#" class="footer-link">服务条款</a>
        <span class="divider">|</span>
        <a href="#" class="footer-link">联系我们</a>
      </div>
      <div class="footer-info">
        <span>健康生活，从营养管理开始</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// Footer组件逻辑
</script>

<style scoped>
.app-footer {
  height: 100%;
  background-color: #fff;
  border-top: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: center;
}

.footer-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  max-width: 1200px;
  padding: 0 20px;
  font-size: 12px;
  color: #909399;
}

.copyright {
  display: flex;
  align-items: center;
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 8px;
}

.footer-link {
  color: #909399;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-link:hover {
  color: #409eff;
}

.divider {
  color: #dcdfe6;
}

.footer-info {
  font-style: italic;
  color: #c0c4cc;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .footer-content {
    flex-direction: column;
    gap: 8px;
    text-align: center;
    padding: 8px 10px;
  }
  
  .footer-links {
    order: -1;
  }
  
  .footer-info {
    display: none;
  }
}

@media (max-width: 480px) {
  .footer-links {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .divider {
    display: none;
  }
  
  .footer-link {
    padding: 2px 8px;
  }
}
</style>