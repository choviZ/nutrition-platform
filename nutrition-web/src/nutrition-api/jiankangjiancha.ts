// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 健康检查 GET /api/health/check */
export async function healthCheckUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseMapStringObject_>('/api/health/check', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 服务信息 GET /api/health/info */
export async function serviceInfoUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseMapStringObject_>('/api/health/info', {
    method: 'GET',
    ...(options || {}),
  })
}
