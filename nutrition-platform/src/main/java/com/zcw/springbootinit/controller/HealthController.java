package com.zcw.springbootinit.controller;

import com.zcw.springbootinit.common.BaseResponse;
import com.zcw.springbootinit.common.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 *
 * @author zcw
 */
@Api(tags = "健康检查")
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/check")
    @ApiOperation(value = "健康检查")
    public BaseResponse<Map<String, Object>> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "营养搭配服务平台");
        result.put("version", "1.0.0");
        result.put("timestamp", LocalDateTime.now());
        result.put("message", "服务运行正常");
        
        return ResultUtils.success(result);
    }

    @GetMapping("/info")
    @ApiOperation(value = "服务信息")
    public BaseResponse<Map<String, Object>> serviceInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("serviceName", "营养搭配服务平台");
        result.put("description", "为用户提供个性化、科学的营养搭配方案");
        result.put("features", new String[]{
            "用户信息管理",
            "营养需求评估", 
            "饮食方案制定",
            "饮食记录管理",
            "数据分析"
        });
        result.put("author", "zcw");
        
        return ResultUtils.success(result);
    }
}