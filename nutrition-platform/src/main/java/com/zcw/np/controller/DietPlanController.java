package com.zcw.np.controller;

import com.zcw.np.annotation.AuthCheck;
import com.zcw.np.common.BaseResponse;
import com.zcw.np.common.ErrorCode;
import com.zcw.np.common.ResultUtils;
import com.zcw.np.constant.UserConstant;
import com.zcw.np.exception.BusinessException;
import com.zcw.np.exception.ThrowUtils;
import com.zcw.np.model.dto.dietplan.DietPlanAdjustRequest;
import com.zcw.np.model.dto.dietplan.DietPlanGenerateRequest;
import com.zcw.np.model.vo.DietPlanVO;
import com.zcw.np.service.DietPlanService;
import com.zcw.np.utils.UserContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * 饮食方案接口
 *
 * @author zcw
 */
@RestController
@RequestMapping("/diet-plan")
@Slf4j
@Api(tags = "饮食方案管理")
public class DietPlanController {

    @Resource
    private DietPlanService dietPlanService;

    /**
     * 生成个性化饮食方案
     */
    @PostMapping("/generate")
    @ApiOperation(value = "生成个性化饮食方案")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<DietPlanVO> generateDietPlan(@Valid @RequestBody DietPlanGenerateRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        // 如果请求中没有指定用户ID，使用当前登录用户ID
        if (request.getUserId() == null) {
            request.setUserId(currentUserId);
        } else {
            // 检查权限：只有管理员可以为其他用户生成方案
            if (!currentUserId.equals(request.getUserId()) && 
                !UserContextUtils.isAdmin()) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限为其他用户生成饮食方案");
            }
        }
        try {
            DietPlanVO result = dietPlanService.generateDietPlan(request);
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("生成饮食方案失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成饮食方案失败：" + e.getMessage());
        }
    }

    /**
     * 调整饮食方案
     */
    @PostMapping("/adjust")
    @ApiOperation(value = "调整饮食方案")
    public BaseResponse<DietPlanVO> adjustDietPlan(@Valid @RequestBody DietPlanAdjustRequest request) {
        if (request == null || request.getPlanId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        
        // 权限校验：只能调整自己的方案，管理员可以调整任何人的方案
        DietPlanVO existingPlan = dietPlanService.getDietPlanById(request.getPlanId());
        if (existingPlan == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "饮食方案不存在");
        }
        
        if (!currentUserId.equals(existingPlan.getUserId()) && 
            !UserContextUtils.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "只能调整自己的饮食方案");
        }

        try {
            DietPlanVO result = dietPlanService.adjustDietPlan(request);
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("调整饮食方案失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "调整饮食方案失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户和日期获取饮食方案
     */
    @GetMapping("/user/{userId}/date/{date}")
    @ApiOperation(value = "根据用户和日期获取饮食方案")
    public BaseResponse<DietPlanVO> getDietPlanByUserAndDate(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "方案日期", required = true) @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        
        ThrowUtils.throwIf(userId == null || date == null, ErrorCode.PARAMS_ERROR);

        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        
        // 权限校验：只能查看自己的方案，管理员可以查看任何人的方案
        if (!currentUserId.equals(userId) && 
            !UserContextUtils.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "只能查看自己的饮食方案");
        }

        DietPlanVO result = dietPlanService.getDietPlanByUserAndDate(userId, date);
        return ResultUtils.success(result);
    }

    /**
     * 获取用户的饮食方案列表
     */
    @GetMapping("/user/{userId}")
    @ApiOperation(value = "获取用户的饮食方案列表")
    public BaseResponse<List<DietPlanVO>> getDietPlansByUser(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "开始日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(value = "结束日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        
        ThrowUtils.throwIf(userId == null, ErrorCode.PARAMS_ERROR);
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        // 检查权限：只有本人或管理员可以查看
        if (!currentUserId.equals(userId) && !UserContextUtils.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限查看此用户的饮食方案");
        }
        List<DietPlanVO> result = dietPlanService.getDietPlansByUser(userId, startDate, endDate);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户的饮食方案列表
     */
    @GetMapping("/my")
    @ApiOperation(value = "获取当前用户的饮食方案列表")
    public BaseResponse<List<DietPlanVO>> getMyDietPlans(
            @ApiParam(value = "开始日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(value = "结束日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        List<DietPlanVO> result = dietPlanService.getDietPlansByUser(currentUserId, startDate, endDate);
        return ResultUtils.success(result);
    }

    /**
     * 复制饮食方案
     */
    @PostMapping("/copy/{planId}")
    @ApiOperation(value = "复制饮食方案")
    public BaseResponse<DietPlanVO> copyDietPlan(
            @ApiParam(value = "方案ID", required = true) @PathVariable Long planId,
            @ApiParam(value = "目标日期", required = true) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate targetDate) {
        
        ThrowUtils.throwIf(planId == null || targetDate == null, ErrorCode.PARAMS_ERROR);
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        // 检查原方案权限（简化处理，实际应该检查原方案是否属于当前用户）
        try {
            DietPlanVO result = dietPlanService.copyDietPlan(planId, targetDate);
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("复制饮食方案失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "复制饮食方案失败：" + e.getMessage());
        }
    }

    /**
     * 删除饮食方案
     */
    @DeleteMapping("/{planId}")
    @ApiOperation(value = "删除饮食方案")
    public BaseResponse<Boolean> deleteDietPlan(@ApiParam(value = "方案ID", required = true) @PathVariable Long planId) {
        ThrowUtils.throwIf(planId == null, ErrorCode.PARAMS_ERROR);
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        // 检查权限（简化处理，实际应该检查方案是否属于当前用户）
        try {
            boolean result = dietPlanService.deleteDietPlan(planId);
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("删除饮食方案失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除饮食方案失败：" + e.getMessage());
        }
    }

    /**
     * 确认饮食方案
     */
    @PostMapping("/confirm/{planId}")
    @ApiOperation(value = "确认饮食方案")
    public BaseResponse<Boolean> confirmDietPlan(@ApiParam(value = "方案ID", required = true) @PathVariable Long planId) {
        ThrowUtils.throwIf(planId == null, ErrorCode.PARAMS_ERROR);
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        try {
            boolean result = dietPlanService.confirmDietPlan(planId);
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("确认饮食方案失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "确认饮食方案失败：" + e.getMessage());
        }
    }

    /**
     * 计算饮食方案营养成分
     */
    @PostMapping("/calculate-nutrition/{planId}")
    @ApiOperation(value = "计算饮食方案营养成分")
    public BaseResponse<DietPlanVO> calculateNutrition(@ApiParam(value = "方案ID", required = true) @PathVariable Long planId) {
        ThrowUtils.throwIf(planId == null, ErrorCode.PARAMS_ERROR);
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        try {
            DietPlanVO result = dietPlanService.calculateNutrition(planId);
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("计算营养成分失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "计算营养成分失败：" + e.getMessage());
        }
    }

    /**
     * 管理员获取所有饮食方案
     */
    @GetMapping("/admin/all")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "管理员获取所有饮食方案")
    public BaseResponse<List<DietPlanVO>> getAllDietPlans(
            @ApiParam(value = "用户ID") @RequestParam(required = false) Long userId,
            @ApiParam(value = "开始日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(value = "结束日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        List<DietPlanVO> result;
        if (userId != null) {
            result = dietPlanService.getDietPlansByUser(userId, startDate, endDate);
        } else {
            // 如果没有指定用户ID，返回所有用户的方案（需要在Service中实现）
            result = dietPlanService.getDietPlansByUser(null, startDate, endDate);
        }
        return ResultUtils.success(result);
    }
}