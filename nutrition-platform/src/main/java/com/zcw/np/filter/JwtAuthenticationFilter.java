package com.zcw.np.filter;

import com.zcw.np.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * JWT认证过滤器
 *
 * @author zcw
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtils jwtUtils;

    /**
     * 不需要认证的路径
     */
    private static final List<String> SKIP_PATHS = Arrays.asList(
            "/auth/login",
            "/auth/register",
            "/auth/validate",
            "/doc.html",
            "/swagger-ui.html",
            "/swagger-resources",
            "/v2/api-docs",
            "/webjars",
            "/favicon.ico",
            "/actuator/health"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String requestPath = request.getRequestURI();
        
        // 跳过不需要认证的路径
        if (shouldSkipAuthentication(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 从请求头中获取Token
        String token = getTokenFromRequest(request);
        log.debug("请求路径: {}, Token: {}", requestPath, token != null ? "存在" : "不存在");
        
        if (StringUtils.hasText(token)) {
            try {
                // 验证Token
                if (jwtUtils.validateToken(token)) {
                    // 从Token中获取用户信息
                    String username = jwtUtils.getUsernameFromToken(token);
                    Long userId = jwtUtils.getUserIdFromToken(token);
                    String userRole = jwtUtils.getUserRoleFromToken(token);
                    
                    log.debug("Token验证成功，用户: {}, ID: {}, 角色: {}", username, userId, userRole);
                    
                    if (StringUtils.hasText(username) && userId != null) {
                        // 根据用户角色设置权限
                        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_" + (userRole != null ? userRole.toUpperCase() : "USER"))
                        );
                        
                        // 创建认证对象
                        UsernamePasswordAuthenticationToken authentication = 
                                new UsernamePasswordAuthenticationToken(
                                        username, 
                                        null, 
                                        authorities
                                );
                        
                        // 设置用户ID和角色到认证对象的详情中
                        Map<String, Object> details = new HashMap<>();
                        details.put("userId", userId);
                        details.put("userRole", userRole);
                        authentication.setDetails(details);
                        
                        // 设置到安全上下文
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        log.debug("JWT认证成功，用户: {}, ID: {}, 角色: {}", username, userId, userRole);
                    } else {
                        log.warn("Token中缺少必要的用户信息，用户名: {}, 用户ID: {}", username, userId);
                    }
                } else {
                    log.warn("Token验证失败，Token无效或已过期");
                }
            } catch (Exception e) {
                log.error("JWT认证失败: {}", e.getMessage());
                // 清除安全上下文
                SecurityContextHolder.clearContext();
            }
        } else {
            log.debug("请求中未找到Token");
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.debug("Authorization头内容: [{}]", bearerToken);
        
        if (StringUtils.hasText(bearerToken)) {
            String token;
            if (bearerToken.startsWith("Bearer ")) {
                // 标准Bearer格式
                token = bearerToken.substring(7);
            } else {
                // 直接是token（兼容格式）
                token = bearerToken;
            }
            log.debug("提取的Token: [{}]", token);
            return token;
        }
        
        log.debug("Authorization头为空");
        return null;
    }

    /**
     * 判断是否跳过认证
     */
    private boolean shouldSkipAuthentication(String requestPath) {
        return SKIP_PATHS.stream().anyMatch(requestPath::startsWith);
    }
}