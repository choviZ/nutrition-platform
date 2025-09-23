// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 获取活动水平选项 获取可选的活动水平列表 GET /api/api/common/activity-levels */
export async function getActivityLevelsUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseListMapStringObject_>('/api/api/common/activity-levels', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 获取健康目标选项 获取可选的健康目标列表 GET /api/api/common/health-goals */
export async function getHealthGoalsUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseListMapStringObject_>('/api/api/common/health-goals', {
    method: 'GET',
    ...(options || {}),
  })
}
