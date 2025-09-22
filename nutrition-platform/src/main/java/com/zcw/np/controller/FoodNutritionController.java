package com.zcw.np.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcw.np.annotation.AuthCheck;
import com.zcw.np.common.BaseResponse;
import com.zcw.np.common.DeleteRequest;
import com.zcw.np.common.ErrorCode;
import com.zcw.np.common.ResultUtils;
import com.zcw.np.constant.UserConstant;
import com.zcw.np.exception.BusinessException;
import com.zcw.np.exception.ThrowUtils;
import com.zcw.np.model.dto.food.FoodNutritionAddRequest;
import com.zcw.np.model.dto.food.FoodNutritionQueryRequest;
import com.zcw.np.model.dto.food.FoodNutritionUpdateRequest;
import com.zcw.np.model.vo.FoodNutritionVO;
import com.zcw.np.service.FoodNutritionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 食物营养数据接口
 *
 * @author zcw
 */
@RestController
@RequestMapping("/food-nutrition")
@Slf4j
@Api(tags = "食物营养数据管理")
public class FoodNutritionController {

    @Resource
    private FoodNutritionService foodNutritionService;

    /**
     * 创建食物营养数据（管理员）
     *
     * @param foodNutritionAddRequest 创建请求
     * @return 食物ID
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "添加食物营养数据", notes = "需要管理员权限")
    public BaseResponse<Long> addFoodNutrition(@RequestBody @Valid FoodNutritionAddRequest foodNutritionAddRequest) {
        ThrowUtils.throwIf(foodNutritionAddRequest == null, ErrorCode.PARAMS_ERROR);
        
        long foodId = foodNutritionService.addFoodNutrition(foodNutritionAddRequest);
        return ResultUtils.success(foodId);
    }

    /**
     * 删除食物营养数据（管理员）
     *
     * @param deleteRequest 删除请求
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "删除食物营养数据", notes = "需要管理员权限")
    public BaseResponse<Boolean> deleteFoodNutrition(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        long id = deleteRequest.getId();
        boolean result = foodNutritionService.deleteFoodNutrition(id);
        return ResultUtils.success(result);
    }

    /**
     * 更新食物营养数据（管理员）
     *
     * @param foodNutritionUpdateRequest 更新请求
     * @return 是否更新成功
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "更新食物营养数据", notes = "需要管理员权限")
    public BaseResponse<Boolean> updateFoodNutrition(@RequestBody @Valid FoodNutritionUpdateRequest foodNutritionUpdateRequest) {
        ThrowUtils.throwIf(foodNutritionUpdateRequest == null || foodNutritionUpdateRequest.getFoodId() == null, 
                ErrorCode.PARAMS_ERROR);
        
        boolean result = foodNutritionService.updateFoodNutrition(foodNutritionUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 根据ID获取食物营养数据
     *
     * @param id 食物ID
     * @return 食物营养数据
     */
    @GetMapping("/get")
    @ApiOperation(value = "根据ID获取食物营养数据")
    public BaseResponse<FoodNutritionVO> getFoodNutritionById(@RequestParam("id") long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        
        FoodNutritionVO foodNutritionVO = foodNutritionService.getFoodNutritionById(id);
        return ResultUtils.success(foodNutritionVO);
    }

    /**
     * 分页获取食物营养数据列表（GET方式，便于前端调用）
     *
     * @param foodNutritionQueryRequest 查询请求
     * @return 分页结果
     */
    @GetMapping("/list/page")
    @ApiOperation(value = "分页查询食物营养数据（GET方式）")
    public BaseResponse<Page<FoodNutritionVO>> listFoodNutritionByPageGet(FoodNutritionQueryRequest foodNutritionQueryRequest) {
        if (foodNutritionQueryRequest == null) {
            foodNutritionQueryRequest = new FoodNutritionQueryRequest();
        }
        Page<FoodNutritionVO> foodNutritionVOPage = foodNutritionService.listFoodNutritionByPage(foodNutritionQueryRequest);
        return ResultUtils.success(foodNutritionVOPage);
    }
}