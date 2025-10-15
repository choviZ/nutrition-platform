<template>
  <div class="nutrition-page">
    <div class="page-header">
      <h2>营养记录</h2>
      <p>记录和管理您的每日营养摄入</p>
      <div class="operation-bar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon> 添加记录
        </el-button>
        <el-input
          v-model="searchKey"
          placeholder="搜索食物名称"
          style="width: 240px; margin-left: 16px"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>
    </div>

    <el-table
      :data="filteredRecords"
      style="width: 100%"
      stripe
      v-loading="loading"
    >
      <el-table-column prop="createTime" label="时间" width="180" sortable>
        <template #default="{ row }">
          {{ formatDate(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="foodName" label="食物名称" />
      <el-table-column prop="foodAmount" label="数量" />
      <el-table-column prop="calories" label="热量" />
      <el-table-column prop="protein" label="蛋白质" />
      <el-table-column prop="carbohydrate" label="碳水化合物" />
      <el-table-column prop="fat" label="脂肪"/>
      <el-table-column prop="mealType" label="餐次" width="100">
          <template #default="{ row }">
            {{ getMealTypeName(row.mealType) }}
          </template>
        </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button
            type="primary"
            size="small"
            @click="handleEdit(row)"
            >编辑</el-button
          >
          <el-button
            type="danger"
            size="small"
            @click="handleDelete(row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEditMode ? '编辑记录' : '新增记录'"
      width="500px"
    >
      <el-form
        :model="formData"
        label-width="80px"
        ref="formRef"
        :rules="formRules"
      >
        <el-form-item label="食物名称" prop="foodName">
          <el-autocomplete
            v-model="formData.foodName"
            :fetch-suggestions="queryFoodSuggestions"
            placeholder="请输入食物名称"
            @select="handleFoodSelect"
            @input="handleFoodInput"
            clearable
          />
        </el-form-item>
        <el-form-item label="数量(g)" prop="foodAmount">
          <el-input-number
            v-model="formData.foodAmount"
            :min="1"
            :max="1000"
          />
        </el-form-item>
        <el-form-item label="餐次" prop="mealType">
          <el-select v-model="formData.mealType" placeholder="请选择餐次">
            <el-option label="早餐" :value="1" />
            <el-option label="午餐" :value="2" />
            <el-option label="晚餐" :value="3" />
            <el-option label="加餐" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间" prop="createTime">
          <el-date-picker
            v-model="formData.createTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  addDietRecordUsingPost1, deleteDietRecordUsingDelete1,
  queryDietRecordsGetUsingGet1 as getDietRecords, updateDietRecordUsingPost1
} from '@/nutrition-api/yinshijiluguanli'
import { searchFoodByNameUsingGet1 } from '@/nutrition-api/shiwuyingyangshujuguanli'

interface DietRecord {
  id: string
  foodName: string
  foodAmount: number
  createTime: string
  calories: number
  mealType: number
}

// 表格数据
const loading = ref(false)
const records = ref<DietRecord[]>([])
const searchKey = ref('')

// 对话框相关
const dialogVisible = ref(false)
const isEditMode = ref(false)
const selectedFood = ref<any>(null) // 存储用户选择的食物对象
const formData = ref<Partial<DietRecord>>({
  foodName: '',
  foodAmount: 100,
  createTime: new Date().toISOString(),
  mealType: 1
})

// 表单验证规则
const formRules = {
  foodName: [
    { required: true, message: '请输入食物名称', trigger: 'blur' },
    { 
      validator: (rule: any, value: string, callback: Function) => {
        if (!value) {
          callback(new Error('请输入食物名称'))
          return
        }
        // 检查是否选择了有效的食物
        if (!selectedFood.value || selectedFood.value.foodName !== value) {
          callback(new Error('请从下拉列表中选择有效的食物'))
          return
        }
        callback()
      }, 
      trigger: 'blur' 
    }
  ],
  foodAmount: [{ required: true, message: '请输入数量', trigger: 'blur' }],
  createTime: [{ required: true, message: '请选择时间', trigger: 'change' }],
  mealType: [{ required: true, message: '请选择餐次', trigger: 'change' }]
}

// 过滤后的记录
const filteredRecords = computed(() => {
  return records.value.filter(item =>
    item.foodName.includes(searchKey.value.trim())
  )
})

// 加载数据
const loadData = async () => {
  try {
    loading.value = true
    const res = await getDietRecords({
      current: 1,
      pageSize: 20
    })
    records.value = res.data.data.records
  } catch (error) {
    ElMessage.error('数据加载失败')
  } finally {
    loading.value = false
  }
}

// 新增记录
const handleAdd = () => {
  isEditMode.value = false
  selectedFood.value = null // 重置选择的食物
  formData.value = {
    foodName: '',
    foodAmount: 100,
    createTime: new Date().toISOString(),
    mealType: 1
  }
  dialogVisible.value = true
}

// 编辑记录
const handleEdit = (row: DietRecord) => {
  isEditMode.value = true
  selectedFood.value = null // 重置选择的食物
  formData.value = { ...row }
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  try {
    if (isEditMode.value) {
      await updateDietRecordUsingPost1(formData.value)
    } else {
      await addDietRecordUsingPost1({
        foodName: formData.value.foodName,
        foodAmount: formData.value.foodAmount,
        createTime: formData.value.createTime,
        mealType: formData.value.mealType
      })
    }
    ElMessage.success('操作成功')
    dialogVisible.value = false
    await loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 删除记录
const handleDelete = async (id: string) => {
  try {
    await ElMessageBox.confirm('确定删除该记录？', '警告', {
      type: 'warning'
    })
    await deleteDietRecordUsingDelete1(id)
    ElMessage.success('删除成功')
    await loadData()
  } catch (error) {
    ElMessage.info('取消删除')
  }
}

onMounted(() => {
  loadData()
})

// 添加日期格式化方法
const formatDate = (isoString?: string) => {
  if (!isoString) return '--'
  const date = new Date(isoString)
  if (isNaN(date.getTime())) return '--'
  return `${date.getFullYear()}年${(date.getMonth() + 1).toString().padStart(2, '0')}月${date.getDate().toString().padStart(2, '0')}日 ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

// 添加餐次名称转换方法
const getMealTypeName = (mealType: number) => {
  switch (mealType) {
    case 1: return '早餐'
    case 2: return '午餐'
    case 3: return '晚餐'
    case 4: return '加餐'
    default: return '未知'
  }
}

// 查询食物建议
const queryFoodSuggestions = async (queryString: string, cb: (arg: any[]) => void) => {
  if (!queryString || queryString.length < 1) {
    cb([])
    return
  }
  
  try {
    const response = await searchFoodByNameUsingGet1({ foodName: queryString })
    if (response.data && response.data.code === 200 && response.data.data) {
      // 将API返回的数据转换为自动完成组件需要的格式
      const suggestions = response.data.data.map(item => ({
        value: item.foodName,
        item: item
      }))
      cb(suggestions)
    } else {
      cb([])
    }
  } catch (error) {
    console.error('查询食物失败:', error)
    cb([])
  }
}

// 处理食物选择
const handleFoodSelect = (item: any) => {
  // 当用户选择一个食物时，将食物名称设置到表单中
  formData.value.foodName = item.value
  // 保存选择的食物对象
  selectedFood.value = item.item
}

// 处理食物输入
const handleFoodInput = () => {
  // 当用户手动输入时，清除选择的食物对象
  selectedFood.value = null
}
</script>

<style scoped>
.operation-bar {
  display: flex;
  align-items: center;
  margin-top: 20px;
}

.el-table {
  margin-top: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.nutrition-page {
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

.content-placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}
</style>

