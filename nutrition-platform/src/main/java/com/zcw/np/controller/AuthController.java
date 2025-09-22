package com.zcw.np.controller;

import com.zcw.np.common.BaseResponse;
import com.zcw.np.common.ErrorCode;
import com.zcw.np.common.ResultUtils;
import com.zcw.np.exception.BusinessException;
import com.zcw.np.model.dto.auth.LoginRequest;
import com.zcw.np.model.dto.auth.LoginResponse;
import com.zcw.np.model.dto.auth.RegisterRequest;
import com.zcw.np.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 认证控制器
 *
 * @author zcw
 */
@Api(tags = "认证管理")
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Resource
    private AuthService authService;

    /**
     * 用户登录
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        if (loginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResultUtils.success(loginResponse);
    }

    /**
     * 用户注册
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Long userId = authService.register(registerRequest);
        return ResultUtils.success(userId);
    }

    /**
     * 用户退出登录
     */
    @ApiOperation(value = "用户退出登录")
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (!StringUtils.hasText(token)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Token不能为空");
        }
        
        Boolean result = authService.logout(token);
        return ResultUtils.success(result);
    }

    /**
     * 验证Token
     */
    @ApiOperation(value = "验证Token")
    @GetMapping("/validate")
    public BaseResponse<Boolean> validateToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        log.info("收到Token验证请求，Token: {}", token != null ? token.substring(0, Math.min(token.length(), 20)) + "..." : "null");
        
        if (!StringUtils.hasText(token)) {
            log.warn("Token为空，返回false");
            return ResultUtils.success(false);
        }
        
        Boolean isValid = authService.validateToken(token);
        log.info("Token验证结果: {}", isValid);
        return ResultUtils.success(isValid);
    }

    /**
     * 刷新Token
     */
    @ApiOperation(value = "刷新Token")
    @PostMapping("/refresh")
    public BaseResponse<String> refreshToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (!StringUtils.hasText(token)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Token不能为空");
        }
        
        String newToken = authService.refreshToken(token);
        return ResultUtils.success(newToken);
    }

    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.info("Authorization头内容: [{}]", bearerToken);
        
        if (StringUtils.hasText(bearerToken)) {
            String token;
            if (bearerToken.startsWith("Bearer ")) {
                // 标准Bearer格式
                token = bearerToken.substring(7);
            } else {
                // 直接是token（兼容格式）
                token = bearerToken;
            }
            log.info("提取的Token: [{}]", token);
            return token;
        }
        
        log.warn("Authorization头为空");
        return null;
    }
}