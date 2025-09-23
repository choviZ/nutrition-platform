<template>
  <div class="user-management">
    <el-card class="page-container">
      <template #header>
        <div class="card-header">
          <h2>用户管理</h2>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加用户
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-section">
        <el-form :model="searchForm" :inline="true" class="search-form">
          <el-form-item label="用户名">
            <el-input
              v-model="searchForm.username"
              placeholder="请输入用户名"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="真实姓名">
            <el-input
              v-model="searchForm.realName"
              placeholder="请输入真实姓名"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input
              v-model="searchForm.email"
              placeholder="请输入邮箱"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="searchForm.gender" placeholder="请选择性别" clearable style="width: 120px">
              <el-option label="男" :value="1" />
              <el-option label="女" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 用户列表 -->
      <div class="table-section">
        <el-table
          v-loading="loading"
          :data="userList"
          style="width: 100%"
          stripe
          border
        >
          <el-table-column prop="userId" label="用户ID" width="80" />
          <el-table-column prop="username" label="用户名" width="120" />
          <el-table-column prop="realName" label="真实姓名" width="120" />
          <el-table-column prop="email" label="邮箱" width="200" />
          <el-table-column prop="phone" label="手机号" width="130" />
          <el-table-column prop="genderDesc" label="性别" width="80" />
          <el-table-column prop="age" label="年龄" width="80" />
          <el-table-column prop="height" label="身高(cm)" width="100" />
          <el-table-column prop="weight" label="体重(kg)" width="100" />
          <el-table-column prop="healthGoalDesc" label="健康目标" width="120" />
          <el-table-column prop="statusDesc" label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                {{ row.statusDesc }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleEdit(row)">
                编辑
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="handleDelete(row)"
                :disabled="row.userRole === 'admin'"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-section">
          <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>

    <!-- 添加/编辑用户对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userFormRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="userForm.username" placeholder="请输入用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="userForm.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="userForm.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="!isEdit">
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input
                v-model="userForm.password"
                type="password"
                placeholder="请输入密码"
                show-password
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="userForm.gender" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" :value="1" />
                <el-option label="女" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input-number
                v-model="userForm.age"
                :min="1"
                :max="120"
                placeholder="请输入年龄"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身高(cm)" prop="height">
              <el-input-number
                v-model="userForm.height"
                :min="50"
                :max="250"
                :precision="1"
                placeholder="请输入身高"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体重(kg)" prop="weight">
              <el-input-number
                v-model="userForm.weight"
                :min="20"
                :max="300"
                :precision="1"
                placeholder="请输入体重"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="健康目标" prop="healthGoal">
              <el-select v-model="userForm.healthGoal" placeholder="请选择健康目标" style="width: 100%">
                <el-option label="减重" :value="0" />
                <el-option label="减重" :value="1" />
                <el-option label="增重" :value="2" />
                <el-option label="维持体重" :value="3" />
                <el-option label="增肌" :value="4" />
                <el-option label="改善健康" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="isEdit">
            <el-form-item label="状态" prop="status">
              <el-select v-model="userForm.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="启用" :value="1" />
                <el-option label="禁用" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus, Search, Refresh } from '@element-plus/icons-vue'
