package com.zcw.np.service;

import com.zcw.np.model.dto.auth.LoginRequest;
import com.zcw.np.model.dto.auth.LoginResponse;
import com.zcw.np.model.dto.auth.RegisterRequest;

/**
 * 认证服务接口
 *
 * @author zcw
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * 用户注册
     *
     * @param registerRequest 注册请求
     * @return 用户ID
     */
    Long register(RegisterRequest registerRequest);

    /**
     * 用户退出登录
     *
     * @param token JWT Token
     * @return 是否成功
     */
    Boolean logout(String token);

    /**
     * 验证Token
     *
     * @param token JWT Token
     * @return 是否有效
     */
    Boolean validateToken(String token);

    /**
     * 刷新Token
     *
     * @param token 旧Token
     * @return 新Token
     */
    String refreshToken(String token);
}