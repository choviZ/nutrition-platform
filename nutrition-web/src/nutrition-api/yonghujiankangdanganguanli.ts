// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 创建健康档案 POST /api/health-profile/create */
export async function createHealthProfileUsingPost1(
  body: API.HealthProfileCreateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseHealthProfileVO_>('/api/health-profile/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除当前用户健康档案 POST /api/health-profile/delete */
export async function deleteMyHealthProfileUsingPost1(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/health-profile/delete', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 删除指定用户健康档案 POST /api/health-profile/delete/${param0} */
export async function deleteHealthProfileByUserIdUsingPost1(
  // 叠加生成的参数型注释
  params: API.deleteHealthProfileByUserIdUsingPOST1Params,
  options?: { [key: string]: any }
) {
  const { userId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/health-profile/delete/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 检查当前用户是否已有健康档案 GET /api/health-profile/exists */
export async function hasHealthProfileUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/health-profile/exists', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 获取当前用户健康档案 GET /api/health-profile/get */
export async function getMyHealthProfileUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseHealthProfileVO_>('/api/health-profile/get', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 获取指定用户健康档案 GET /api/health-profile/get/${param0} */
export async function getHealthProfileByUserIdUsingGet1(
  // 叠加生成的参数型注释
  params: API.getHealthProfileByUserIdUsingGET1Params,
  options?: { [key: string]: any }
) {
  const { userId: param0, ...queryParams } = params
  return request<API.BaseResponseHealthProfileVO_>(`/api/health-profile/get/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 更新健康档案 POST /api/health-profile/update */
export async function updateHealthProfileUsingPost1(
  body: API.HealthProfileUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseHealthProfileVO_>('/api/health-profile/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
