package com.zcw.np.model.dto.dietrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 饮食记录添加请求
 *
 * @author zcw
 */
@Data
@ApiModel(description = "饮食记录添加请求")
public class DietRecordAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "餐别：1-早餐，2-午餐，3-晚餐，4-加餐", example = "1", required = true)
    @NotNull(message = "餐别不能为空")
    @Min(value = 1, message = "餐别值必须在1-4之间")
    @Max(value = 4, message = "餐别值必须在1-4之间")
    private Integer mealType;

    @ApiModelProperty(value = "食物名称", example = "白米饭", required = true)
    @NotBlank(message = "食物名称不能为空")
    @Size(max = 100, message = "食物名称长度不能超过100个字符")
    private String foodName;

    @ApiModelProperty(value = "食物分量（g）", example = "150.0", required = true)
    @NotNull(message = "食物分量不能为空")
    @DecimalMin(value = "0.1", message = "食物分量必须大于0")
    @DecimalMax(value = "9999.99", message = "食物分量不能超过9999.99g")
    private BigDecimal foodAmount;

    @ApiModelProperty(value = "记录时间", example = "2022-01-01 00:00:00", required = true)
    @NotNull(message = "记录时间不能为空")
    private LocalDateTime createTime;
}