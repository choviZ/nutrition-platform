<template>
  <div class="diet-plan-records-page">
    <div class="page-header">
      <h2>饮食方案记录</h2>
      <p>查看和管理您的所有饮食方案记录</p>
    </div>
    <!-- 用户饮食方案列表 -->
    <el-card class="diet-plan-list-card">
      <template #header>
        <div class="card-header">
          <h3>我的饮食方案列表</h3>
          <el-button type="primary" size="small" @click="loadDietPlanList" :loading="listLoading">
            刷新列表
          </el-button>
        </div>
      </template>

      <el-table :data="dietPlanList" style="width: 100%" v-loading="listLoading">
        <el-table-column prop="planName" label="方案名称"/>
        <el-table-column prop="planDate" label="方案日期"/>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalCalories" label="总热量(kcal)"/>
        <el-table-column prop="totalProtein" label="蛋白质(g)"/>
        <el-table-column prop="totalFat" label="脂肪(g)"/>
        <el-table-column prop="totalCarbohydrate" label="碳水(g)"/>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="viewDietPlan(scope.row)">
              查看
            </el-button>
            <el-button size="small" type="success" @click="confirmDietPlanById(scope.row)" v-if="scope.row.status === 1">
              确认
            </el-button>
            <el-button size="small" type="danger" @click="deleteDietPlan(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="dietPlanList.length === 0 && !listLoading" class="empty-list">
        <el-empty description="暂无饮食方案" />
      </div>
    </el-card>

    <!-- 饮食方案详情对话框 -->
    <el-dialog v-model="resultDialogVisible" title="饮食方案详情" width="80%" top="5vh">
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
        </el-descriptions>

        <div class="meal-sections">
          <!-- 早餐 -->
          <div v-if="dietPlanResult.breakfast && dietPlanResult.breakfast.length > 0" class="meal-section">
            <h4>早餐</h4>
            <el-table :data="dietPlanResult.breakfast" style="width: 100%">
              <el-table-column prop="foodName" label="食物名称" width="150" />
              <el-table-column prop="amount" label="分量(g)" width="90" />
              <el-table-column prop="calories" label="热量(kcal)" width="100" />
              <el-table-column prop="protein" label="蛋白质(g)" width="90" />
              <el-table-column prop="fat" label="脂肪(g)" width="90" />
              <el-table-column prop="carbohydrate" label="碳水化合物(g)" width="120" />
              <el-table-column prop="cookingMethod" label="烹饪方式" width="100" />
              <el-table-column prop="remark" label="备注" min-width="120" />
            </el-table>
          </div>

          <!-- 午餐 -->
          <div v-if="dietPlanResult.lunch && dietPlanResult.lunch.length > 0" class="meal-section">
            <h4>午餐</h4>
            <el-table :data="dietPlanResult.lunch" style="width: 100%">
              <el-table-column prop="foodName" label="食物名称" width="150" />
              <el-table-column prop="amount" label="分量(g)" width="90" />
              <el-table-column prop="calories" label="热量(kcal)" width="100" />
              <el-table-column prop="protein" label="蛋白质(g)" width="90" />
              <el-table-column prop="fat" label="脂肪(g)" width="90" />
              <el-table-column prop="carbohydrate" label="碳水化合物(g)" width="120" />
              <el-table-column prop="cookingMethod" label="烹饪方式" width="100" />
              <el-table-column prop="remark" label="备注" min-width="120" />
            </el-table>
          </div>

          <!-- 晚餐 -->
          <div v-if="dietPlanResult.dinner && dietPlanResult.dinner.length > 0" class="meal-section">
            <h4>晚餐</h4>
            <el-table :data="dietPlanResult.dinner" style="width: 100%">
              <el-table-column prop="foodName" label="食物名称" width="150" />
              <el-table-column prop="amount" label="分量(g)" width="90" />
              <el-table-column prop="calories" label="热量(kcal)" width="100" />
              <el-table-column prop="protein" label="蛋白质(g)" width="90" />
              <el-table-column prop="fat" label="脂肪(g)" width="90" />
              <el-table-column prop="carbohydrate" label="碳水化合物(g)" width="120" />
              <el-table-column prop="cookingMethod" label="烹饪方式" width="100" />
              <el-table-column prop="remark" label="备注" min-width="120" />
            </el-table>
          </div>

          <!-- 加餐 -->
          <div v-if="dietPlanResult.snacks && dietPlanResult.snacks.length > 0" class="meal-section">
            <h4>加餐</h4>
            <el-table :data="dietPlanResult.snacks" style="width: 100%">
              <el-table-column prop="foodName" label="食物名称" width="150" />
              <el-table-column prop="amount" label="分量(g)" width="90" />
              <el-table-column prop="calories" label="热量(kcal)" width="100" />
              <el-table-column prop="protein" label="蛋白质(g)" width="90" />
              <el-table-column prop="fat" label="脂肪(g)" width="90" />
              <el-table-column prop="carbohydrate" label="碳水化合物(g)" width="120" />
              <el-table-column prop="cookingMethod" label="烹饪方式" width="100" />
              <el-table-column prop="remark" label="备注" min-width="120" />
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyDietPlansUsingGet1, confirmDietPlanUsingPost1, deleteDietPlanUsingDelete1 } from '@/nutrition-api/yinshifanganguanli'

