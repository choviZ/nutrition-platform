package com.zcw.np.model.dto.dietplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.List;

/**
 * 饮食方案调整请求DTO
 *
 * @author zcw
 */
@Data
@ApiModel(description = "饮食方案调整请求")
public class DietPlanAdjustRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "方案ID", example = "1", required = true)
    @NotNull(message = "方案ID不能为空")
    @Positive(message = "方案ID必须为正数")
    private Long planId;

    @ApiModelProperty(value = "调整类型（1-替换食物，2-调整分量，3-添加食物，4-删除食物）", example = "1", required = true)
    @NotNull(message = "调整类型不能为空")
    private Integer adjustType;

    @ApiModelProperty(value = "餐次类型（1-早餐，2-午餐，3-晚餐，4-加餐）", example = "1", required = true)
    @NotNull(message = "餐次类型不能为空")
    private Integer mealType;

    @ApiModelProperty(value = "原食物ID（替换或删除时需要）", example = "1")
    private Long originalFoodId;

    @ApiModelProperty(value = "新食物ID（替换或添加时需要）", example = "2")
    private Long newFoodId;

    @ApiModelProperty(value = "原食物分量（g）", example = "100.0")
    private Double originalAmount;

    @ApiModelProperty(value = "新食物分量（g）", example = "150.0")
    private Double newAmount;

    @ApiModelProperty(value = "调整原因", example = "用户不喜欢这种食物")
    private String adjustReason;

    @ApiModelProperty(value = "批量调整项目")
    private List<AdjustItem> adjustItems;

    /**
     * 调整项目内部类
     */
    @Data
    @ApiModel(description = "调整项目")
    public static class AdjustItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "餐次类型（1-早餐，2-午餐，3-晚餐，4-加餐）", example = "1")
        private Integer mealType;

        @ApiModelProperty(value = "调整类型（1-替换食物，2-调整分量，3-添加食物，4-删除食物）", example = "1")
        private Integer adjustType;

        @ApiModelProperty(value = "原食物ID", example = "1")
        private Long originalFoodId;

        @ApiModelProperty(value = "新食物ID", example = "2")
        private Long newFoodId;

        @ApiModelProperty(value = "原食物分量（g）", example = "100.0")
        private Double originalAmount;

        @ApiModelProperty(value = "新食物分量（g）", example = "150.0")
        private Double newAmount;

        @ApiModelProperty(value = "调整原因", example = "用户偏好")
        private String adjustReason;
    }

    @ApiModelProperty(value = "是否自动保存调整结果", example = "true")
    private Boolean autoSave = true;

    @ApiModelProperty(value = "是否重新计算营养成分", example = "true")
    private Boolean recalculateNutrition = true;
}