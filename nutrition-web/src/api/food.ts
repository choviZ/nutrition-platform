import request from '../request.ts'
import type {
  Food,
  FoodSearchRequest,
  PageResponse
} from './types'

// 食物相关API

// 搜索食物
export const searchFoods = (params: FoodSearchRequest): Promise<PageResponse<Food>> => {
  return request({
    url: '/food/search',
    method: 'get',
    params
  })
}

// 获取食物详情
export const getFoodDetail = (id: number): Promise<Food> => {
  return request({
    url: `/food/${id}`,
    method: 'get'
  })
}

// 获取食物分类列表
export const getFoodCategories = (): Promise<string[]> => {
  return request({
    url: '/food/categories',
    method: 'get'
  })
}

// 获取热门食物
export const getPopularFoods = (limit?: number): Promise<Food[]> => {
  return request({
    url: '/food/popular',
    method: 'get',
    params: { limit }
  })
}

// 获取推荐食物
export const getRecommendedFoods = (limit?: number): Promise<Food[]> => {
  return request({
    url: '/food/recommended',
    method: 'get',
    params: { limit }
  })
}

// 根据营养成分搜索食物
export const searchFoodsByNutrition = (params: {
  minCalories?: number
  maxCalories?: number
  minProtein?: number
  maxProtein?: number
  minCarbohydrates?: number
  maxCarbohydrates?: number
  minFat?: number
  maxFat?: number
  page?: number
  size?: number
}): Promise<PageResponse<Food>> => {
  return request({
    url: '/food/search-by-nutrition',
    method: 'get',
    params
  })
}
