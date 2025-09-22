package com.zcw.np.controller;

import com.zcw.np.annotation.AuthCheck;
import com.zcw.np.common.BaseResponse;
import com.zcw.np.common.ErrorCode;
import com.zcw.np.common.ResultUtils;
import com.zcw.np.constant.UserConstant;
import com.zcw.np.exception.ThrowUtils;
import com.zcw.np.model.dto.health.HealthProfileCreateRequest;
import com.zcw.np.model.dto.health.HealthProfileUpdateRequest;
import com.zcw.np.model.vo.HealthProfileVO;
import com.zcw.np.service.UserHealthProfileService;
import com.zcw.np.utils.UserContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户健康档案接口
 *
 * @author zcw
 */
@RestController
@RequestMapping("/api/health-profile")
@Slf4j
@Api(tags = "用户健康档案管理")
public class UserHealthProfileController {

    @Resource
    private UserHealthProfileService userHealthProfileService;

    /**
     * 创建健康档案
     *
     * @param healthProfileCreateRequest 创建请求
     * @return 健康档案信息
     */
    @PostMapping("/create")
    @ApiOperation(value = "创建健康档案")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<HealthProfileVO> createHealthProfile(@RequestBody @Valid HealthProfileCreateRequest healthProfileCreateRequest) {
        ThrowUtils.throwIf(healthProfileCreateRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getCurrentUserId();
        // 创建健康档案
        HealthProfileVO healthProfileVO = userHealthProfileService.createHealthProfile(healthProfileCreateRequest, currentUserId);
        return ResultUtils.success(healthProfileVO);
    }

    /**
     * 更新健康档案
     *
     * @param healthProfileUpdateRequest 更新请求
     * @return 健康档案信息
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新健康档案")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<HealthProfileVO> updateHealthProfile(@RequestBody @Valid HealthProfileUpdateRequest healthProfileUpdateRequest) {
        ThrowUtils.throwIf(healthProfileUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getCurrentUserId();
        // 更新健康档案
        HealthProfileVO healthProfileVO = userHealthProfileService.updateHealthProfile(healthProfileUpdateRequest, currentUserId);
        return ResultUtils.success(healthProfileVO);
    }

    /**
     * 获取当前用户健康档案
     *
     * @return 健康档案信息
     */
    @GetMapping("/get")
    @ApiOperation(value = "获取当前用户健康档案")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<HealthProfileVO> getMyHealthProfile() {
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getCurrentUserId();
        // 获取健康档案
        HealthProfileVO healthProfileVO = userHealthProfileService.getHealthProfileByUserId(currentUserId);
        return ResultUtils.success(healthProfileVO);
    }

    /**
     * 获取指定用户健康档案（管理员权限）
     *
     * @param userId 用户ID
     * @return 健康档案信息
     */
    @GetMapping("/get/{userId}")
    @ApiOperation(value = "获取指定用户健康档案")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<HealthProfileVO> getHealthProfileByUserId(@PathVariable("userId") Long userId) {
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        // 获取健康档案
        HealthProfileVO healthProfileVO = userHealthProfileService.getHealthProfileByUserId(userId);
        return ResultUtils.success(healthProfileVO);
    }

    /**
     * 删除当前用户健康档案
     *
     * @return 删除结果
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除当前用户健康档案")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> deleteMyHealthProfile() {
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getCurrentUserId();
        // 删除健康档案
        boolean result = userHealthProfileService.deleteHealthProfile(currentUserId);
        return ResultUtils.success(result);
    }

    /**
     * 删除指定用户健康档案（管理员权限）
     *
     * @param userId 用户ID
     * @return 删除结果
     */
    @PostMapping("/delete/{userId}")
    @ApiOperation(value = "删除指定用户健康档案")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteHealthProfileByUserId(@PathVariable("userId") Long userId) {
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        // 删除健康档案
        boolean result = userHealthProfileService.deleteHealthProfile(userId);
        return ResultUtils.success(result);
    }

    /**
     * 检查当前用户是否已有健康档案
     *
     * @return 是否存在健康档案
     */
    @GetMapping("/exists")
    @ApiOperation(value = "检查当前用户是否已有健康档案")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> hasHealthProfile() {
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getCurrentUserId();
        // 检查是否存在健康档案
        boolean exists = userHealthProfileService.hasHealthProfile(currentUserId);
        return ResultUtils.success(exists);
    }


}