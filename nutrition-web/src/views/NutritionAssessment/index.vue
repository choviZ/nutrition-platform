<template>
  <div class="nutrition-assessment-page">
    <div class="page-header">
      <h2>营养需求评估</h2>
      <p>输入您的基本信息，获取科学的营养建议</p>
    </div>
    <el-card class="assessment-card">
      <template #header>
        <div class="card-header">
          <h3>基本信息</h3>
        </div>
      </template>
      <el-form
        ref="assessmentFormRef"
        :model="assessmentForm"
        :rules="assessmentFormRules"
        label-width="120px"
        class="assessment-form"
      >
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input-number
                v-model="assessmentForm.age"
                :min="1"
                :max="120"
                placeholder="请输入年龄"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="assessmentForm.gender" placeholder="请选择性别"
                         style="width: 100%">
                <el-option label="男" :value="1" />
                <el-option label="女" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="体重(kg)" prop="weight">
              <el-input-number
                v-model="assessmentForm.weight"
                :min="20"
                :max="300"
                :precision="1"
                placeholder="请输入体重"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身高(cm)" prop="height">
              <el-input-number
                v-model="assessmentForm.height"
                :min="50"
                :max="250"
                :precision="1"
                placeholder="请输入身高"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动水平" prop="activityLevel">
              <el-select v-model="assessmentForm.activityLevel" placeholder="请选择活动水平"
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
          <el-col :span="12">
            <el-form-item label="健康目标" prop="healthGoal">
              <el-select v-model="assessmentForm.healthGoal" placeholder="请选择健康目标" style="width: 100%">
                <el-option label="减肥" :value="1" />
                <el-option label="增肌" :value="2" />
                <el-option label="维持健康" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否保存" prop="saveResult">
              <el-switch v-model="assessmentForm.saveResult" active-text="保存" inactive-text="不保存" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            提交评估
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-dialog v-model="resultDialogVisible" title="评估结果" width="600">
      <div v-if="assessmentResult">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="身高">{{ currentRecordHeight }} cm</el-descriptions-item>
          <el-descriptions-item label="体重">{{ currentRecordWeight }} kg</el-descriptions-item>
          <el-descriptions-item label="BMI值">{{ assessmentResult.bmi }}</el-descriptions-item>
          <el-descriptions-item label="BMI状态">{{ assessmentResult.bmiStatus }}</el-descriptions-item>
          <el-descriptions-item label="基础代谢率(BMR)">{{ assessmentResult.bmr }} kcal</el-descriptions-item>
          <el-descriptions-item label="每日热量需求">{{ assessmentResult.dailyCalories }} kcal</el-descriptions-item>
          <el-descriptions-item label="蛋白质需求">{{ assessmentResult.proteinRequirement }} g</el-descriptions-item>
          <el-descriptions-item label="脂肪需求">{{ assessmentResult.fatRequirement }} g</el-descriptions-item>
          <el-descriptions-item label="碳水化合物需求">{{ assessmentResult.carbohydrateRequirement }} g</el-descriptions-item>
          <el-descriptions-item label="膳食纤维需求">{{ assessmentResult.fiberRequirement }} g</el-descriptions-item>
          <el-descriptions-item label="钙需求">{{ assessmentResult.calciumRequirement }} mg</el-descriptions-item>
          <el-descriptions-item label="铁需求">{{ assessmentResult.ironRequirement }} mg</el-descriptions-item>
          <el-descriptions-item label="维生素A需求">{{ assessmentResult.vitaminARequirement }} μg</el-descriptions-item>
          <el-descriptions-item label="维生素C需求">{{ assessmentResult.vitaminCRequirement }} mg</el-descriptions-item>
          <el-descriptions-item label="健康建议">{{ assessmentResult.healthAdvice }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="resultDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 评估记录表格 -->
    <el-card class="assessment-records-card">
      <template #header>
        <div class="card-header">
          <h3>评估记录</h3>
          <el-button type="primary" @click="fetchAssessmentRecords" :loading="recordsLoading">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>

      <el-table :data="assessmentRecords" style="width: 100%" v-loading="recordsLoading">
        <el-table-column prop="requirementId" label="需求ID" width="80" />
        <el-table-column prop="assessmentDate" label="评估日期">
          <template #default="{ row }">
            {{ formatDate(row.assessmentDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="bmi" label="BMI">
          <template #default="{ row }">
            {{ row.bmi || '暂无数据' }}
          </template>
        </el-table-column>
        <el-table-column prop="bmiStatus" label="BMI状态">
          <template #default="{ row }">
            {{ row.bmiStatus || '暂无数据' }}
          </template>
        </el-table-column>
        <el-table-column prop="bmr" label="基础代谢率(kcal)" />
        <el-table-column prop="dailyCalories" label="每日热量需求(kcal)" />
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-space direction="vertical">
              <el-button type="primary" size="small" @click="viewRecordDetail(row)">
                查看详情
              </el-button>
              <el-button type="success" size="small" @click="createDietPlan(row)">
                创建饮食方案
              </el-button>
              <el-button type="danger" size="small" @click="deleteRecord(row.requirementId)">
                删除
              </el-button>
            </el-space>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import {
  assessNutritionRequirementUsingPost1,
  queryNutritionRequirementsUsingPost1,
  deleteNutritionRequirementUsingGet1,
  getNutritionRequirementByIdUsingGet1
} from '@/nutrition-api/yingyangxuqiupingguguanli'

const router = useRouter()
const assessmentFormRef = ref()
const submitLoading = ref(false)
const resultDialogVisible = ref(false)

// 评估表单
const assessmentForm = reactive<API.NutritionAssessmentRequest>({
  healthGoal: 3, // 健康目标默认值
  saveResult: true, // 是否保存评估结果默认值
  height: 165,
  age: 20,
  gender: 0,
  weight: 60,
  activityLevel: 1
})

// 评估表单校验规则
const assessmentFormRules = {
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  weight: [{ required: true, message: '请输入体重', trigger: 'blur' }],
  activityLevel: [{ required: true, message: '请选择活动水平', trigger: 'change' }],
  healthGoal: [
    { required: true, message: '请选择健康目标', trigger: 'change' },
  ]
}

// 评估结果
const assessmentResult = ref<API.NutritionAssessmentResponse>()

// 当前查看的记录的身高和体重
const currentRecordHeight = ref(165)
const currentRecordWeight = ref(60)

// 评估记录相关数据
const assessmentRecords = ref<API.NutritionAssessmentResponse[]>([])
const recordsLoading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

/**
 * 处理提交
 */
const handleSubmit = async () => {
  if (!assessmentFormRef.value) return
  try {
    await assessmentFormRef.value.validate()
    submitLoading.value = true
    const response = await assessNutritionRequirementUsingPost1(assessmentForm)
    if (response.data.code === 200) {
      ElMessage.success('营养需求评估成功')
      assessmentResult.value = response.data.data
      // 设置当前记录的身高和体重
      currentRecordHeight.value = assessmentForm.height
      currentRecordWeight.value = assessmentForm.weight
      resultDialogVisible.value = true
      // 刷新评估记录列表
      fetchAssessmentRecords()
    } else {
      ElMessage.error(response.data.message || '评估失败')
    }
  } catch (error) {
    console.error('评估失败:', error)
    ElMessage.error('评估失败')
  } finally {
    submitLoading.value = false
  }
}

/**
 * 获取评估记录列表
 */
const fetchAssessmentRecords = async () => {
  recordsLoading.value = true
  try {
    // 从localStorage获取用户信息
    const userInfoStr = localStorage.getItem('userInfo')
    let userId = null
    if (userInfoStr) {
      try {
        const parsed = JSON.parse(userInfoStr)
        userId = parsed.userId
      } catch (e) {
        console.error('解析用户信息失败:', e)
      }
    }
    
    // 构建查询参数，添加用户ID以只查询当前用户的记录
    const queryParams: API.NutritionQueryRequest = {
      current: currentPage.value,
      pageSize: pageSize.value
    }
    
    // 如果有用户ID，添加到查询参数中
    if (userId) {
      queryParams.userId = userId
    }
    
    const response = await queryNutritionRequirementsUsingPost1(queryParams)
    if (response.data.code === 200) {
      // 根据API类型定义，response.data.data直接就是NutritionAssessmentResponse[]
      assessmentRecords.value = response.data.data || []
      total.value = response.data.data?.length || 0
      console.log('获取到的评估记录:', assessmentRecords.value)
    } else {
      ElMessage.error(response.data.message || '获取评估记录失败')
    }
  } catch (error) {
    console.error('获取评估记录失败:', error)
    ElMessage.error('获取评估记录失败')
  } finally {
    recordsLoading.value = false
  }
}

/**
 * 查看评估记录详情
 */
const viewRecordDetail = async (record: API.NutritionAssessmentResponse) => {
  try {
    const response = await getNutritionRequirementByIdUsingGet1({ id: record.requirementId || 0 })
    if (response.data.code === 200) {
      assessmentResult.value = response.data.data
      console.log('获取到的评估详情:', assessmentResult.value)
      // 暂时使用默认值，因为API返回的数据中不包含身高和体重信息
      // 在实际应用中，可能需要从其他地方获取这些信息，或者修改后端API
      currentRecordHeight.value = 165
      currentRecordWeight.value = 60
      resultDialogVisible.value = true
    } else {
      ElMessage.error(response.data.message || '获取评估详情失败')
    }
  } catch (error) {
    console.error('获取评估详情失败:', error)
    ElMessage.error('获取评估详情失败')
  }
}

/**
 * 删除评估记录
 */
const deleteRecord = async (requirementId: number) => {
  try {
    await ElMessageBox.confirm('确定要删除这条评估记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const response = await deleteNutritionRequirementUsingGet1({ id: requirementId })
    if (response.data.code === 200) {
      ElMessage.success('删除成功')
      fetchAssessmentRecords()
    } else {
      ElMessage.error(response.data.message || '删除失败')
    }
  } catch (error: any) {
    if (error !== 'cancel' && error !== 'close') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

/**
 * 创建饮食方案
 */
const createDietPlan = (record: API.NutritionAssessmentResponse) => {
  if (!record.requirementId) {
    ElMessage.warning('该记录没有营养需求ID，无法创建饮食方案')
    return
  }

  // 从localStorage获取用户ID
  const userInfo = localStorage.getItem('userInfo')
  let userId = null
  if (userInfo) {
    try {
      const parsedUserInfo = JSON.parse(userInfo)
      userId = parsedUserInfo.userId
    } catch (error) {
      console.error('解析用户信息失败:', error)
    }
  }

  // 跳转到饮食方案页面，并传递营养需求ID和用户ID
  router.push({
    path: '/diet-plan',
    query: {
      requirementId: record.requirementId,
      userId: userId || ''
    }
  })
}

/**
 * 处理页码变化
 */
const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchAssessmentRecords()
}

/**
 * 处理每页条数变化
 */
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchAssessmentRecords()
}

/**
 * 格式化日期
 */
const formatDate = (dateString: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)

  // 获取年月日
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')

  // 返回格式化后的日期字符串，只包含年月日
  return `${year}-${month}-${day}`
}

/**
 * 获取健康目标文本
 */
const getHealthGoalText = (goal: number) => {
  switch (goal) {
    case 1: return '减肥'
    case 2: return '增肌'
    case 3: return '维持健康'
    default: return '未知'
  }
}

// 页面加载时获取评估记录
onMounted(() => {
  fetchAssessmentRecords()
})
</script>

<style scoped>
.nutrition-assessment-page {
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

.assessment-card {
  margin-bottom: 40px;
}

.query-form .el-form-item {
  margin-bottom: 24px;
}

.assessment-records-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
