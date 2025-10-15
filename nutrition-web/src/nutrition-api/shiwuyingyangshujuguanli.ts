// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 添加食物营养数据 需要管理员权限 POST /api/food-nutrition/add */
export async function addFoodNutritionUsingPost1(
  body: API.FoodNutritionAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/food-nutrition/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除食物营养数据 需要管理员权限 POST /api/food-nutrition/delete */
export async function deleteFoodNutritionUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/food-nutrition/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 根据ID获取食物营养数据 GET /api/food-nutrition/get */
export async function getFoodNutritionByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getFoodNutritionByIdUsingGET1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseFoodNutritionVO_>('/api/food-nutrition/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 分页查询食物营养数据（GET方式） GET /api/food-nutrition/list/page */
export async function listFoodNutritionByPageGetUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listFoodNutritionByPageGetUsingGET1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageFoodNutritionVO_>('/api/food-nutrition/list/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 根据食物名称模糊查询 GET /api/food-nutrition/search */
export async function searchFoodByNameUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.searchFoodByNameUsingGET1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListFoodNutritionVO_>('/api/food-nutrition/search', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 更新食物营养数据 需要管理员权限 POST /api/food-nutrition/update */
export async function updateFoodNutritionUsingPost1(
  body: API.FoodNutritionUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/food-nutrition/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
