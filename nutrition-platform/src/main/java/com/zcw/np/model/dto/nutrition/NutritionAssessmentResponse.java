package com.zcw.np.model.dto.nutrition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 营养需求评估响应DTO
 *
 * @author zcw
 */
@Data
@ApiModel(description = "营养需求评估响应")
public class NutritionAssessmentResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "需求ID")
    private Long requirementId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "评估日期")
    private Date assessmentDate;

    @ApiModelProperty(value = "基础代谢率（BMR）")
    private BigDecimal bmr;

    @ApiModelProperty(value = "每日热量需求（kcal）")
    private BigDecimal dailyCalories;

    @ApiModelProperty(value = "蛋白质需求（g）")
    private BigDecimal proteinRequirement;

    @ApiModelProperty(value = "脂肪需求（g）")
    private BigDecimal fatRequirement;

    @ApiModelProperty(value = "碳水化合物需求（g）")
    private BigDecimal carbohydrateRequirement;

    @ApiModelProperty(value = "纤维需求（g）")
    private BigDecimal fiberRequirement;

    @ApiModelProperty(value = "维生素A需求（μg）")
    private BigDecimal vitaminARequirement;

    @ApiModelProperty(value = "维生素C需求（mg）")
    private BigDecimal vitaminCRequirement;

    @ApiModelProperty(value = "维生素D需求（μg）")
    private BigDecimal vitaminDRequirement;

    @ApiModelProperty(value = "钙需求（mg）")
    private BigDecimal calciumRequirement;

    @ApiModelProperty(value = "铁需求（mg）")
    private BigDecimal ironRequirement;

    @ApiModelProperty(value = "锌需求（mg）")
    private BigDecimal zincRequirement;

    @ApiModelProperty(value = "BMI值")
    private BigDecimal bmi;

    @ApiModelProperty(value = "BMI状态（偏瘦/正常/超重/肥胖）")
    private String bmiStatus;

    @ApiModelProperty(value = "健康建议")
    private String healthAdvice;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}