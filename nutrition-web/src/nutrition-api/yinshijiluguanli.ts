// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 删除饮食记录 DELETE /api/diet-record/${param0} */
export async function deleteDietRecordUsingDelete1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteDietRecordUsingDELETE1Params,
  options?: { [key: string]: any }
) {
  const { recordId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean_>(`/api/diet-record/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 添加饮食记录 POST /api/diet-record/add */
export async function addDietRecordUsingPost1(
  body: API.DietRecordAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/diet-record/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 根据ID获取饮食记录 GET /api/diet-record/get/${param0} */
export async function getDietRecordByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDietRecordByIdUsingGET1Params,
  options?: { [key: string]: any }
) {
  const { recordId: param0, ...queryParams } = params
  return request<API.BaseResponseDietRecordVO_>(`/api/diet-record/get/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取指定日期的饮食记录 GET /api/diet-record/list/date */
export async function getDietRecordsByDateUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDietRecordsByDateUsingGET1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListDietRecordVO_>('/api/diet-record/list/date', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 分页查询饮食记录（GET方式） GET /api/diet-record/list/page */
export async function queryDietRecordsGetUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.queryDietRecordsGetUsingGET1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseIPageDietRecordVO_>('/api/diet-record/list/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取饮食记录统计报表 GET /api/diet-record/statistics */
export async function getDietRecordStatisticsUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDietRecordStatisticsUsingGET1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListDietRecordStatisticsVO_>('/api/diet-record/statistics', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取本月饮食记录统计 GET /api/diet-record/statistics/month */
export async function getMonthlyStatisticsUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseListDietRecordStatisticsVO_>('/api/diet-record/statistics/month', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 获取本周饮食记录统计 GET /api/diet-record/statistics/week */
export async function getWeeklyStatisticsUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseListDietRecordStatisticsVO_>('/api/diet-record/statistics/week', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 获取今日饮食记录 GET /api/diet-record/today */
export async function getTodayDietRecordsUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseListDietRecordVO_>('/api/diet-record/today', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 更新饮食记录 POST /api/diet-record/update */
export async function updateDietRecordUsingPost1(
  body: API.DietRecordUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/diet-record/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