// 饮食方案列表
const dietPlanList = ref<API.DietPlanVO[]>([])
const listLoading = ref(false)
const resultDialogVisible = ref(false)
const dietPlanResult = ref<API.DietPlanVO>()

// 加载饮食方案列表
const loadDietPlanList = async () => {
  listLoading.value = true
  try {
    const response = await getMyDietPlansUsingGet1({})
    // 根据拦截器实现，response.data.code === 200时返回的是整个response对象
    // 所以我们需要访问response.data.data来获取实际数据
    if (response.data && response.data.data && Array.isArray(response.data.data)) {
      dietPlanList.value = response.data.data
    } else {
      dietPlanList.value = []
    }
  } catch (error) {
    console.error('获取饮食方案列表失败:', error)
    // 错误已经在响应拦截器中处理，这里不需要再次显示错误消息
    dietPlanList.value = []
  } finally {
    listLoading.value = false
  }
}

// 查看饮食方案
const viewDietPlan = (plan: API.DietPlanVO) => {
  dietPlanResult.value = plan
  resultDialogVisible.value = true
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
    // 刷新列表
    loadDietPlanList()
  } catch (error) {
    console.error('确认饮食方案失败:', error)
    // 错误已经在响应拦截器中处理，这里不需要再次显示错误消息
  }
}

// 通过ID确认饮食方案
const confirmDietPlanById = async (plan: API.DietPlanVO) => {
  if (!plan.planId) return

  try {
    const response = await confirmDietPlanUsingPost1({ planId: plan.planId })
    // 响应拦截器已经处理了code为200的情况，直接返回response
    // 所以这里不需要再次检查code，直接处理数据即可
    ElMessage.success('饮食方案确认成功')
    // 刷新列表
    loadDietPlanList()
  } catch (error) {
    console.error('确认饮食方案失败:', error)
    // 错误已经在响应拦截器中处理，这里不需要再次显示错误消息
  }
}

// 删除饮食方案
const deleteDietPlan = async (plan: API.DietPlanVO) => {
  if (!plan.planId) return

  try {
    await ElMessageBox.confirm('确定要删除该饮食方案吗？此操作不可撤销', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const response = await deleteDietPlanUsingDelete1({ planId: plan.planId })
    // 响应拦截器已经处理了code为200的情况，直接返回response
    // 所以这里不需要再次检查code，直接处理数据即可
    ElMessage.success('饮食方案删除成功')
    // 刷新列表
    loadDietPlanList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除饮食方案失败:', error)
      // 错误已经在响应拦截器中处理，这里不需要再次显示错误消息
    }
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

// 初始化
onMounted(() => {
  loadDietPlanList()
})
</script>

<style scoped>
.diet-plan-records-page {
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

.diet-plan-list-card {
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

.empty-list {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 0;
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
