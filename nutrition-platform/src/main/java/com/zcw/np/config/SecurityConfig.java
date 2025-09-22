package com.zcw.np.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security 配置
 *
 * @author zcw
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护
            .csrf().disable()
            // 禁用框架保护
            .headers().frameOptions().disable()
            .and()
            // 配置授权规则
            .authorizeRequests()
                // 允许访问knife4j相关路径
                .antMatchers("/doc.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs", "/v3/api-docs", "/swagger-ui/**").permitAll()
                // 允许访问静态资源
                .antMatchers("/favicon.ico", "/error").permitAll()
                // 允许访问健康检查接口
                .antMatchers("/health/**").permitAll()
                // 允许访问用户相关接口（临时开放，后续可根据需要调整）
                .antMatchers("/user/**").permitAll()
                // 允许访问所有API接口（开发阶段临时开放）
                .antMatchers("/api/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            .and()
            // 禁用session管理（如果使用JWT的话）
            .sessionManagement().disable()
            // 禁用默认登录页面
            .formLogin().disable()
            // 禁用HTTP Basic认证
            .httpBasic().disable();
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}