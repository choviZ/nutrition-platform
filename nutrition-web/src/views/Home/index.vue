<template>
  <div class="home-page">
    <!-- 顶部横幅区域 -->
    <div class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">智能营养管理平台</h1>
        <p class="hero-subtitle">科学分析您的饮食结构，定制专属营养方案，开启健康生活新旅程</p>
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="navigateTo('/assessment')">
            <el-icon><Promotion /></el-icon>
            开始营养评估
          </el-button>
          <el-button type="success" size="large" @click="navigateTo('/nutrition')">
            <el-icon><EditPen /></el-icon>
            记录今日饮食
          </el-button>
        </div>
      </div>
      <div class="hero-image">
        <div class="floating-elements">
          <div class="food-icon food-icon-1">🥗</div>
          <div class="food-icon food-icon-2">🍎</div>
          <div class="food-icon food-icon-3">🥛</div>
          <div class="food-icon food-icon-4">🍞</div>
          <div class="food-icon food-icon-5">🥩</div>
        </div>
      </div>
    </div>

    <!-- 今日营养建议区域 -->
    <div class="nutrition-advice-section">
      <h2 class="section-title">今日营养建议</h2>
      <el-row :gutter="30">
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <div class="advice-card water-advice">
            <div class="advice-header">
              <div class="advice-icon">
                <el-icon><MostlyCloudy /></el-icon>
              </div>
              <h3 class="advice-title">水分补充</h3>
            </div>
            <div class="advice-content">
              <p>建议每日饮水量为2000-2500毫升，分多次饮用，避免一次性大量饮水。</p>
              <div class="advice-tips">
                <div class="tip-item">
                  <el-icon><Clock /></el-icon>
                  <span>早晨起床后一杯温水，有助于激活身体机能</span>
                </div>
                <div class="tip-item">
                  <el-icon><Clock /></el-icon>
                  <span>餐前30分钟饮水，可增加饱腹感</span>
                </div>
                <div class="tip-item">
                  <el-icon><Clock /></el-icon>
                  <span>运动前后及时补充水分，维持体液平衡</span>
                </div>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <div class="advice-card vitamin-advice">
            <div class="advice-header">
              <div class="advice-icon">
                <el-icon><Sunny /></el-icon>
              </div>
              <h3 class="advice-title">维生素补充</h3>
            </div>
            <div class="advice-content">
              <p>均衡摄入各类维生素，增强免疫力，维持身体正常代谢。</p>
              <div class="vitamin-list">
                <div class="vitamin-item">
                  <div class="vitamin-name">维生素A</div>
                  <div class="vitamin-source">胡萝卜、菠菜、动物肝脏</div>
                </div>
                <div class="vitamin-item">
                  <div class="vitamin-name">维生素C</div>
                  <div class="vitamin-source">柑橘、草莓、西兰花</div>
                </div>
                <div class="vitamin-item">
                  <div class="vitamin-name">维生素D</div>
                  <div class="vitamin-source">鱼类、蛋黄、日光照射</div>
                </div>
                <div class="vitamin-item">
                  <div class="vitamin-name">维生素E</div>
                  <div class="vitamin-source">坚果、植物油、全谷物</div>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <!-- 健康资讯区域 -->
    <div class="health-tips-section">
      <h2 class="section-title">健康资讯</h2>
      <el-row :gutter="20">
        <el-col :xs="24" :md="16">
          <el-card class="tips-card" shadow="hover">
            <template #header>
              <div class="tips-header">
                <h3>今日健康小贴士</h3>
                <el-button type="text" @click="refreshTip">
                  <el-icon><Refresh /></el-icon>
                  换一个
                </el-button>
              </div>
            </template>
            <div class="tip-content">
              <div class="tip-icon">💡</div>
              <p>{{ currentTip.content }}</p>
              <div class="tip-category">{{ currentTip.category }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :md="8">
          <el-card class="quick-actions-card" shadow="hover">
            <template #header>
              <h3>快捷操作</h3>
            </template>
            <div class="quick-actions">
              <div class="quick-action" v-for="action in quickActions" :key="action.id" @click="navigateTo(action.route)">
                <div class="action-icon" :style="{ backgroundColor: action.color }">
                  <el-icon>
                    <component :is="action.icon" />
                  </el-icon>
                </div>
                <span>{{ action.title }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 导航方法
const navigateTo = (path: string) => {
  router.push(path)
}

// 健康小贴士
const healthTips = ref([
  {
    id: 1,
    content: '每天摄入五种不同颜色的蔬果，可以确保获得多种维生素和矿物质。',
    category: '饮食均衡'
  },
  {
    id: 2,
    content: '餐前喝一杯水可以增加饱腹感，有助于控制食量，避免过量进食。',
    category: '饮食习惯'
  },
  {
    id: 3,
    content: '早餐是一天中最重要的一餐，不要跳过，它能提供一天所需的能量。',
    category: '饮食时间'
  },
  {
    id: 4,
    content: '细嚼慢咽不仅有助于消化，还能让大脑有足够时间接收到饱腹信号。',
    category: '饮食方式'
  },
  {
    id: 5,
    content: '适量摄入优质蛋白质，如鱼、禽、豆类，有助于维持肌肉量和促进新陈代谢。',
    category: '营养知识'
  }
])

const currentTip = ref(healthTips.value[0])

// 刷新健康小贴士
const refreshTip = () => {
  const randomIndex = Math.floor(Math.random() * healthTips.value.length)
  currentTip.value = healthTips.value[randomIndex]
}

// 快捷操作
const quickActions = ref([
  {
    id: 1,
    title: '记录早餐',
    icon: 'Sunrise',
    color: '#ff9a76',
    route: '/nutrition'
  },
  {
    id: 2,
    title: '记录午餐',
    icon: 'Sunny',
    color: '#ffcc70',
    route: '/nutrition'
  },
  {
    id: 3,
    title: '记录晚餐',
    icon: 'Moon',
    color: '#7d5fff',
    route: '/nutrition'
  },
  {
    id: 4,
    title: '查看报告',
    icon: 'DataBoard',
    color: '#00d2d3',
    route: '/nutrition'
  }
])

// 组件挂载时随机选择一个健康小贴士
onMounted(() => {
  refreshTip()
})
</script>

<style scoped>.home-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 0;
  overflow-x: hidden;
}

/* 英雄区域样式 */
.hero-section {
  position: relative;
  padding: 80px 20px;
  text-align: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  overflow: hidden;
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 800px;
  margin: 0 auto;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 700;
  margin-bottom: 20px;
  letter-spacing: -1px;
  animation: fadeInUp 0.8s ease-out;
}

.hero-subtitle {
  font-size: 1.5rem;
  margin-bottom: 40px;
  opacity: 0.9;
  animation: fadeInUp 0.8s ease-out 0.2s both;
}

.hero-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  flex-wrap: wrap;
  animation: fadeInUp 0.8s ease-out 0.4s both;
}

