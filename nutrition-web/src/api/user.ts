import request from '../request.ts'
import type {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  User,
  ApiResponse
} from './types'

// 用户登录
export const login = (data: LoginRequest): Promise<LoginResponse> => {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

// 用户注册
export const register = (data: RegisterRequest): Promise<User> => {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

// 获取用户信息
export const getUserInfo = (): Promise<User> => {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

// 更新用户信息
export const updateUserInfo = (data: Partial<User>): Promise<User> => {
  return request({
    url: '/user/update',
    method: 'put',
    data
  })
}

// 修改密码
export const changePassword = (data: { oldPassword: string; newPassword: string }): Promise<void> => {
  return request({
    url: '/user/change-password',
    method: 'put',
    data
  })
}

// 用户登出
export const logout = (): Promise<void> => {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}
