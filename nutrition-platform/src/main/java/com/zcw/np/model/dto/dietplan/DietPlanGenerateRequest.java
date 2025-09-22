package com.zcw.np.model.dto.dietplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 饮食方案生成请求DTO
 *
 * @author zcw
 */
@Data
@ApiModel(description = "饮食方案生成请求")
public class DietPlanGenerateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", example = "1", required = true)
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;

    @ApiModelProperty(value = "方案日期", example = "2024-01-15", required = true)
    @NotNull(message = "方案日期不能为空")
    private LocalDate planDate;

    @ApiModelProperty(value = "方案名称", example = "减脂饮食方案")
    private String planName;

    @ApiModelProperty(value = "营养需求ID（基于营养评估结果）", example = "1", required = true)
    @NotNull(message = "营养需求ID不能为空")
    @Positive(message = "营养需求ID必须为正数")
    private Long requirementId;

    @ApiModelProperty(value = "饮食偏好（素食、无乳糖等）", example = "[\"素食\", \"无乳糖\"]")
    private List<String> dietaryPreferences;

    @ApiModelProperty(value = "忌口食物", example = "[\"花生\", \"海鲜\"]")
    private List<String> forbiddenFoods;

    @ApiModelProperty(value = "餐次分配比例（早餐:午餐:晚餐:加餐）", example = "0.25:0.35:0.30:0.10")
    private String mealRatio;

    @ApiModelProperty(value = "是否包含加餐", example = "true")
    private Boolean includeSnacks = true;

    @ApiModelProperty(value = "食物种类偏好（1-清淡，2-适中，3-重口味）", example = "2")
    private Integer tastePreference = 2;

    @ApiModelProperty(value = "烹饪方式偏好", example = "[\"蒸\", \"煮\", \"炒\"]")
    private List<String> cookingMethods;

    @ApiModelProperty(value = "是否自动保存方案", example = "true")
    private Boolean autoSave = true;
}