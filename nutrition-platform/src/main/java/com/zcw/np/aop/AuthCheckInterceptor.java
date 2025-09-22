package com.zcw.np.aop;

import com.zcw.np.annotation.AuthCheck;
import com.zcw.np.common.ErrorCode;
import com.zcw.np.constant.UserConstant;
import com.zcw.np.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 权限校验 AOP
 *
 * @author zcw
 */
@Aspect
@Component
@Slf4j
public class AuthCheckInterceptor {

    /**
     * 执行拦截
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        
        // 如果不需要权限校验，直接放行
        if (!StringUtils.hasText(mustRole)) {
            return joinPoint.proceed();
        }
        
        // 获取当前用户认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        
        // 获取用户角色
        String userRole = getCurrentUserRole(authentication);
        
        // 校验权限
        if (!hasPermission(userRole, mustRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "权限不足，需要" + mustRole + "权限");
        }
        
        // 权限校验通过，执行原方法
        return joinPoint.proceed();
    }
    
    /**
     * 获取当前用户角色
     */
    private String getCurrentUserRole(Authentication authentication) {
        Object details = authentication.getDetails();
        if (details instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> detailsMap = (Map<String, Object>) details;
            return (String) detailsMap.get("userRole");
        }
        return null;
    }
    
    /**
     * 判断是否有权限
     */
    private boolean hasPermission(String userRole, String mustRole) {
        // 如果用户角色为空，默认为普通用户
        if (!StringUtils.hasText(userRole)) {
            userRole = UserConstant.DEFAULT_ROLE;
        }
        
        // 如果是被封号用户，直接拒绝
        if (UserConstant.BAN_ROLE.equals(userRole)) {
            return false;
        }
        
        // 如果需要管理员权限
        if (UserConstant.ADMIN_ROLE.equals(mustRole)) {
            return UserConstant.ADMIN_ROLE.equals(userRole);
        }
        
        // 如果需要普通用户权限，管理员和普通用户都可以
        if (UserConstant.DEFAULT_ROLE.equals(mustRole)) {
            return UserConstant.DEFAULT_ROLE.equals(userRole) || UserConstant.ADMIN_ROLE.equals(userRole);
        }
        
        // 其他情况，角色必须完全匹配
        return mustRole.equals(userRole);
    }
}