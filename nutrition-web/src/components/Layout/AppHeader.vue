<template>
  <div class="app-header">
    <!-- 左侧Logo区域 -->
    <div class="header-left">
      <div class="logo">
        <el-icon class="logo-icon" :size="28">
          <Apple />
        </el-icon>
        <span class="logo-text">营养平台</span>
      </div>
    </div>

    <!-- 中间导航区域 -->
    <div class="header-center">
      <el-menu
        :default-active="activeIndex"
        class="header-menu"
        mode="horizontal"
        @select="handleSelect"
        :ellipsis="false"
      >
        <el-menu-item index="/">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/nutrition">
          <el-icon><DataAnalysis /></el-icon>
          <span>营养记录</span>
        </el-menu-item>
        <el-menu-item index="/assessment">
          <el-icon><DataAnalysis /></el-icon>
          <span>营养评估</span>
        </el-menu-item>
        <el-menu-item index="/food">
          <el-icon><Food /></el-icon>
          <span>食物库</span>
        </el-menu-item>
        <el-menu-item index="/analysis">
          <el-icon><TrendCharts /></el-icon>
          <span>数据分析</span>
        </el-menu-item>
        <el-menu-item index="/profile">
          <el-icon><Setting /></el-icon>
          <span>个人设置</span>
        </el-menu-item>
        <el-menu-item index="/user-management">
          <el-icon><UserFilled /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 右侧用户区域 -->
    <div class="header-right">
      <el-dropdown @command="handleCommand">
        <div class="user-info">
          <el-avatar
            :size="32"
            :src="userInfo.avatar || ''"
            class="user-avatar"
          >
            <el-icon><User /></el-icon>
          </el-avatar>
          <span class="user-name">{{ userInfo.nickname || '用户昵称' }}</span>
          <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              个人资料
            </el-dropdown-item>
            <el-dropdown-item command="settings">
              <el-icon><Setting /></el-icon>
              账户设置
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()

// 当前激活的菜单项
const activeIndex = computed(() => route.path)

// 用户信息
const userInfo = ref({
  nickname: '用户',
  avatar: '', // 空字符串会显示默认头像
  email: ''
})

// 初始化用户信息
const initUserInfo = () => {
  const storedUserInfo = localStorage.getItem('userInfo')
  if (storedUserInfo) {
    try {
      const parsed = JSON.parse(storedUserInfo)
      userInfo.value = {
        nickname: parsed.username || '用户',
        avatar: '',
        email: parsed.email || ''
      }
    } catch (error) {
      console.error('解析用户信息失败:', error)
    }
  }
}

// 组件挂载时初始化用户信息
onMounted(() => {
  initUserInfo()
})

// 处理菜单选择
const handleSelect = (key: string) => {
  router.push(key)
}

// 处理用户下拉菜单
const handleCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'settings':
      router.push('/settings')
      break
    case 'logout':
      handleLogout()
      break
  }
}

// 退出登录
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    // 清除本地存储的用户信息和token
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')

    ElMessage.success('退出登录成功')

    // 跳转到登录页
    router.push('/login')
  } catch (error) {
    // 用户取消退出
  }
}
</script>

<style scoped>
.app-header {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background-color: #fff;
}

/* 左侧Logo区域 */
.header-left {
  display: flex;
  align-items: center;
  min-width: 200px;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.logo:hover {
  opacity: 0.8;
}

.logo-icon {
  color: #67c23a;
  margin-right: 8px;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  background: linear-gradient(45deg, #67c23a, #409eff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 中间导航区域 */
.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
  max-width: 600px;
}

.header-menu {
  border-bottom: none !important;
  background-color: transparent !important;
}

.header-menu .el-menu-item {
  border-bottom: 2px solid transparent !important;
  color: #606266;
  font-weight: 500;
  transition: all 0.3s;
}

.header-menu .el-menu-item:hover {
  color: #409eff;
  background-color: rgba(64, 158, 255, 0.1) !important;
}

.header-menu .el-menu-item.is-active {
  color: #409eff !important;
  border-bottom-color: #409eff !important;
  background-color: rgba(64, 158, 255, 0.1) !important;
}

/* 右侧用户区域 */
.header-right {
  display: flex;
  align-items: center;
  min-width: 200px;
  justify-content: flex-end;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  transition: all 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.user-avatar {
  margin-right: 8px;
  border: 2px solid #e4e7ed;
}

.user-name {
  font-size: 14px;
  color: #303133;
  margin-right: 4px;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dropdown-icon {
  font-size: 12px;
  color: #909399;
  transition: transform 0.3s;
}

.user-info:hover .dropdown-icon {
  transform: rotate(180deg);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .app-header {
    padding: 0 10px;
  }

  .header-left {
    min-width: auto;
  }

  .logo-text {
    display: none;
  }

  .header-center {
    max-width: none;
  }

  .header-menu .el-menu-item span {
    display: none;
  }

  .header-right {
    min-width: auto;
  }

  .user-name {
    display: none;
  }
}
</style>
