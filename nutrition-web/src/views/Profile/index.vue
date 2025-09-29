<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <h2>个人信息管理</h2>
          <el-button type="primary" @click="handleSave" :loading="saveLoading">
            <el-icon>
              <Check />
            </el-icon>
            保存所有信息
          </el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab" class="profile-tabs">
        <!-- 基本信息标签页 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form
            ref="basicFormRef"
            :model="userForm"
            :rules="userFormRules"
            label-width="120px"
            class="profile-form"
          >
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="userForm.username" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="真实姓名" prop="realName">
                  <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
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

            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="性别" prop="gender">
                  <el-select v-model="userForm.gender" placeholder="请选择性别" style="width: 100%">
                    <el-option label="女" :value="0" />
                    <el-option label="男" :value="1" />
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

            <el-row :gutter="24">
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

            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="健康目标" prop="healthGoal">
                  <el-select v-model="userForm.healthGoal" placeholder="请选择健康目标"
                             style="width: 100%">
                    <el-option label="减重" :value="0" />
                    <el-option label="减重" :value="1" />
                    <el-option label="增重" :value="2" />
                    <el-option label="维持体重" :value="3" />
                    <el-option label="增肌" :value="4" />
                    <el-option label="改善健康" :value="5" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <!-- 健康档案标签页 -->
        <el-tab-pane label="健康档案" name="health">
          <el-form
            ref="healthFormRef"
            :model="healthForm"
            :rules="healthFormRules"
            label-width="120px"
            class="profile-form"
          >
            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="活动水平" prop="activityLevel">
                  <el-select v-model="healthForm.activityLevel" placeholder="请选择活动水平"
                             style="width: 100%">
                    <el-option label="久坐（很少运动）" :value="1" />
                    <el-option label="轻度活动（每周1-3次轻度运动）" :value="2" />
                    <el-option label="中度活动（每周3-5次中等强度运动）" :value="3" />
                    <el-option label="高度活动（每周6-7次高强度运动）" :value="4" />
                    <el-option label="极高活动（每天高强度运动或体力劳动）" :value="5" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="过敏史" prop="allergies">
                  <el-input
                    v-model="healthForm.allergies"
                    type="textarea"
                    :rows="3"
                    placeholder="请详细描述您的过敏史，如对某些食物、药物等的过敏情况"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="慢性疾病" prop="chronicDiseases">
                  <el-input
                    v-model="healthForm.chronicDiseases"
                    type="textarea"
                    :rows="3"
                    placeholder="请描述您的慢性疾病史，如糖尿病、高血压、心脏病等"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="饮食偏好" prop="dietaryPreferences">
                  <el-input
                    v-model="healthForm.dietaryPreferences"
                    type="textarea"
                    :rows="3"
                    placeholder="请描述您的饮食偏好，如素食、低盐、低糖等特殊饮食需求"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="运动习惯" prop="exerciseHabits">
                  <el-input
                    v-model="healthForm.exerciseHabits"
                    type="textarea"
                    :rows="3"
                    placeholder="请描述您的运动习惯，如跑步、游泳、健身等"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="24">
                <el-form-item label="禁忌食物" prop="forbiddenFoods">
                  <el-input
                    v-model="healthForm.forbiddenFoods"
                    type="textarea"
                    :rows="3"
                    placeholder="请列出您不能或不愿意食用的食物"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Check } from '@element-plus/icons-vue'
import {
  getUserByIdUsingGet1,
  updateUserUsingPost1
} from '@/nutrition-api/userController'
import {
  getMyHealthProfileUsingGet1,
  createHealthProfileUsingPost1,
  updateHealthProfileUsingPost1,
  hasHealthProfileUsingGet1
} from '@/nutrition-api/yonghujiankangdanganguanli'

// 响应式数据
const activeTab = ref('basic')
const saveLoading = ref(false)
const basicFormRef = ref<FormInstance>()
const healthFormRef = ref<FormInstance>()

// 用户基本信息表单
const userForm = reactive<API.UserUpdateRequest>({
  userId: undefined,
  username: '',
  realName: '',
  email: '',
  phone: '',
  gender: undefined,
  age: undefined,
  height: undefined,
  weight: undefined,
  healthGoal: undefined
})

// 健康档案表单
const healthForm = reactive<API.HealthProfileCreateRequest & { profileId?: number }>({
  profileId: undefined,
  activityLevel: undefined,
  allergies: '',
  chronicDiseases: '',
  dietaryPreferences: '',
  exerciseHabits: '',
  forbiddenFoods: ''
})

