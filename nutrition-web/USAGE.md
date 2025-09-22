# Axios 和 Element Plus 使用指南

## 概述

本项目已成功整合了 axios 和 element-plus，提供了完整的 HTTP 客户端和 UI 组件库支持。

## 已配置的功能

### 1. Element Plus UI 组件库
- ✅ 全局注册所有 Element Plus 组件
- ✅ 全局注册所有 Element Plus 图标
- ✅ 自动导入 Element Plus 样式

### 2. Axios HTTP 客户端
- ✅ 统一的请求/响应拦截器
- ✅ 自动错误处理和消息提示
- ✅ Token 自动添加
- ✅ TypeScript 类型支持

### 3. API 服务模块
- ✅ 用户相关 API (登录、注册、用户信息)
- ✅ 营养记录 API (CRUD 操作、统计)
- ✅ 食物信息 API (搜索、分类、推荐)
- ✅ 完整的 TypeScript 类型定义

## 使用示例

### Element Plus 组件使用

```vue
<template>
  <div>
    <!-- 按钮组件 -->
    <el-button type="primary" @click="handleClick">
      <el-icon><Plus /></el-icon>
      添加记录
    </el-button>

    <!-- 表单组件 -->
    <el-form :model="form" label-width="120px">
      <el-form-item label="食物名称">
        <el-input v-model="form.foodName" placeholder="请输入食物名称" />
      </el-form-item>
      <el-form-item label="卡路里">
        <el-input-number v-model="form.calories" :min="0" />
      </el-form-item>
    </el-form>

    <!-- 表格组件 -->
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="foodName" label="食物名称" />
      <el-table-column prop="calories" label="卡路里" />
    </el-table>

    <!-- 消息提示 -->
    <el-button @click="showMessage">显示消息</el-button>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const form = ref({
  foodName: '',
  calories: 0
})

const tableData = ref([
  { foodName: '苹果', calories: 52 },
  { foodName: '香蕉', calories: 89 }
])

const handleClick = () => {
  console.log('按钮被点击')
}

const showMessage = () => {
  ElMessage.success('这是一条成功消息')
}
</script>
```

### API 服务使用

```vue
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { 
  login, 
  getNutritionRecords, 
  createNutritionRecord,
  searchFoods 
} from '@/api'
import type { 
  LoginRequest, 
  NutritionRecordRequest,
  FoodSearchRequest 
} from '@/api'

const user = ref(null)
const nutritionRecords = ref([])
const foods = ref([])

// 用户登录
const handleLogin = async () => {
  try {
    const loginData: LoginRequest = {
      username: 'testuser',
      password: 'password123'
    }
    
    const result = await login(loginData)
    user.value = result.user
    localStorage.setItem('token', result.token)
    
    // 登录成功后的操作
    console.log('登录成功', result)
  } catch (error) {
    console.error('登录失败', error)
  }
}

// 获取营养记录
const fetchNutritionRecords = async () => {
  try {
    const result = await getNutritionRecords({
      page: 1,
      size: 10
    })
    nutritionRecords.value = result.records
  } catch (error) {
    console.error('获取营养记录失败', error)
  }
}

// 创建营养记录
const createRecord = async () => {
  try {
    const recordData: NutritionRecordRequest = {
      foodName: '苹果',
      calories: 52,
      protein: 0.3,
      carbohydrates: 14,
      fat: 0.2,
      recordDate: '2024-01-15'
    }
    
    const result = await createNutritionRecord(recordData)
    console.log('创建成功', result)
    
    // 重新获取列表
    await fetchNutritionRecords()
  } catch (error) {
    console.error('创建失败', error)
  }
}

// 搜索食物
const searchFood = async () => {
  try {
    const searchParams: FoodSearchRequest = {
      keyword: '苹果',
      page: 1,
      size: 10
    }
    
    const result = await searchFoods(searchParams)
    foods.value = result.records
  } catch (error) {
    console.error('搜索失败', error)
  }
}

onMounted(() => {
  // 组件挂载时获取数据
  fetchNutritionRecords()
})
</script>
```

### 直接使用 request 实例

```typescript
import { request } from '@/api'

// GET 请求
const getData = async () => {
  try {
    const data = await request({
      url: '/custom-endpoint',
      method: 'get',
      params: { id: 1 }
    })
    return data
  } catch (error) {
    console.error('请求失败', error)
  }
}

// POST 请求
const postData = async () => {
  try {
    const data = await request({
      url: '/custom-endpoint',
      method: 'post',
      data: {
        name: '测试数据',
        value: 123
      }
    })
    return data
  } catch (error) {
    console.error('请求失败', error)
  }
}
```

## 环境配置

### 开发环境 (.env.development)
```
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE=营养平台
VITE_APP_ENV=development
```

### 生产环境 (.env.production)
```
VITE_API_BASE_URL=https://your-api-domain.com/api
VITE_APP_TITLE=营养平台
VITE_APP_ENV=production
```

## 错误处理

axios 已配置了全局错误处理：

- **401 未授权**: 自动清除 token 并跳转到登录页
- **403 拒绝访问**: 显示权限不足提示
- **404 未找到**: 显示资源不存在提示
- **500 服务器错误**: 显示服务器错误提示
- **网络错误**: 显示网络连接错误提示

## 类型支持

项目提供了完整的 TypeScript 类型定义：

- `ApiResponse<T>`: 通用 API 响应类型
- `PageResponse<T>`: 分页响应类型
- `User`: 用户信息类型
- `NutritionRecord`: 营养记录类型
- `Food`: 食物信息类型
- 以及各种请求参数类型

## 注意事项

1. 所有 API 请求都会自动添加 Authorization 头（如果存在 token）
2. 响应数据会自动解析，只返回 data 部分
3. 错误会自动显示 Element Plus 消息提示
4. 请确保后端 API 返回格式符合 `ApiResponse` 类型定义

## 启动项目

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

访问 http://localhost:5173 查看测试页面。