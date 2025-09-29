<template>
  <div class="food-management">
    <el-card class="page-container">
      <template #header>
        <div class="card-header">
          <h2>食物管理</h2>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加食物
          </el-button>
        </div>
      </template>
      <!-- 搜索区域 -->
      <div class="search-section">
        <el-form :model="searchForm" :inline="true" class="search-form">
          <el-form-item label="食物名称">
            <el-input
              v-model="searchForm.foodName"
              placeholder="请输入食物名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <!-- 表格区域 -->
      <div class="table-section">
        <el-table :data="foodList" border stripe style="width: 100%">
          <el-table-column prop="foodName" label="食物名称" />
          <el-table-column prop="calories" label="热量 (kcal)" />
          <el-table-column prop="protein" label="蛋白质 (g)" />
          <el-table-column prop="fat" label="脂肪 (g)" />
          <el-table-column prop="carbohydrates" label="碳水化合物 (g)" />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleEdit(row)">
                编辑
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="handleDelete(row)"
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
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>
    <!-- 添加/编辑食物对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="foodFormRef"
        :model="foodForm"
        :rules="foodFormRules"
        label-width="120px"
      >
        <el-form-item label="食物名称" prop="foodName">
          <el-input v-model="foodForm.foodName" placeholder="请输入食物名称" />
        </el-form-item>
        <el-form-item label="热量 (kcal)" prop="calories">
          <el-input-number v-model="foodForm.calories" :min="0" />
        </el-form-item>
        <el-form-item label="蛋白质 (g)" prop="protein">
          <el-input-number v-model="foodForm.protein" :min="0" />
        </el-form-item>
        <el-form-item label="脂肪 (g)" prop="fat">
          <el-input-number v-model="foodForm.fat" :min="0" />
        </el-form-item>
        <el-form-item label="碳水化合物 (g)" prop="carbohydrates">
          <el-input-number v-model="foodForm.carbohydrates" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDialogClose">取 消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            {{ submitLoading ? '提交中...' : '确 定' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

// 导入食物营养数据管理 API
import {
  listFoodNutritionByPageGetUsingGet1,
  addFoodNutritionUsingPost1,
  updateFoodNutritionUsingPost1,
  deleteFoodNutritionUsingPost1
} from '@/nutrition-api/shiwuyingyangshujuguanli'

// 响应式数据
const searchForm = reactive({
  foodName: '',
})
const foodList = ref([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0,
})
const dialogVisible = ref(false)
const dialogTitle = ref('')
const foodFormRef = ref<FormInstance>()
const foodForm = reactive({
  id: undefined,
  foodName: '',
  calories: 0,
  protein: 0,
  fat: 0,
  carbohydrates: 0,
})

const foodFormRules = reactive<FormRules>({
  foodName: [{ required: true, message: '请输入食物名称', trigger: 'blur' }],
  calories: [{ required: true, message: '请输入热量', trigger: 'blur' }],
  protein: [{ required: true, message: '请输入蛋白质含量', trigger: 'blur' }],
  fat: [{ required: true, message: '请输入脂肪含量', trigger: 'blur' }],
  carbohydrates: [{ required: true, message: '请输入碳水化合物含量', trigger: 'blur' }],
})

const submitLoading = ref(false)
const isEdit = ref(false)

/**
 * 加载列表
 */
const loadFoodList = async () => {
  try {
    const params = {
      foodName: searchForm.foodName || undefined,
      current: pagination.current,
      pageSize: pagination.size,
    }
    const res = await listFoodNutritionByPageGetUsingGet1(params)
    if (res.data.code === 200) {
      // 转换数据结构以匹配前端表格
      foodList.value = res.data.data?.records?.map(item => ({
        id: item.foodId,
        foodName: item.foodName,
        calories: item.caloriesPer100g,
        protein: item.proteinPer100g,
        fat: item.fatPer100g,
        carbohydrates: item.carbohydratePer100g
      })) || []
      pagination.total = typeof res.data.data?.total === 'string' ? parseInt(res.data.data?.total) : (res.data.data?.total || 0)
    } else {
      ElMessage.error(res.data.message || '获取食物列表失败')
    }
  } catch (error) {
    console.error('获取食物列表失败:', error)
    ElMessage.error('获取食物列表失败')
  }
}

/**
 * 搜索
 */
const handleSearch = () => {
  pagination.current = 1
  loadFoodList()
}
/**
 * 重置食物表单
 */
const handleReset = () => {
  searchForm.foodName = ''
  handleSearch()
}

/**
 * 添加食物
 */
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '添加食物'
  resetFoodForm()
  dialogVisible.value = true
}

/**
 * 编辑
 * @param row
 */
const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑食物'
  Object.assign(foodForm, row)
  dialogVisible.value = true
}

/**
 * 删除
 * @param row
 */
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除食物【${row.foodName}】吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      const res = await deleteFoodNutritionUsingPost1({ id:row.id })
      if (res.data.code === 200) {
        ElMessage.success('删除成功')
        await loadFoodList()
      } else {
        ElMessage.error(res.data.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

/**
 * 提交表单
 */
const handleSubmit = async () => {
  await foodFormRef.value?.validate()
  submitLoading.value = true
  try {
    let res
    if (isEdit.value) {
      // 更新食物数据
      const updateData:API.FoodNutritionUpdateRequest = {
        foodId: foodForm.id,
        foodName: foodForm.foodName,
        caloriesPer100g: foodForm.calories,
        proteinPer100g: foodForm.protein,
        fatPer100g: foodForm.fat,
        carbohydratePer100g: foodForm.carbohydrates
      }
      res = await updateFoodNutritionUsingPost1(updateData)
    } else {
      // 添加食物数据
      const addData = {
        foodName: foodForm.foodName,
        caloriesPer100g: foodForm.calories,
        proteinPer100g: foodForm.protein,
        fatPer100g: foodForm.fat,
        carbohydratePer100g: foodForm.carbohydrates
      }
      res = await addFoodNutritionUsingPost1(addData)
    }
    if (res.data.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
      dialogVisible.value = false
      await loadFoodList()
    } else {
      ElMessage.error(res.data.message || (isEdit.value ? '更新失败' : '添加失败'))
    }
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error(isEdit.value ? '更新失败' : '添加失败')
  } finally {
    submitLoading.value = false
  }
}

/**
 * 关闭弹窗
 */
const handleDialogClose = () => {
  dialogVisible.value = false
  resetFoodForm()
  foodFormRef.value?.clearValidate()
}

/**
 * 重置食物表单
 */
const resetFoodForm = () => {
  Object.assign(foodForm, {
    id: undefined,
    foodName: '',
    calories: 0,
    protein: 0,
    fat: 0,
    carbohydrates: 0,
  })
}

/**
 * 分页
 * @param size
 */
const handleSizeChange = (size: number) => {
  pagination.size = size
  loadFoodList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  loadFoodList()
}

/**
 * 页面加载时获取数据
 */
onMounted(() => {
  loadFoodList()
})
</script>

<style scoped>
.food-management {
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
</style>
