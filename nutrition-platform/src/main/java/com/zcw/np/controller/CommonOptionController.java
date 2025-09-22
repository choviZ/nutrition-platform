package com.zcw.np.controller;

import com.zcw.np.common.BaseResponse;
import com.zcw.np.common.ResultUtils;
import com.zcw.np.service.CommonOptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 公共选项接口
 *
 * @author zcw
 */
@RestController
@RequestMapping("/api/common")
@Slf4j
@Api(tags = "公共选项管理")
public class CommonOptionController {

    @Resource
    private CommonOptionService commonOptionService;

    /**
     * 获取活动水平选项
     */
    @GetMapping("/activity-levels")
    @ApiOperation(value = "获取活动水平选项", notes = "获取可选的活动水平列表")
    public BaseResponse<List<Map<String, Object>>> getActivityLevels() {
        List<Map<String, Object>> options = commonOptionService.getActivityLevelOptions();
        return ResultUtils.success(options);
    }

    /**
     * 获取健康目标选项
     */
    @GetMapping("/health-goals")
    @ApiOperation(value = "获取健康目标选项", notes = "获取可选的健康目标列表")
    public BaseResponse<List<Map<String, Object>>> getHealthGoals() {
        List<Map<String, Object>> options = commonOptionService.getHealthGoalOptions();
        return ResultUtils.success(options);
    }
}