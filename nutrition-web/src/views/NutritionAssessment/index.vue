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
    <el-dialog v-model="resultDialogVisible" title="评估结果" width="500">
      <div v-if="assessmentResult">
        <p><strong>能量需求:</strong> {{ assessmentResult.energy }} kcal</p>
        <p><strong>蛋白质需求:</strong> {{ assessmentResult.protein }} g</p>
        <p><strong>脂肪需求:</strong> {{ assessmentResult.fat }} g</p>
        <p><strong>碳水化合物需求:</strong> {{ assessmentResult.carbohydrates }} g</p>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="resultDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import {
  assessNutritionRequirementUsingPost1
} from '@/nutrition-api/yingyangxuqiupingguguanli.ts'

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
      resultDialogVisible.value = true
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
</style>