/* 浮动食物图标 */
.hero-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
}

.floating-elements {
  position: relative;
  width: 100%;
  height: 100%;
}

.food-icon {
  position: absolute;
  font-size: 3rem;
  opacity: 0.3;
  animation: float 6s ease-in-out infinite;
}

.food-icon-1 {
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.food-icon-2 {
  top: 30%;
  right: 15%;
  animation-delay: 1s;
}

.food-icon-3 {
  bottom: 30%;
  left: 20%;
  animation-delay: 2s;
}

.food-icon-4 {
  bottom: 20%;
  right: 10%;
  animation-delay: 3s;
}

.food-icon-5 {
  top: 50%;
  left: 50%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(10deg);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 今日营养建议区域样式 */
.nutrition-advice-section {
  padding: 60px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.section-title {
  text-align: center;
  font-size: 2.2rem;
  font-weight: 600;
  margin-bottom: 50px;
  color: #333;
}

.advice-card {
  background: white;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  height: 100%;
  position: relative;
  overflow: hidden;
}

.advice-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
}

.advice-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 5px;
}

.water-advice::before {
  background: linear-gradient(90deg, #4fc3f7, #29b6f6);
}

.vitamin-advice::before {
  background: linear-gradient(90deg, #ffb74d, #ff9800);
}

.advice-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.advice-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 1.8rem;
  color: white;
}

.water-advice .advice-icon {
  background: linear-gradient(135deg, #4fc3f7, #29b6f6);
}

.vitamin-advice .advice-icon {
  background: linear-gradient(135deg, #ffb74d, #ff9800);
}

.advice-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.advice-content p {
  font-size: 1.1rem;
  line-height: 1.7;
  color: #555;
  margin-bottom: 20px;
}

.advice-tips {
  margin-top: 20px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 15px;
  color: #666;
}

.tip-item .el-icon {
  margin-right: 10px;
  color: #29b6f6;
  margin-top: 3px;
}

.vitamin-list {
  margin-top: 20px;
}

.vitamin-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 15px;
  background: rgba(255, 152, 0, 0.05);
  border-radius: 8px;
  margin-bottom: 10px;
  transition: all 0.3s ease;
}

.vitamin-item:hover {
  background: rgba(255, 152, 0, 0.1);
  transform: translateX(5px);
}

.vitamin-name {
  font-weight: 600;
  color: #ff9800;
}

.vitamin-source {
  color: #666;
}



/* 健康资讯区域样式 */
.health-tips-section {
  padding: 60px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.tips-card {
  height: 100%;
}

.tips-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tips-header h3 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.tip-content {
  position: relative;
  padding-left: 50px;
}

.tip-icon {
  position: absolute;
  left: 0;
  top: 0;
  font-size: 2rem;
}

.tip-content p {
  font-size: 1.1rem;
  line-height: 1.7;
  color: #555;
  margin-bottom: 15px;
}

.tip-category {
  display: inline-block;
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
  padding: 5px 15px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
}

.quick-actions-card {
  height: 100%;
}

.quick-actions-card h3 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 25px;
  color: #333;
}

.quick-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.quick-action {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  border-radius: 10px;
  background: rgba(0, 0, 0, 0.02);
  transition: all 0.3s ease;
  cursor: pointer;
}

.quick-action:hover {
  background: rgba(0, 0, 0, 0.05);
  transform: translateX(5px);
}

.action-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.2rem;
}

.quick-action span {
  font-size: 0.9rem;
  font-weight: 500;
  color: #333;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-title {
    font-size: 2.5rem;
  }

  .hero-subtitle {
    font-size: 1.2rem;
  }

  .hero-actions {
    flex-direction: column;
    align-items: center;
  }

  .food-icon {
    font-size: 2rem;
  }

  .quick-actions {
    grid-template-columns: 1fr;
  }

  .section-title {
    font-size: 1.8rem;
  }
}
</style>
