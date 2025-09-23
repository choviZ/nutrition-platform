// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 删除饮食方案 DELETE /api/diet-plan/${param0} */
export async function deleteDietPlanUsingDelete1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteDietPlanUsingDELETE1Params,
  options?: { [key: string]: any }
) {
  const { planId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/diet-plan/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 调整饮食方案 POST /api/diet-plan/adjust */
export async function adjustDietPlanUsingPost1(
  body: API.DietPlanAdjustRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseDietPlanVO_>('/api/diet-plan/adjust', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 管理员获取所有饮食方案 GET /api/diet-plan/admin/all */
export async function getAllDietPlansUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAllDietPlansUsingGET1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListDietPlanVO_>('/api/diet-plan/admin/all', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 计算饮食方案营养成分 POST /api/diet-plan/calculate-nutrition/${param0} */
export async function calculateNutritionUsingPost1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.calculateNutritionUsingPOST1Params,
  options?: { [key: string]: any }
) {
  const { planId: param0, ...queryParams } = params
  return request<API.BaseResponseDietPlanVO_>(`/api/diet-plan/calculate-nutrition/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 确认饮食方案 POST /api/diet-plan/confirm/${param0} */
export async function confirmDietPlanUsingPost1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.confirmDietPlanUsingPOST1Params,
  options?: { [key: string]: any }
) {
  const { planId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/diet-plan/confirm/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 复制饮食方案 POST /api/diet-plan/copy/${param0} */
export async function copyDietPlanUsingPost1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.copyDietPlanUsingPOST1Params,
  options?: { [key: string]: any }
) {
  const { planId: param0, ...queryParams } = params
  return request<API.BaseResponseDietPlanVO_>(`/api/diet-plan/copy/${param0}`, {
    method: 'POST',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  })
}

/** 生成个性化饮食方案 POST /api/diet-plan/generate */
export async function generateDietPlanUsingPost1(
  body: API.DietPlanGenerateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseDietPlanVO_>('/api/diet-plan/generate', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取当前用户的饮食方案列表 GET /api/diet-plan/my */
export async function getMyDietPlansUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getMyDietPlansUsingGET1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListDietPlanVO_>('/api/diet-plan/my', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取用户的饮食方案列表 GET /api/diet-plan/user/${param0} */
export async function getDietPlansByUserUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDietPlansByUserUsingGET1Params,
  options?: { [key: string]: any }
) {
  const { userId: param0, ...queryParams } = params
  return request<API.BaseResponseListDietPlanVO_>(`/api/diet-plan/user/${param0}`, {
    method: 'GET',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  })
}

/** 根据用户和日期获取饮食方案 GET /api/diet-plan/user/${param1}/date/${param0} */
export async function getDietPlanByUserAndDateUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDietPlanByUserAndDateUsingGET1Params,
  options?: { [key: string]: any }
) {
  const { date: param0, userId: param1, ...queryParams } = params
  return request<API.BaseResponseDietPlanVO_>(`/api/diet-plan/user/${param1}/date/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}
