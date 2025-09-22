package com.zcw.np.model.dto.nutrition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 营养需求评估请求DTO
 *
 * @author zcw
 */
@Data
@ApiModel(description = "营养需求评估请求")
public class NutritionAssessmentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "年龄", required = true, example = "25")
    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄必须大于0")
    @Max(value = 150, message = "年龄不能超过150")
    private Integer age;

    @ApiModelProperty(value = "性别（0-女，1-男）", required = true, example = "1")
    @NotNull(message = "性别不能为空")
    @Min(value = 0, message = "性别值必须为0或1")
    @Max(value = 1, message = "性别值必须为0或1")
    private Integer gender;

    @ApiModelProperty(value = "身高（cm）", required = true, example = "175.0")
    @NotNull(message = "身高不能为空")
    @DecimalMin(value = "50.0", message = "身高不能小于50cm")
    @DecimalMax(value = "300.0", message = "身高不能超过300cm")
    private BigDecimal height;

    @ApiModelProperty(value = "体重（kg）", required = true, example = "70.0")
    @NotNull(message = "体重不能为空")
    @DecimalMin(value = "10.0", message = "体重不能小于10kg")
    @DecimalMax(value = "500.0", message = "体重不能超过500kg")
    private BigDecimal weight;

    @ApiModelProperty(value = "活动水平（1-久坐，2-轻度，3-中度，4-重度）", required = true, example = "2")
    @NotNull(message = "活动水平不能为空")
    @Min(value = 1, message = "活动水平值必须为1-4")
    @Max(value = 4, message = "活动水平值必须为1-4")
    private Integer activityLevel;

    @ApiModelProperty(value = "健康目标（1-减肥，2-增肌，3-维持健康）", required = true, example = "3")
    @NotNull(message = "健康目标不能为空")
    @Min(value = 1, message = "健康目标值必须为1-3")
    @Max(value = 3, message = "健康目标值必须为1-3")
    private Integer healthGoal;

    @ApiModelProperty(value = "是否保存评估结果", example = "true")
    private Boolean saveResult = true;
}