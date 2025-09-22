package com.zcw.np.model.dto.dietrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 饮食记录更新请求
 *
 * @author zcw
 */
@Data
@ApiModel(description = "饮食记录更新请求")
public class DietRecordUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录ID", example = "1", required = true)
    @NotNull(message = "记录ID不能为空")
    @Positive(message = "记录ID必须为正数")
    private Long recordId;

    @ApiModelProperty(value = "记录日期", example = "2025-09-22")
    private LocalDate recordDate;

    @ApiModelProperty(value = "餐别：1-早餐，2-午餐，3-晚餐，4-加餐", example = "1")
    @Min(value = 1, message = "餐别值必须在1-4之间")
    @Max(value = 4, message = "餐别值必须在1-4之间")
    private Integer mealType;

    @ApiModelProperty(value = "食物名称", example = "白米饭")
    @Size(max = 100, message = "食物名称长度不能超过100个字符")
    private String foodName;

    @ApiModelProperty(value = "食物分量（g）", example = "150.0")
    @DecimalMin(value = "0.1", message = "食物分量必须大于0")
    @DecimalMax(value = "9999.99", message = "食物分量不能超过9999.99g")
    private BigDecimal foodAmount;

    @ApiModelProperty(value = "热量（kcal）", example = "174.0")
    @DecimalMin(value = "0", message = "热量不能为负数")
    @DecimalMax(value = "99999.99", message = "热量值过大")
    private BigDecimal calories;

    @ApiModelProperty(value = "蛋白质（g）", example = "3.9")
    @DecimalMin(value = "0", message = "蛋白质含量不能为负数")
    @DecimalMax(value = "9999.99", message = "蛋白质含量值过大")
    private BigDecimal protein;

    @ApiModelProperty(value = "碳水化合物（g）", example = "38.85")
    @DecimalMin(value = "0", message = "碳水化合物含量不能为负数")
    @DecimalMax(value = "9999.99", message = "碳水化合物含量值过大")
    private BigDecimal carbohydrate;

    @ApiModelProperty(value = "脂肪（g）", example = "0.45")
    @DecimalMin(value = "0", message = "脂肪含量不能为负数")
    @DecimalMax(value = "9999.99", message = "脂肪含量值过大")
    private BigDecimal fat;
}