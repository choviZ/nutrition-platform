import request from '../request.ts'
import type {
  NutritionRecord,
  NutritionRecordRequest,
  NutritionGoal,
  NutritionGoalRequest,
  PageResponse
} from './types'

// 营养记录相关API

// 获取营养记录列表
export const getNutritionRecords = (params: {
  page?: number
  size?: number
  startDate?: string
  endDate?: string
}): Promise<PageResponse<NutritionRecord>> => {
  return request({
    url: '/nutrition/records',
    method: 'get',
    params
  })
}

// 创建营养记录
export const createNutritionRecord = (data: NutritionRecordRequest): Promise<NutritionRecord> => {
  return request({
    url: '/nutrition/records',
    method: 'post',
    data
  })
}

// 更新营养记录
export const updateNutritionRecord = (id: number, data: Partial<NutritionRecordRequest>): Promise<NutritionRecord> => {
  return request({
    url: `/nutrition/records/${id}`,
    method: 'put',
    data
  })
}

// 删除营养记录
export const deleteNutritionRecord = (id: number): Promise<void> => {
  return request({
    url: `/nutrition/records/${id}`,
    method: 'delete'
  })
}

// 获取指定日期的营养记录
export const getNutritionRecordsByDate = (date: string): Promise<NutritionRecord[]> => {
  return request({
    url: `/nutrition/records/date/${date}`,
    method: 'get'
  })
}

// 获取营养统计数据
export const getNutritionStats = (params: {
  startDate: string
  endDate: string
}): Promise<{
  totalCalories: number
  totalProtein: number
  totalCarbohydrates: number
  totalFat: number
  totalFiber: number
  totalSugar: number
  totalSodium: number
  avgCalories: number
  recordCount: number
}> => {
  return request({
    url: '/nutrition/stats',
    method: 'get',
    params
  })
}

// 营养目标相关API

// 获取用户营养目标
export const getNutritionGoal = (): Promise<NutritionGoal> => {
  return request({
    url: '/nutrition/goal',
    method: 'get'
  })
}

// 设置营养目标
export const setNutritionGoal = (data: NutritionGoalRequest): Promise<NutritionGoal> => {
  return request({
    url: '/nutrition/goal',
    method: 'post',
    data
  })
}

// 更新营养目标
export const updateNutritionGoal = (data: Partial<NutritionGoalRequest>): Promise<NutritionGoal> => {
  return request({
    url: '/nutrition/goal',
    method: 'put',
    data
  })
}
