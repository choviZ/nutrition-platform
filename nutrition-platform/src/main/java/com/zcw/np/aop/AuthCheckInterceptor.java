package com.zcw.np.aop;

import com.zcw.np.annotation.AuthCheck;
import com.zcw.np.utils.UserContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

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
        
        // 校验权限（UserContextUtils内部会处理登录检查和权限验证）
        UserContextUtils.requireRole(mustRole);
        
        // 权限校验通过，执行原方法
        return joinPoint.proceed();
    }
}