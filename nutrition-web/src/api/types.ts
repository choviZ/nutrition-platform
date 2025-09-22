// 通用API响应类型
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 分页响应类型
export interface PageResponse<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

// 用户相关类型
export interface User {
  id: number
  username: string
  email: string
  nickname?: string
  avatar?: string
  createTime: string
  updateTime: string
}

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  user: User
}

export interface RegisterRequest {
  username: string
  password: string
  email: string
  nickname?: string
}

// 营养记录相关类型
export interface NutritionRecord {
  id: number
  userId: number
  foodName: string
  calories: number
  protein: number
  carbohydrates: number
  fat: number
  fiber: number
  sugar: number
  sodium: number
  recordDate: string
  createTime: string
  updateTime: string
}

export interface NutritionRecordRequest {
  foodName: string
  calories: number
  protein: number
  carbohydrates: number
  fat: number
  fiber?: number
  sugar?: number
  sodium?: number
  recordDate: string
}

// 食物相关类型
export interface Food {
  id: number
  name: string
  calories: number
  protein: number
  carbohydrates: number
  fat: number
  fiber: number
  sugar: number
  sodium: number
  category: string
  description?: string
}

export interface FoodSearchRequest {
  keyword: string
  category?: string
  page?: number
  size?: number
}

// 营养目标类型
export interface NutritionGoal {
  id: number
  userId: number
  dailyCalories: number
  dailyProtein: number
  dailyCarbohydrates: number
  dailyFat: number
  dailyFiber: number
  dailySugar: number
  dailySodium: number
  createTime: string
  updateTime: string
}

export interface NutritionGoalRequest {
  dailyCalories: number
  dailyProtein: number
  dailyCarbohydrates: number
  dailyFat: number
  dailyFiber?: number
  dailySugar?: number
  dailySodium?: number
}