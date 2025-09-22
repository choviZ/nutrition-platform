<template>
  <div class="home-page">
    <div class="welcome-section">
      <h1>欢迎使用营养平台</h1>
      <p>科学管理您的营养摄入，开启健康生活新方式</p>
    </div>
    
    <div class="feature-cards">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="6" v-for="feature in features" :key="feature.id">
          <el-card class="feature-card" shadow="hover">
            <div class="feature-icon">
              <el-icon :size="40">
                <component :is="feature.icon" />
              </el-icon>
            </div>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const features = ref([
  {
    id: 1,
    title: '营养记录',
    description: '记录每日饮食，追踪营养摄入',
    icon: 'DataAnalysis'
  },
  {
    id: 2,
    title: '食物库',
    description: '丰富的食物数据库，营养成分详细',
    icon: 'Food'
  },
  {
    id: 3,
    title: '数据分析',
    description: '可视化分析，了解营养趋势',
    icon: 'TrendCharts'
  },
  {
    id: 4,
    title: '个人设置',
    description: '个性化配置，定制专属方案',
    icon: 'Setting'
  }
])
</script>

<style scoped>
.home-page {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-section {
  text-align: center;
  margin-bottom: 60px;
}

.welcome-section h1 {
  font-size: 36px;
  color: #303133;
  margin-bottom: 16px;
  background: linear-gradient(45deg, #67c23a, #409eff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.welcome-section p {
  font-size: 18px;
  color: #606266;
  line-height: 1.6;
}

.feature-cards {
  margin-top: 40px;
}

.feature-card {
  text-align: center;
  padding: 20px;
  height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  margin-bottom: 16px;
  color: #409eff;
}

.feature-card h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 12px;
}

.feature-card p {
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
}

@media (max-width: 768px) {
  .welcome-section h1 {
    font-size: 28px;
  }
  
  .welcome-section p {
    font-size: 16px;
  }
  
  .home-page {
    padding: 20px 10px;
  }
}
</style>