// 表单验证规则
const userFormRules: FormRules = {
  realName: [
    { min: 2, max: 10, message: '真实姓名长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ]
}

const healthFormRules: FormRules = {
  activityLevel: [
    { required: true, message: '请选择活动水平', trigger: 'change' }
  ]
}

// 当前用户id
const currentUserId = computed(() => {
  // 从localStorage或store中获取当前用户ID
  const userInfo = localStorage.getItem('userInfo')
  if (userInfo) {
    const user = JSON.parse(userInfo)
    return user.userId
  }
  return null
})

/**
 * 加载用户信息
 */
const loadUserInfo = async () => {
  if (!currentUserId.value) {
    ElMessage.error('请先登录')
    return
  }
  const response = await getUserByIdUsingGet1({ id: currentUserId.value })
  if (response.data.data && response.data.code === 200) {
    const userData = response.data.data
    userForm.userId = userData.userId
    userForm.username = userData.username
    userForm.realName = userData.realName
    userForm.email = userData.email
    userForm.phone = userData.phone
    userForm.gender = userData.gender
    userForm.age = userData.age
    userForm.height = userData.height
    userForm.weight = userData.weight
    userForm.healthGoal = userData.healthGoal
  } else {
    console.error('获取用户信息失败:', response.data.message)
    ElMessage.error(response.data.message || '获取用户信息失败')
  }
}

const loadHealthProfile = async () => {
  try {
    // 先检查是否已有健康档案
    const res = await hasHealthProfileUsingGet1()
    if (res.data.code === 200 && res.data.data) {
      // 获取健康档案
      const response = await getMyHealthProfileUsingGet1()
      if (response.data.code === 200 && response.data.data) {
        Object.assign(healthForm, {
          profileId: response.data.data.profileId,
          activityLevel: response.data.data.activityLevel,
          allergies: response.data.data.allergies || '',
          chronicDiseases: response.data.data.chronicDiseases || '',
          dietaryPreferences: response.data.data.dietaryPreferences || '',
          exerciseHabits: response.data.data.exerciseHabits || '',
          forbiddenFoods: response.data.data.forbiddenFoods || ''
        })
      }
    }
  } catch (error) {
    console.error('加载健康档案失败:', error)
    // 如果没有健康档案，不显示错误信息
  }
}

/**
 * 保存用户信息
 */
const saveUserInfo = async () => {
  if (!basicFormRef.value) return false

  try {
    await basicFormRef.value.validate()
    const response = await updateUserUsingPost1(userForm)
    if (response.data.code === 200) {
      ElMessage.success('基本信息保存成功')
      return true
    } else {
      ElMessage.error(response.data.message || '基本信息保存失败')
      return false
    }
  } catch (error) {
    console.error('保存基本信息失败:', error)
    ElMessage.error('基本信息验证失败')
    return false
  }
}

/**
 * 保存健康档案
 */
const saveHealthProfile = async () => {
  if (!healthFormRef.value) return false

  try {
    await healthFormRef.value.validate()

    let response
    if (healthForm.profileId) {
      // 更新健康档案
      response = await updateHealthProfileUsingPost1({
        profileId: healthForm.profileId,
        activityLevel: healthForm.activityLevel,
        allergies: healthForm.allergies,
        chronicDiseases: healthForm.chronicDiseases,
        dietaryPreferences: healthForm.dietaryPreferences,
        exerciseHabits: healthForm.exerciseHabits,
        forbiddenFoods: healthForm.forbiddenFoods
      })
    } else {
      // 创建健康档案
      response = await createHealthProfileUsingPost1({
        activityLevel: healthForm.activityLevel,
        allergies: healthForm.allergies,
        chronicDiseases: healthForm.chronicDiseases,
        dietaryPreferences: healthForm.dietaryPreferences,
        exerciseHabits: healthForm.exerciseHabits,
        forbiddenFoods: healthForm.forbiddenFoods
      })
      // 如果创建成功，更新profileId
      if (response.data.code === 200 && response.data) {
        healthForm.profileId = response.data.data?.profileId
      }
    }
    if (response.data.code === 200) {
      ElMessage.success('健康档案保存成功')
      return true
    } else {
      ElMessage.error(response.data.message || '健康档案保存失败')
      return false
    }
  } catch (error) {
    console.error('保存健康档案失败:', error)
    ElMessage.error('健康档案验证失败')
    return false
  }
}

/**
 * 保存 - 同时调用保存用户基础信息和保存健康档案
 */
const handleSave = async () => {
  saveLoading.value = true
  try {
    const userSaved = await saveUserInfo()
    const healthSaved = await saveHealthProfile()

    if (userSaved && healthSaved) {
      ElMessage.success('所有信息保存成功')
    } else if (userSaved) {
      ElMessage.warning('基本信息保存成功，健康档案保存失败')
    } else if (healthSaved) {
      ElMessage.warning('健康档案保存成功，基本信息保存失败')
    }
  } finally {
    saveLoading.value = false
  }
}

/**
 * 页面加载时获取用户基础信息和用户健康档案
 */
onMounted(() => {
  loadUserInfo()
  loadHealthProfile()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.profile-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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

.profile-tabs {
  margin-top: 20px;
}

.profile-form {
  padding: 20px 0;
}

.profile-form .el-form-item {
  margin-bottom: 24px;
}

.profile-form .el-textarea {
  width: 100%;
}

.profile-form .el-input-number {
  width: 100%;
}

:deep(.el-tabs__item) {
  font-size: 16px;
  font-weight: 500;
}

:deep(.el-tabs__nav-wrap::after) {
  background-color: #e4e7ed;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

:deep(.el-card__header) {
  background-color: #f8f9fa;
  border-bottom: 1px solid #e4e7ed;
}
</style>
