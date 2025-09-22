package com.zcw.np.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcw.np.common.ErrorCode;
import com.zcw.np.exception.BusinessException;
import com.zcw.np.model.dto.auth.LoginRequest;
import com.zcw.np.model.dto.auth.LoginResponse;
import com.zcw.np.model.dto.auth.RegisterRequest;
import com.zcw.np.model.entity.User;
import com.zcw.np.service.AuthService;
import com.zcw.np.service.UserService;
import com.zcw.np.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 认证服务实现类
 *
 * @author zcw
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserService userService;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Value("${nutrition.jwt.expiration}")
    private Long jwtExpiration;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // 参数校验
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码不能为空");
        }

        // 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userService.getOne(queryWrapper);

        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码错误");
        }

        // 检查用户状态
        if (user.getIsDelete() != null && user.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "用户已被删除");
        }

        // 生成JWT Token
        String token = jwtUtils.generateToken(user.getUserId(), user.getUsername());

        // 构建登录响应
        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userRole(user.getUserRole())
                .expiresIn(jwtExpiration)
                .build();
    }

    @Override
    public Long register(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String confirmPassword = registerRequest.getConfirmPassword();

        // 参数校验
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码不能为空");
        }

        if (!password.equals(confirmPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }

        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        long count = userService.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }

        // 检查邮箱是否已存在
        if (StringUtils.hasText(registerRequest.getEmail())) {
            QueryWrapper<User> emailQueryWrapper = new QueryWrapper<>();
            emailQueryWrapper.eq("email", registerRequest.getEmail());
            long emailCount = userService.count(emailQueryWrapper);
            if (emailCount > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱已被注册");
            }
        }

        // 检查手机号是否已存在
        if (StringUtils.hasText(registerRequest.getPhone())) {
            QueryWrapper<User> phoneQueryWrapper = new QueryWrapper<>();
            phoneQueryWrapper.eq("phone", registerRequest.getPhone());
            long phoneCount = userService.count(phoneQueryWrapper);
            if (phoneCount > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号已被注册");
            }
        }

        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setRealName(StringUtils.hasText(registerRequest.getNickname()) ? 
                registerRequest.getNickname() : username);
        user.setUserRole("user"); // 默认角色为普通用户

        boolean result = userService.save(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
        }

        return user.getUserId();
    }

    @Override
    public Boolean logout(String token) {
        // JWT是无状态的，这里可以实现token黑名单机制
        // 简单实现：验证token是否有效即可
        if (!StringUtils.hasText(token)) {
            return false;
        }

        try {
            return jwtUtils.validateToken(token);
        } catch (Exception e) {
            log.error("退出登录验证token失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        try {
            return jwtUtils.validateToken(token);
        } catch (Exception e) {
            log.error("验证token失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String refreshToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Token不能为空");
        }

        try {
            // 验证旧token
            if (!jwtUtils.validateToken(token)) {
                throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "Token无效");
            }

            // 从旧token中获取用户信息
            String username = jwtUtils.getUsernameFromToken(token);
            Long userId = jwtUtils.getUserIdFromToken(token);

            // 生成新token
            return jwtUtils.generateToken(userId, username);
        } catch (Exception e) {
            log.error("刷新token失败: {}", e.getMessage());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "刷新token失败");
        }
    }
}