import {
  listUserByPageUsingPost1,
  addUserUsingPost1,
  updateUserUsingPost1,
  deleteUserUsingPost1
} from '@/nutrition-api/userController'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const userList = ref<API.UserVO[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const userFormRef = ref<FormInstance>()

// 搜索表单
const searchForm = reactive({
  username: '',
  realName: '',
  email: '',
  gender: undefined as number | undefined,
  status: undefined as number | undefined
})

// 分页信息
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

// 用户表单
const userForm = reactive<API.UserAddRequest & API.UserUpdateRequest>({
  userId: undefined,
  username: '',
  realName: '',
  email: '',
  phone: '',
  password: '',
  gender: undefined,
  age: undefined,
  height: undefined,
  weight: undefined,
  healthGoal: undefined,
  status: 0
})

// 表单验证规则
const userFormRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  realName: [
    { min: 2, max: 10, message: '真实姓名长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  password: [
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ]
}

// 计算属性
const dialogTitle = computed(() => isEdit.value ? '编辑用户' : '添加用户')

// 辅助函数
const getHealthGoalDesc = (healthGoal: number | undefined) => {
  const goalMap: Record<number, string> = {
    0: '减重',
    1: '减重',
    2: '增重',
    3: '维持体重',
    4: '增肌',
    5: '改善健康'
  }
  return healthGoal !== undefined && healthGoal !== null ? goalMap[healthGoal] || '未知' : '未设置'
}

// 方法
const loadUserList = async () => {
  loading.value = true
  try {
    const params: API.UserQueryRequest = {
      current: pagination.current,
      pageSize: pagination.pageSize,
      ...searchForm
    }

    const response = await listUserByPageUsingPost1(params)
    if (response.code === 200 && response.data) {
      // 处理用户数据，添加描述性字段
      const processedUsers = (response.data.records || []).map(user => ({
        ...user,
        genderDesc: user.gender === 1 ? '男' : user.gender === 0 ? '女' : '未知',
        statusDesc: user.status === 1 ? '启用' : '禁用',
        healthGoalDesc: getHealthGoalDesc(user.healthGoal)
      }))
      userList.value = processedUsers
      // 确保 total 是数字类型
      pagination.total = typeof response.data.total === 'string' ? parseInt(response.data.total) : (response.data.total || 0)
    } else {
      ElMessage.error(response.message || '获取用户列表失败')
    }
  } catch (error) {
    ElMessage.error('获取用户列表失败')
    console.error('Load user list error:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  loadUserList()
}

const handleReset = () => {
  Object.assign(searchForm, {
    username: '',
    realName: '',
    email: '',
    gender: undefined,
    status: undefined
  })
  pagination.current = 1
  loadUserList()
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.current = 1
  loadUserList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  loadUserList()
}

const handleAdd = () => {
  isEdit.value = false
  dialogVisible.value = true
  resetUserForm()
}

const handleEdit = (row: API.UserVO) => {
  isEdit.value = true
  dialogVisible.value = true
  Object.assign(userForm, {
    userId: row.userId,
    username: row.username,
    realName: row.realName,
    email: row.email,
    phone: row.phone,
    gender: row.gender,
    age: row.age,
    height: row.height,
    weight: row.weight,
    healthGoal: row.healthGoal,
    status: row.status
  })
}

const handleDelete = async (row: API.UserVO) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${row.username}" 吗？此操作不可恢复。`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await deleteUserUsingPost1({ id: row.userId })
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadUserList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('Delete user error:', error)
    }
  }
}

const handleSubmit = async () => {
  if (!userFormRef.value) return

  try {
    await userFormRef.value.validate()
    submitLoading.value = true

    let response
    if (isEdit.value) {
      response = await updateUserUsingPost1(userForm as API.UserUpdateRequest)
    } else {
      response = await addUserUsingPost1(userForm as API.UserAddRequest)
    }

    if (response.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
      dialogVisible.value = false
      loadUserList()
    } else {
      ElMessage.error(response.message || (isEdit.value ? '更新失败' : '添加失败'))
    }
  } catch (error) {
    console.error('Submit user error:', error)
  } finally {
    submitLoading.value = false
  }
}

const handleDialogClose = () => {
  dialogVisible.value = false
  resetUserForm()
  userFormRef.value?.clearValidate()
}

const resetUserForm = () => {
  Object.assign(userForm, {
    userId: undefined,
    username: '',
    realName: '',
    email: '',
    phone: '',
    password: '',
    gender: undefined,
    age: undefined,
    height: undefined,
    weight: undefined,
    healthGoal: undefined,
    status: 0
  })
}

// 生命周期
onMounted(() => {
  loadUserList()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.page-container {
  min-height: calc(100vh - 120px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  color: #303133;
  font-size: 20px;
  font-weight: 600;
}

.search-section {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.search-form {
  margin-bottom: 0;
}

.table-section {
  margin-top: 20px;
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
}

:deep(.el-pagination) {
  justify-content: center;
}

:deep(.el-form-item) {
  margin-bottom: 18px;
}

:deep(.el-dialog__header) {
  padding: 20px 20px 10px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-dialog__body) {
  padding: 20px;
}

:deep(.el-dialog__footer) {
  padding: 10px 20px 20px;
  border-top: 1px solid #ebeef5;
}
</style>
