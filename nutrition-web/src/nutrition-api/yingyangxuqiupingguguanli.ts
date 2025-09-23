// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 获取营养需求评估详情 根据ID获取营养需求评估记录详情 GET /api/nutrition/${param0} */
export async function getNutritionRequirementByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getNutritionRequirementByIdUsingGET1Params,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseNutritionAssessmentResponse_>(`/api/nutrition/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 营养需求评估 根据用户基本信息计算营养需求 POST /api/nutrition/assess */
export async function assessNutritionRequirementUsingPost1(
  body: API.NutritionAssessmentRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseNutritionAssessmentResponse_>('/api/nutrition/assess', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除营养需求评估记录 删除指定的营养需求评估记录 GET /api/nutrition/del/${param0} */
export async function deleteNutritionRequirementUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteNutritionRequirementUsingGET1Params,
  options?: { [key: string]: any }
) {
  const { id: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/nutrition/del/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取最新营养需求评估 获取当前用户最新的营养需求评估记录 GET /api/nutrition/latest */
export async function getLatestNutritionRequirementUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseNutritionAssessmentResponse_>('/api/nutrition/latest', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 查询营养需求记录 根据条件查询营养需求评估记录 POST /api/nutrition/query */
export async function queryNutritionRequirementsUsingPost1(
  body: API.NutritionQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListNutritionAssessmentResponse_>('/api/nutrition/query', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
