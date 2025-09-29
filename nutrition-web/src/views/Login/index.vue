<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="logo">
          <el-icon class="logo-icon" :size="32"><Apple /></el-icon>
          <h1 class="logo-text">营养平台</h1>
        </div>
        <p class="login-subtitle">欢迎登录营养搭配服务平台</p>
      </div>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            :prefix-icon="User"
            clearable
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
            clearable
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-button"
            :loading="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <p class="demo-account">
          <el-icon><InfoFilled /></el-icon>
          演示账号：admin / 123456
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { User, Lock, Apple, InfoFilled } from '@element-plus/icons-vue'
import { loginUsingPost1 } from '@/nutrition-api/renzhengguanli'

const router = useRouter()

// 表单引用
const loginFormRef = ref<FormInstance>()

// 加载状态
const loading = ref(false)

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: ''
})

// 表单验证规则
const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    // 验证表单
    await loginFormRef.value.validate()

    loading.value = true

    // 调用登录API
    const response = await loginUsingPost1({
      username: loginForm.username,
      password: loginForm.password
    })

    if (response && response.data.code === 200 && response.data.data) {
      // 保存token
      localStorage.setItem('token', response.data.data.token || '')
      localStorage.setItem('userInfo', JSON.stringify({
        userId: response.data.data.userId,
        username: response.data.data.username,
        userRole: response.data.data.userRole,
        email: response.data.data.email,
        phone: response.data.data.phone
      }))
      ElMessage.success('登录成功')
      // 跳转到首页
      await router.push('/')
    } else {
      ElMessage.error(response?.data.message || '登录失败，请检查用户名和密码')
    }
  } catch (error: any) {
    console.error('登录失败:', error)
    ElMessage.error(error.message || '登录失败，请检查用户名和密码')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  padding: 40px;
  backdrop-filter: blur(10px);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
}

.logo-icon {
  color: #67c23a;
  margin-right: 8px;
}

.logo-text {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  background: linear-gradient(45deg, #67c23a, #409eff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0;
}

.login-subtitle {
  color: #606266;
  font-size: 14px;
  margin: 0;
}

.login-form {
  margin-bottom: 24px;
}

.login-form .el-form-item {
  margin-bottom: 24px;
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 500;
}

.login-footer {
  text-align: center;
}

.demo-account {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 13px;
  margin: 0;
  padding: 12px;
  background-color: #f5f7fa;
  border-radius: 6px;
}

.demo-account .el-icon {
  margin-right: 6px;
  color: #409eff;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-card {
    padding: 24px;
    margin: 0 16px;
  }

  .logo-text {
    font-size: 24px;
  }
}
</style>
