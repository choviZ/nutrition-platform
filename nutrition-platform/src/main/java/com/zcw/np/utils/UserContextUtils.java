package com.zcw.np.utils;

import com.zcw.np.common.ErrorCode;
import com.zcw.np.constant.UserConstant;
import com.zcw.np.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 用户上下文工具类
 * 统一管理用户信息获取和权限校验逻辑
 *
 * @author zcw
 */
@Slf4j
public class UserContextUtils {

    /**
     * 获取当前认证信息
     *
     * @return Authentication 认证信息，如果未登录返回null
     */
    public static Authentication getCurrentAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        return authentication;
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID，如果未登录返回null
     */
    public static Long getCurrentUserId() {
        Authentication authentication = getCurrentAuthentication();
        if (authentication == null) {
            return null;
        }
        return getCurrentUserId(authentication);
    }

    /**
     * 获取当前用户ID（必须登录）
     *
     * @return 用户ID
     * @throws BusinessException 如果未登录
     */
    public static Long getRequiredCurrentUserId() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return userId;
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名，如果未登录返回null
     */
    public static String getCurrentUsername() {
        Authentication authentication = getCurrentAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

    /**
     * 获取当前用户角色
     *
     * @return 用户角色，如果未登录返回null
     */
    public static String getCurrentUserRole() {
        Authentication authentication = getCurrentAuthentication();
        if (authentication == null) {
            return null;
        }
        return getCurrentUserRole(authentication);
    }

    /**
     * 获取当前用户角色（必须登录）
     *
     * @return 用户角色
     * @throws BusinessException 如果未登录
     */
    public static String getRequiredCurrentUserRole() {
        String userRole = getCurrentUserRole();
        if (!StringUtils.hasText(userRole)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return userRole;
    }

    /**
     * 检查当前用户是否已登录
     *
     * @return true-已登录，false-未登录
     */
    public static boolean isLoggedIn() {
        return getCurrentAuthentication() != null;
    }

    /**
     * 检查当前用户是否为管理员
     *
     * @return true-是管理员，false-不是管理员
     */
    public static boolean isAdmin() {
        String userRole = getCurrentUserRole();
        return UserConstant.ADMIN_ROLE.equals(userRole);
    }

    /**
     * 检查当前用户是否为指定用户本人
     *
     * @param targetUserId 目标用户ID
     * @return true-是本人，false-不是本人
     */
    public static boolean isSelf(Long targetUserId) {
        if (targetUserId == null) {
            return false;
        }
        Long currentUserId = getCurrentUserId();
        return targetUserId.equals(currentUserId);
    }

    /**
     * 检查当前用户是否为本人或管理员
     *
     * @param targetUserId 目标用户ID
     * @return true-是本人或管理员，false-都不是
     */
    public static boolean isSelfOrAdmin(Long targetUserId) {
        return isAdmin() || isSelf(targetUserId);
    }

    /**
     * 检查当前用户是否有指定角色
     *
     * @param requiredRole 需要的角色
     * @return true-有权限，false-无权限
     */
    public static boolean hasRole(String requiredRole) {
        if (!StringUtils.hasText(requiredRole)) {
            return true; // 不需要特定角色
        }

        String userRole = getCurrentUserRole();
        
        // 如果用户角色为空，默认为普通用户
        if (!StringUtils.hasText(userRole)) {
            userRole = UserConstant.DEFAULT_ROLE;
        }
        
        // 如果是被封号用户，直接拒绝
        if (UserConstant.BAN_ROLE.equals(userRole)) {
            return false;
        }
        
        // 如果需要管理员权限
        if (UserConstant.ADMIN_ROLE.equals(requiredRole)) {
            return UserConstant.ADMIN_ROLE.equals(userRole);
        }
        
        // 如果需要普通用户权限，管理员和普通用户都可以
        if (UserConstant.DEFAULT_ROLE.equals(requiredRole)) {
            return UserConstant.DEFAULT_ROLE.equals(userRole) || UserConstant.ADMIN_ROLE.equals(userRole);
        }
        
        // 其他情况，角色必须完全匹配
        return requiredRole.equals(userRole);
    }

    /**
     * 要求当前用户必须已登录
     *
     * @throws BusinessException 如果未登录
     */
    public static void requireLogin() {
        if (!isLoggedIn()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
    }

    /**
     * 要求当前用户必须是管理员
     *
     * @throws BusinessException 如果不是管理员
     */
    public static void requireAdmin() {
        requireLogin();
        if (!isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "需要管理员权限");
        }
    }

    /**
     * 要求当前用户必须是本人或管理员
     *
     * @param targetUserId 目标用户ID
     * @throws BusinessException 如果既不是本人也不是管理员
     */
    public static void requireSelfOrAdmin(Long targetUserId) {
        requireLogin();
        if (!isSelfOrAdmin(targetUserId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "只有本人或管理员可以执行此操作");
        }
    }

    /**
     * 要求当前用户必须有指定角色
     *
     * @param requiredRole 需要的角色
     * @throws BusinessException 如果没有权限
     */
    public static void requireRole(String requiredRole) {
        requireLogin();
        if (!hasRole(requiredRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "权限不足，需要" + requiredRole + "权限");
        }
    }

    // ========== 私有辅助方法 ==========

    /**
     * 从认证信息中获取用户ID
     */
    private static Long getCurrentUserId(Authentication authentication) {
        Object details = authentication.getDetails();
        if (details instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> detailsMap = (Map<String, Object>) details;
            return (Long) detailsMap.get("userId");
        }
        return null;
    }

    /**
     * 从认证信息中获取用户角色
     */
    private static String getCurrentUserRole(Authentication authentication) {
        Object details = authentication.getDetails();
        if (details instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> detailsMap = (Map<String, Object>) details;
            return (String) detailsMap.get("userRole");
        }
        return null;
    }
}