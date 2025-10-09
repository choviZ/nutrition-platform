package com.zcw.np.controller;

import com.zcw.np.common.BaseResponse;
import com.zcw.np.common.ResultUtils;
import com.zcw.np.model.dto.nutrition.NutritionAssessmentRequest;
import com.zcw.np.model.dto.nutrition.NutritionAssessmentResponse;
import com.zcw.np.model.dto.nutrition.NutritionQueryRequest;
import com.zcw.np.model.entity.NutritionRequirement;
import com.zcw.np.service.NutritionRequirementService;
import com.zcw.np.exception.ThrowUtils;
import com.zcw.np.common.ErrorCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 营养需求评估控制器
 *
 * @author zcw
 */
@RestController
@RequestMapping("/nutrition")
@Slf4j
@Api(tags = "营养需求评估管理")
public class NutritionRequirementController {

    @Resource
    private NutritionRequirementService nutritionRequirementService;

    /**
     * 营养需求评估
     */
    @PostMapping("/assess")
    @ApiOperation(value = "营养需求评估", notes = "根据用户基本信息计算营养需求")
    public BaseResponse<NutritionAssessmentResponse> assessNutritionRequirement(@Valid @RequestBody NutritionAssessmentRequest request) {
        
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        
        try {
            NutritionAssessmentResponse response = nutritionRequirementService.assessNutritionRequirement(request);
            return ResultUtils.success(response);
        } catch (Exception e) {
            log.error("营养需求评估失败", e);
            throw new RuntimeException("营养需求评估失败：" + e.getMessage());
        }
    }

    /**
     * 查询营养需求记录
     */
    @PostMapping("/query")
    @ApiOperation(value = "查询营养需求记录", notes = "根据条件查询营养需求评估记录")
    public BaseResponse<List<NutritionAssessmentResponse>> queryNutritionRequirements(@RequestBody NutritionQueryRequest request) {
        if (request == null) {
            request = new NutritionQueryRequest();
        }
        
        try {
            List<NutritionAssessmentResponse> responses = nutritionRequirementService.queryNutritionRequirements(request);
            return ResultUtils.success(responses);
        } catch (Exception e) {
            log.error("查询营养需求记录失败", e);
            throw new RuntimeException("查询营养需求记录失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前用户的最新营养需求评估
     */
    @GetMapping("/latest")
    @ApiOperation(value = "获取最新营养需求评估", notes = "获取当前用户最新的营养需求评估记录")
    public BaseResponse<NutritionAssessmentResponse> getLatestNutritionRequirement() {
        try {
            NutritionQueryRequest request = new NutritionQueryRequest();
            request.setPageSize(1);
            request.setCurrent(1);
            
            List<NutritionAssessmentResponse> responses = nutritionRequirementService.queryNutritionRequirements(request);
            
            if (responses.isEmpty()) {
                return ResultUtils.success(null);
            }
            
            return ResultUtils.success(responses.get(0));
        } catch (Exception e) {
            log.error("获取最新营养需求评估失败", e);
            throw new RuntimeException("获取最新营养需求评估失败：" + e.getMessage());
        }
    }

    /**
     * 删除营养需求评估记录
     */
    @GetMapping("/del/{id}")
    @ApiOperation(value = "删除营养需求评估记录", notes = "删除指定的营养需求评估记录")
    public BaseResponse<Boolean> deleteNutritionRequirement(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "记录ID不能为空");
        try {
            boolean result = nutritionRequirementService.removeById(id);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除失败，记录不存在");
            
            return ResultUtils.success(true);
        } catch (Exception e) {
            log.error("删除营养需求评估记录失败", e);
            throw new RuntimeException("删除营养需求评估记录失败：" + e.getMessage());
        }
    }

    /**
     * 获取营养需求评估记录详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取营养需求评估详情", notes = "根据ID获取营养需求评估记录详情")
    public BaseResponse<NutritionAssessmentResponse> getNutritionRequirementById(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "记录ID不能为空");
        try {
            NutritionRequirement requirement = nutritionRequirementService.getById(id);
            ThrowUtils.throwIf(requirement == null, ErrorCode.NOT_FOUND_ERROR, "记录不存在");
            
            NutritionAssessmentResponse response = new NutritionAssessmentResponse();
            // 手动设置属性，解决字段命名不一致问题
            response.setRequirementId(requirement.getRequirementId());
            response.setUserId(requirement.getUserId());
            response.setAssessmentDate(requirement.getAssessmentDate());
            response.setDailyCalories(requirement.getDailyCalories());
            response.setProteinRequirement(requirement.getProtein());
            response.setFatRequirement(requirement.getFat());
            response.setCarbohydrateRequirement(requirement.getCarbohydrate());
            response.setFiberRequirement(requirement.getFiber());
            response.setVitaminARequirement(requirement.getVitaminA());
            response.setVitaminCRequirement(requirement.getVitaminC());
            response.setCalciumRequirement(requirement.getCalcium());
            response.setIronRequirement(requirement.getIron());
            response.setCreateTime(requirement.getCreateTime());
            // 设置BMI和BMI状态
            response.setBmi(requirement.getBmi());
            // 将整数类型的BMI状态转换为字符串类型
            String bmiStatus = "正常"; // 默认值
            if (requirement.getBmiStatus() != null) {
                switch (requirement.getBmiStatus()) {
                    case 0:
                        bmiStatus = "偏瘦";
                        break;
                    case 1:
                        bmiStatus = "正常";
                        break;
                    case 2:
                        bmiStatus = "超重";
                        break;
                    case 3:
                        bmiStatus = "肥胖";
                        break;
                    default:
                        bmiStatus = "正常";
                }
            }
            response.setBmiStatus(bmiStatus);
            return ResultUtils.success(response);
        } catch (Exception e) {
            log.error("获取营养需求评估详情失败", e);
            throw new RuntimeException("获取营养需求评估详情失败：" + e.getMessage());
        }
    }


}