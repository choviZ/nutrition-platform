package com.zcw.np.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

/**
 * 饮食方案视图对象VO
 *
 * @author zcw
 */
@Data
@ApiModel(description = "饮食方案视图对象")
public class DietPlanVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "方案ID", example = "1")
    private Long planId;

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;

    @ApiModelProperty(value = "方案日期", example = "2024-01-15")
    private LocalDate planDate;

    @ApiModelProperty(value = "方案名称", example = "减脂饮食方案")
    private String planName;

    @ApiModelProperty(value = "早餐内容")
    private List<MealItem> breakfast;

    @ApiModelProperty(value = "午餐内容")
    private List<MealItem> lunch;

    @ApiModelProperty(value = "晚餐内容")
    private List<MealItem> dinner;

    @ApiModelProperty(value = "加餐内容")
    private List<MealItem> snacks;

    @ApiModelProperty(value = "总热量（kcal）", example = "2000.0")
    private BigDecimal totalCalories;

    @ApiModelProperty(value = "总蛋白质（g）", example = "120.0")
    private BigDecimal totalProtein;

    @ApiModelProperty(value = "总碳水化合物（g）", example = "250.0")
    private BigDecimal totalCarbohydrate;

    @ApiModelProperty(value = "总脂肪（g）", example = "80.0")
    private BigDecimal totalFat;

    @ApiModelProperty(value = "总膳食纤维（g）", example = "30.0")
    private BigDecimal totalFiber;

    @ApiModelProperty(value = "方案状态（1-草稿，2-已确认，3-已执行）", example = "2")
    private Integer status;

    @ApiModelProperty(value = "营养达标情况")
    private NutritionComplianceVO nutritionCompliance;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 餐次项目内部类
     */
    @Data
    @ApiModel(description = "餐次项目")
    public static class MealItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "食物ID", example = "1")
        private Long foodId;

        @ApiModelProperty(value = "食物名称", example = "鸡胸肉")
        private String foodName;

        @ApiModelProperty(value = "食物分类", example = "肉类")
        private String foodCategory;

        @ApiModelProperty(value = "食物分量（g）", example = "150.0")
        private BigDecimal amount;

        @ApiModelProperty(value = "热量（kcal）", example = "165.0")
        private BigDecimal calories;

        @ApiModelProperty(value = "蛋白质（g）", example = "31.0")
        private BigDecimal protein;

        @ApiModelProperty(value = "碳水化合物（g）", example = "0.0")
        private BigDecimal carbohydrate;

        @ApiModelProperty(value = "脂肪（g）", example = "3.6")
        private BigDecimal fat;

        @ApiModelProperty(value = "膳食纤维（g）", example = "0.0")
        private BigDecimal fiber;

        @ApiModelProperty(value = "烹饪方式", example = "蒸")
        private String cookingMethod;

        @ApiModelProperty(value = "备注", example = "去皮")
        private String remark;
    }

    /**
     * 营养达标情况内部类
     */
    @Data
    @ApiModel(description = "营养达标情况")
    public static class NutritionComplianceVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "热量达标率（%）", example = "95.5")
        private BigDecimal caloriesComplianceRate;

        @ApiModelProperty(value = "蛋白质达标率（%）", example = "102.3")
        private BigDecimal proteinComplianceRate;

        @ApiModelProperty(value = "碳水化合物达标率（%）", example = "88.7")
        private BigDecimal carbohydrateComplianceRate;

        @ApiModelProperty(value = "脂肪达标率（%）", example = "110.2")
        private BigDecimal fatComplianceRate;

        @ApiModelProperty(value = "膳食纤维达标率（%）", example = "75.8")
        private BigDecimal fiberComplianceRate;

        @ApiModelProperty(value = "整体营养评分（0-100）", example = "85.5")
        private BigDecimal overallScore;

        @ApiModelProperty(value = "营养建议")
        private List<String> nutritionAdvice;
    }
}