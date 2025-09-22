package com.zcw.np.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类 - 基于Hutool实现
 * 
 * @author zcw
 */
@Slf4j
@Component
public class JwtUtils {

    /**
     * JWT密钥
     */
    @Value("${nutrition.jwt.secret}")
    private String secret;

    /**
     * JWT过期时间（毫秒）
     */
    @Value("${nutrition.jwt.expiration}")
    private Long expiration;

    /**
     * 生成JWT Token
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param userRole 用户角色
     * @return JWT Token
     */
    public String generateToken(Long userId, String username, String userRole) {
        Map<String, Object> payload = new HashMap<>();
        
        // 设置标准声明
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        payload.put(JWTPayload.SUBJECT, username);
        payload.put(JWTPayload.ISSUED_AT, now);
        payload.put(JWTPayload.EXPIRES_AT, expiryDate);
        payload.put(JWTPayload.NOT_BEFORE, now);
        
        // 设置自定义声明
        payload.put("userId", userId);
        payload.put("username", username);
        payload.put("userRole", userRole);
        
        return JWTUtil.createToken(payload, secret.getBytes());
    }

    /**
     * 生成JWT Token（兼容旧版本）
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT Token
     */
    public String generateToken(Long userId, String username) {
        return generateToken(userId, username, "user");
    }

    /**
     * 从Token中获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            return (String) jwt.getPayload(JWTPayload.SUBJECT);
        } catch (Exception e) {
            log.error("从Token中获取用户名失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token JWT Token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            Object userIdObj = jwt.getPayload("userId");
            if (userIdObj instanceof Number) {
                return ((Number) userIdObj).longValue();
            }
            return null;
        } catch (Exception e) {
            log.error("从Token中获取用户ID失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从Token中获取用户角色
     *
     * @param token JWT Token
     * @return 用户角色
     */
    public String getUserRoleFromToken(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            return (String) jwt.getPayload("userRole");
        } catch (Exception e) {
            log.error("从Token中获取用户角色失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从Token中获取过期时间
     *
     * @param token JWT Token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            Object expObj = jwt.getPayload(JWTPayload.EXPIRES_AT);
            if (expObj instanceof Date) {
                return (Date) expObj;
            } else if (expObj instanceof Number) {
                return new Date(((Number) expObj).longValue() * 1000);
            }
            return null;
        } catch (Exception e) {
            log.error("从Token中获取过期时间失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 检查Token是否过期
     *
     * @param token JWT Token
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration != null && expiration.before(new Date());
        } catch (Exception e) {
            log.error("检查Token过期状态失败: {}", e.getMessage());
            return true;
        }
    }

    /**
     * 验证Token
     *
     * @param token JWT Token
     * @param username 用户名
     * @return 是否有效
     */
    public Boolean validateToken(String token, String username) {
        try {
            if (StrUtil.isBlank(token) || StrUtil.isBlank(username)) {
                return false;
            }
            
            // 验证签名
            if (!JWTUtil.verify(token, secret.getBytes())) {
                log.debug("Token签名验证失败");
                return false;
            }
            
            // 验证用户名
            String tokenUsername = getUsernameFromToken(token);
            if (!username.equals(tokenUsername)) {
                log.debug("Token中的用户名不匹配");
                return false;
            }
            
            // 验证是否过期
            if (isTokenExpired(token)) {
                log.debug("Token已过期");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 验证Token是否有效
     *
     * @param token JWT Token
     * @return 是否有效
     */
    public Boolean validateToken(String token) {
        try {
            if (StrUtil.isBlank(token)) {
                log.debug("Token为空");
                return false;
            }
            
            log.debug("开始验证Token: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
            
            // 验证签名
            if (!JWTUtil.verify(token, secret.getBytes())) {
                log.debug("Token签名验证失败");
                return false;
            }
            
            // 验证是否过期
            boolean expired = isTokenExpired(token);
            log.debug("Token解析成功，是否过期: {}", expired);
            
            return !expired;
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 刷新Token
     *
     * @param token 旧Token
     * @return 新Token
     */
    public String refreshToken(String token) {
        try {
            if (!validateToken(token)) {
                throw new RuntimeException("Token无效，无法刷新");
            }
            
            String username = getUsernameFromToken(token);
            Long userId = getUserIdFromToken(token);
            String userRole = getUserRoleFromToken(token);
            
            return generateToken(userId, username, userRole);
        } catch (Exception e) {
            log.error("刷新Token失败: {}", e.getMessage());
            throw new RuntimeException("刷新Token失败: " + e.getMessage());
        }
    }

    /**
     * 解析Token获取所有载荷信息
     *
     * @param token JWT Token
     * @return 载荷信息
     */
    public Map<String, Object> parseToken(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            return jwt.getPayloads();
        } catch (Exception e) {
            log.error("解析Token失败: {}", e.getMessage());
            return new HashMap<>();
        }
    }
}