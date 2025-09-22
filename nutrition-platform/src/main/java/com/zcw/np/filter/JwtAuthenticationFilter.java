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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        
        if (StringUtils.hasText(token)) {
            try {
                // 验证Token
                if (jwtUtils.validateToken(token)) {
                    // 从Token中获取用户信息
                    String username = jwtUtils.getUsernameFromToken(token);
                    Long userId = jwtUtils.getUserIdFromToken(token);
                    
                    if (StringUtils.hasText(username) && userId != null) {
                        // 创建认证对象
                        UsernamePasswordAuthenticationToken authentication = 
                                new UsernamePasswordAuthenticationToken(
                                        username, 
                                        null, 
                                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                                );
                        
                        // 设置用户ID到认证对象的详情中
                        authentication.setDetails(userId);
                        
                        // 设置到安全上下文
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        log.debug("JWT认证成功，用户: {}, ID: {}", username, userId);
                    }
                }
            } catch (Exception e) {
                log.error("JWT认证失败: {}", e.getMessage());
                // 清除安全上下文
                SecurityContextHolder.clearContext();
            }
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 判断是否跳过认证
     */
    private boolean shouldSkipAuthentication(String requestPath) {
        return SKIP_PATHS.stream().anyMatch(requestPath::startsWith);
    }
}