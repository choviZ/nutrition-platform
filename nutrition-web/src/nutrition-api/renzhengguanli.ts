// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 用户登录 POST /api/auth/login */
export async function loginUsingPost1(body: API.LoginRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLoginResponse_>('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 用户退出登录 POST /api/auth/logout */
export async function logoutUsingPost1(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/auth/logout', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 刷新Token POST /api/auth/refresh */
export async function refreshTokenUsingPost1(options?: { [key: string]: any }) {
  return request<API.BaseResponseString_>('/api/auth/refresh', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 用户注册 POST /api/auth/register */
export async function registerUsingPost1(
  body: API.RegisterRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>('/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 验证Token GET /api/auth/validate */
export async function validateTokenUsingGet1(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/auth/validate', {
    method: 'GET',
    ...(options || {}),
  })
}
