<template>
  <div class="diet-plan-page">
    <div class="page-header">
      <h2>饮食方案</h2>
      <p>创建个性化饮食方案，满足您的营养需求</p>
    </div>

    <!-- 创建饮食方案表单 -->
    <el-card class="create-plan-card">
      <template #header>
        <div class="card-header">
          <h3>创建饮食方案</h3>
        </div>
      </template>
      
      <el-form
        ref="dietPlanFormRef"
        :model="dietPlanForm"
        :rules="dietPlanFormRules"
        label-width="120px"
        class="diet-plan-form"
      >
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="方案名称" prop="planName">
              <el-input v-model="dietPlanForm.planName" placeholder="请输入方案名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="方案日期" prop="planDate">
              <el-date-picker
                v-model="dietPlanForm.planDate"
                type="date"
                placeholder="选择方案日期"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="营养需求ID" prop="requirementId">
              <div style="display: flex; align-items: center;">
                <div v-if="displayRequirementId" style="color: #606266; font-size: 14px;">
                  {{ displayRequirementId }}
                </div>
                <div v-else style="color: #C0C4CC; font-size: 14px;">
                  暂无ID
                </div>
                <el-tooltip v-if="route.query.requirementId" content="从营养评估页面传递的ID" placement="top">
                  <el-icon style="margin-left: 10px; color: #67C23A;"><InfoFilled /></el-icon>
                </el-tooltip>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户ID" prop="userId">
              <el-input 
                v-model="displayUserId" 
                placeholder="用户ID" 
                style="width: 100%"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="口味偏好" prop="tastePreference">
              <el-select v-model="dietPlanForm.tastePreference" placeholder="请选择口味偏好" style="width: 100%">
                <el-option label="清淡" :value="1" />
                <el-option label="适中" :value="2" />
                <el-option label="重口味" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否包含加餐" prop="includeSnacks">
              <el-switch 
                v-model="dietPlanForm.includeSnacks" 
                active-text="包含" 
                inactive-text="不包含" 
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="餐次分配比例" prop="mealRatio">
              <el-input v-model="dietPlanForm.mealRatio" placeholder="例如：3:4:3:0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否自动保存" prop="autoSave">
              <el-switch 
                v-model="dietPlanForm.autoSave" 
                active-text="自动保存" 
                inactive-text="不自动保存" 
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="烹饪方式偏好" prop="cookingMethods">
          <el-checkbox-group v-model="dietPlanForm.cookingMethods">
            <el-checkbox label="蒸" />
            <el-checkbox label="煮" />
            <el-checkbox label="炒" />
            <el-checkbox label="烤" />
            <el-checkbox label="炸" />
            <el-checkbox label="凉拌" />
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="饮食偏好" prop="dietaryPreferences">
          <el-checkbox-group v-model="dietPlanForm.dietaryPreferences">
            <el-checkbox label="素食" />
            <el-checkbox label="低盐" />
            <el-checkbox label="低糖" />
            <el-checkbox label="低脂" />
            <el-checkbox label="高蛋白" />
            <el-checkbox label="无乳糖" />
            <el-checkbox label="无麸质" />
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="忌口食物" prop="forbiddenFoods">
          <el-input
            v-model="forbiddenFoodsText"
            type="textarea"
            :rows="3"
            placeholder="请输入忌口食物，多个食物用逗号分隔"
            @input="handleForbiddenFoodsInput"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            生成饮食方案
          </el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 饮食方案结果 -->
    <el-dialog v-model="resultDialogVisible" title="饮食方案" width="80%" top="5vh">
      <div v-if="dietPlanResult" class="diet-plan-result">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="方案名称">{{ dietPlanResult.planName }}</el-descriptions-item>
          <el-descriptions-item label="方案日期">{{ dietPlanResult.planDate }}</el-descriptions-item>
          <el-descriptions-item label="方案状态">
            <el-tag :type="getStatusType(dietPlanResult.status)">
              {{ getStatusText(dietPlanResult.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="总热量">{{ dietPlanResult.totalCalories }} kcal</el-descriptions-item>
          <el-descriptions-item label="总蛋白质">{{ dietPlanResult.totalProtein }} g</el-descriptions-item>
          <el-descriptions-item label="总脂肪">{{ dietPlanResult.totalFat }} g</el-descriptions-item>
          <el-descriptions-item label="总碳水化合物">{{ dietPlanResult.totalCarbohydrate }} g</el-descriptions-item>
          <el-descriptions-item label="总膳食纤维">{{ dietPlanResult.totalFiber }} g</el-descriptions-item>
        </el-descriptions>

        <div class="meal-sections">
          <!-- 早餐 -->
          <div v-if="dietPlanResult.breakfast && dietPlanResult.breakfast.length > 0" class="meal-section">
            <h4>早餐</h4>
            <el-table :data="dietPlanResult.breakfast" style="width: 100%">
              <el-table-column prop="foodName" label="食物名称" />
              <el-table-column prop="amount" label="分量(g)" />
              <el-table-column prop="calories" label="热量(kcal)" />
              <el-table-column prop="protein" label="蛋白质(g)" />
              <el-table-column prop="fat" label="脂肪(g)" />
              <el-table-column prop="carbohydrate" label="碳水化合物(g)" />
              <el-table-column prop="cookingMethod" label="烹饪方式" />
              <el-table-column prop="remark" label="备注" />
            </el-table>
          </div>

          <!-- 午餐 -->
          <div v-if="dietPlanResult.lunch && dietPlanResult.lunch.length > 0" class="meal-section">
            <h4>午餐</h4>
            <el-table :data="dietPlanResult.lunch" style="width: 100%">
              <el-table-column prop="foodName" label="食物名称" />
              <el-table-column prop="amount" label="分量(g)" />
              <el-table-column prop="calories" label="热量(kcal)" />
              <el-table-column prop="protein" label="蛋白质(g)" />
              <el-table-column prop="fat" label="脂肪(g)" />
              <el-table-column prop="carbohydrate" label="碳水化合物(g)" />
              <el-table-column prop="cookingMethod" label="烹饪方式" />
              <el-table-column prop="remark" label="备注" />
            </el-table>
          </div>

          <!-- 晚餐 -->
          <div v-if="dietPlanResult.dinner && dietPlanResult.dinner.length > 0" class="meal-section">
            <h4>晚餐</h4>
            <el-table :data="dietPlanResult.dinner" style="width: 100%">
              <el-table-column prop="foodName" label="食物名称" />
              <el-table-column prop="amount" label="分量(g)" />
              <el-table-column prop="calories" label="热量(kcal)" />
              <el-table-column prop="protein" label="蛋白质(g)" />
              <el-table-column prop="fat" label="脂肪(g)" />
              <el-table-column prop="carbohydrate" label="碳水化合物(g)" />
              <el-table-column prop="cookingMethod" label="烹饪方式" />
              <el-table-column prop="remark" label="备注" />
            </el-table>
          </div>

          <!-- 加餐 -->
          <div v-if="dietPlanResult.snacks && dietPlanResult.snacks.length > 0" class="meal-section">
            <h4>加餐</h4>
            <el-table :data="dietPlanResult.snacks" style="width: 100%">
              <el-table-column prop="foodName" label="食物名称" />
              <el-table-column prop="amount" label="分量(g)" />
              <el-table-column prop="calories" label="热量(kcal)" />
              <el-table-column prop="protein" label="蛋白质(g)" />
              <el-table-column prop="fat" label="脂肪(g)" />
              <el-table-column prop="carbohydrate" label="碳水化合物(g)" />
              <el-table-column prop="cookingMethod" label="烹饪方式" />
              <el-table-column prop="remark" label="备注" />
            </el-table>
          </div>
        </div>

        <!-- 营养达标情况 -->
        <div v-if="dietPlanResult.nutritionCompliance" class="nutrition-compliance">
          <h4>营养达标情况</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="整体营养评分">
              <el-progress 
                :percentage="dietPlanResult.nutritionCompliance.overallScore || 0" 
                :color="getScoreColor(dietPlanResult.nutritionCompliance.overallScore || 0)"
              />
            </el-descriptions-item>
            <el-descriptions-item label="热量达标率">
              {{ dietPlanResult.nutritionCompliance.caloriesComplianceRate }}%
            </el-descriptions-item>
            <el-descriptions-item label="蛋白质达标率">
              {{ dietPlanResult.nutritionCompliance.proteinComplianceRate }}%
            </el-descriptions-item>
            <el-descriptions-item label="脂肪达标率">
              {{ dietPlanResult.nutritionCompliance.fatComplianceRate }}%
            </el-descriptions-item>
            <el-descriptions-item label="碳水化合物达标率">
              {{ dietPlanResult.nutritionCompliance.carbohydrateComplianceRate }}%
            </el-descriptions-item>
            <el-descriptions-item label="膳食纤维达标率">
              {{ dietPlanResult.nutritionCompliance.fiberComplianceRate }}%
            </el-descriptions-item>
          </el-descriptions>

          <div v-if="dietPlanResult.nutritionCompliance.nutritionAdvice" class="nutrition-advice">
            <h5>营养建议</h5>
            <ul>
              <li v-for="(advice, index) in dietPlanResult.nutritionCompliance.nutritionAdvice" :key="index">
                {{ advice }}
              </li>
            </ul>
          </div>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="resultDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="confirmDietPlan" v-if="dietPlanResult?.status === 1">
            确认方案
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { InfoFilled } from '@element-plus/icons-vue'
import { generateDietPlanUsingPost1, confirmDietPlanUsingPost1 } from '@/nutrition-api/yinshifanganguanli'
import request from '@/request'

const router = useRouter()
const route = useRoute()

const dietPlanFormRef = ref()
const submitLoading = ref(false)
const resultDialogVisible = ref(false)

const forbiddenFoodsText = ref('')



// 饮食方案表单
const dietPlanForm = reactive<API.DietPlanGenerateRequest>({
  planName: '',
  planDate: '',
  requirementId: 0,
  userId: 0,
  tastePreference: 2, // 默认适中
  includeSnacks: true,
  mealRatio: '3:4:3:0', // 默认餐次比例
  autoSave: true,
  cookingMethods: [],
  dietaryPreferences: [],
  forbiddenFoods: []
})

// 用于显示的ID（字符串类型，避免大整数精度丢失）
const displayRequirementId = ref('')
const displayUserId = ref('')

// 饮食方案结果
const dietPlanResult = ref<API.DietPlanVO>()

// 表单校验规则
const dietPlanFormRules = {
  planName: [{ required: true, message: '请输入方案名称', trigger: 'blur' }],
  planDate: [{ required: true, message: '请选择方案日期', trigger: 'change' }],
  requirementId: [{ required: true, message: '请输入营养需求ID', trigger: 'blur' }],
  userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }]
}

// 处理忌口食物输入
const handleForbiddenFoodsInput = () => {
  if (forbiddenFoodsText.value) {
    dietPlanForm.forbiddenFoods = forbiddenFoodsText.value.split(',').map(item => item.trim())
  } else {
    dietPlanForm.forbiddenFoods = []
  }
}

// 获取方案状态类型
const getStatusType = (status?: number) => {
  switch (status) {
    case 1: return 'info' // 草稿
    case 2: return 'success' // 已确认
    case 3: return 'warning' // 已执行
    default: return ''
  }
}

// 获取方案状态文本
const getStatusText = (status?: number) => {
  switch (status) {
    case 1: return '草稿'
    case 2: return '已确认'
    case 3: return '已执行'
    default: return '未知'
  }
}

// 获取评分颜色
const getScoreColor = (score: number) => {
  if (score >= 90) return '#67C23A'
  if (score >= 70) return '#E6A23C'
  return '#F56C6C'
}

// 提交表单生成饮食方案
const handleSubmit = async () => {
  if (!dietPlanFormRef.value) return
  try {
    await dietPlanFormRef.value.validate()
    submitLoading.value = true
    
    // 创建请求数据，将requirementId和userId作为字符串处理
    const requestData = {
      ...dietPlanForm,
      requirementId: displayRequirementId.value || dietPlanForm.requirementId.toString(),
      userId: displayUserId.value || dietPlanForm.userId.toString()
    }
    
    console.log('请求数据:', requestData)
    
    // 使用生成的API函数，但通过options参数传递自定义请求配置
    const response = await generateDietPlanUsingPost1(requestData, {
      headers: {
        'Content-Type': 'application/json',
      },
      // 自定义请求转换器，确保大整数ID作为字符串传递
      transformRequest: [(data) => {
        // 将requirementId和userId转换为字符串
        if (data && data.requirementId) {
          data.requirementId = data.requirementId.toString()
        }
        if (data && data.userId) {
          data.userId = data.userId.toString()
        }
        return JSON.stringify(data)
      }]
    })
    
    // 响应拦截器已经处理了code为200的情况，直接返回response
    // 所以这里不需要再次检查code，直接处理数据即可
    ElMessage.success('饮食方案生成成功')
    
    // 添加详细的调试信息
    console.log('完整响应对象:', response)
    console.log('响应数据:', response.data)
    console.log('响应数据类型:', typeof response.data)
    
    // 根据类型定义，响应数据应该是BaseResponseDietPlanVO_类型
    // 其中data字段是DietPlanVO类型
    if (response.data && response.data.data) {
      dietPlanResult.value = response.data.data
      console.log('设置的方案结果 (response.data.data):', dietPlanResult.value)
    } else if (response.data) {
      // 如果response.data.data不存在，尝试直接使用response.data
      dietPlanResult.value = response.data
      console.log('设置的方案结果 (response.data):', dietPlanResult.value)
    } else {
      // 如果response.data也不存在，尝试直接使用response
      dietPlanResult.value = response
      console.log('设置的方案结果 (response):', dietPlanResult.value)
    }
    
    // 检查dietPlanResult.value是否有值
    if (!dietPlanResult.value) {
      console.error('dietPlanResult.value为空')
      ElMessage.error('获取饮食方案数据失败')
      return
    }
    
    // 检查必要的字段是否存在
    if (!dietPlanResult.value.planId) {
      console.warn('dietPlanResult.value缺少planId字段')
    }
    
    if (!dietPlanResult.value.breakfast && !dietPlanResult.value.lunch && 
        !dietPlanResult.value.dinner && !dietPlanResult.value.snacks) {
      console.warn('dietPlanResult.value缺少餐食内容字段')
    }
    
    // 强制设置弹窗可见
    console.log('设置弹窗可见，当前值:', resultDialogVisible.value)
    resultDialogVisible.value = true
    console.log('弹窗可见性已设置为:', resultDialogVisible.value)
    
    // 添加一个延迟，确保弹窗显示
    setTimeout(() => {
      console.log('延迟检查弹窗状态:', resultDialogVisible.value)
      if (!resultDialogVisible.value) {
        console.warn('弹窗可能没有正确显示，尝试再次设置')
        resultDialogVisible.value = true
      }
    }, 100)
    
    // 添加一个简单的测试弹窗
    setTimeout(() => {
      console.log('测试弹窗是否显示')
      if (resultDialogVisible.value) {
        console.log('弹窗应该显示')
      } else {
        console.log('弹窗未显示')
      }
    }, 500)
  } catch (error) {
    console.error('生成饮食方案失败:', error)
    // 错误已经在响应拦截器中处理，这里不需要再次显示错误消息
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (dietPlanFormRef.value) {
    dietPlanFormRef.value.resetFields()
  }
  forbiddenFoodsText.value = ''
  dietPlanForm.cookingMethods = []
  dietPlanForm.dietaryPreferences = []
  dietPlanForm.forbiddenFoods = []
}

// 确认饮食方案
const confirmDietPlan = async () => {
  if (!dietPlanResult.value?.planId) return
  
  try {
    const response = await confirmDietPlanUsingPost1({ planId: dietPlanResult.value.planId })
    // 响应拦截器已经处理了code为200的情况，直接返回response
    // 所以这里不需要再次检查code，直接处理数据即可
    ElMessage.success('饮食方案确认成功')
    // 更新方案状态
    if (dietPlanResult.value) {
      dietPlanResult.value.status = 2
    }
  } catch (error) {
    console.error('确认饮食方案失败:', error)
    // 错误已经在响应拦截器中处理，这里不需要再次显示错误消息
  }
}









// 初始化表单数据
onMounted(() => {
  // 优先从URL参数获取用户ID，如果没有则从localStorage获取
  let userId = null
  if (route.query.userId) {
    userId = route.query.userId
  } else {
    // 从localStorage获取用户信息
    const userInfo = localStorage.getItem('userInfo')
    if (userInfo) {
      try {
        const parsedUserInfo = JSON.parse(userInfo)
        userId = parsedUserInfo.userId || 0
      } catch (error) {
        console.error('解析用户信息失败:', error)
        ElMessage.error('获取用户信息失败')
      }
    } else {
      ElMessage.error('请先登录')
      router.push('/login')
      return
    }
  }
  
  // 设置用户ID
  if (userId) {
    // 设置显示用的用户ID（字符串形式）
    displayUserId.value = userId.toString()
    // 设置表单中的用户ID（数字形式，用于API调用）
    try {
      // 如果是大整数，使用BigInt处理
      if (typeof userId === 'string' && userId.length > 15) {
        const bigIntId = BigInt(userId)
        if (bigIntId > BigInt(Number.MAX_SAFE_INTEGER)) {
          console.warn('用户ID超出JavaScript安全整数范围，使用近似值')
          dietPlanForm.userId = Number.MAX_SAFE_INTEGER
        } else {
          dietPlanForm.userId = Number(bigIntId)
        }
      } else {
        dietPlanForm.userId = Number(userId)
      }
    } catch (error) {
      console.error('转换用户ID失败:', error)
      dietPlanForm.userId = Number.MAX_SAFE_INTEGER
    }
  }
  
  // 如果从营养评估页面跳转过来，获取营养需求ID
  if (route.query.requirementId) {
    // 使用BigInt处理大整数，避免精度丢失
    try {
      const bigIntId = BigInt(route.query.requirementId as string)
      // 转换为字符串用于显示
      displayRequirementId.value = bigIntId.toString()
      // 尝试转换为Number，如果超出安全范围则使用近似值
      if (bigIntId > BigInt(Number.MAX_SAFE_INTEGER)) {
        console.warn('ID超出JavaScript安全整数范围，使用近似值')
        dietPlanForm.requirementId = Number.MAX_SAFE_INTEGER
      } else {
        dietPlanForm.requirementId = Number(bigIntId)
      }
    } catch (error) {
      console.error('解析营养需求ID失败:', error)
      ElMessage.error('营养需求ID格式错误')
    }
  }
  
  // 设置默认日期为今天
  const today = new Date()
  const year = today.getFullYear()
  const month = String(today.getMonth() + 1).padStart(2, '0')
  const day = String(today.getDate()).padStart(2, '0')
  dietPlanForm.planDate = `${year}-${month}-${day}`
  
  // 设置默认方案名称
  dietPlanForm.planName = `饮食方案_${year}${month}${day}`
})
</script>

<style scoped>
.diet-plan-page {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
}

.create-plan-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  color: #303133;
}

.diet-plan-form {
  max-width: 100%;
}

.diet-plan-result {
  max-height: 70vh;
  overflow-y: auto;
}

.meal-sections {
  margin-top: 20px;
}

.meal-section {
  margin-bottom: 20px;
}

.meal-section h4 {
  margin: 0 0 10px 0;
  color: #303133;
  border-bottom: 1px solid #EBEEF5;
  padding-bottom: 10px;
}

.nutrition-compliance {
  margin-top: 20px;
}

.nutrition-compliance h4 {
  margin: 0 0 10px 0;
  color: #303133;
  border-bottom: 1px solid #EBEEF5;
  padding-bottom: 10px;
}

.nutrition-advice {
  margin-top: 15px;
}

.nutrition-advice h5 {
  margin: 0 0 10px 0;
  color: #606266;
}

.nutrition-advice ul {
  margin: 0;
  padding-left: 20px;
}

.nutrition-advice li {
  margin-bottom: 5px;
  color: #606266;
}

.dialog-footer {
  text-align: right;
}
</style>