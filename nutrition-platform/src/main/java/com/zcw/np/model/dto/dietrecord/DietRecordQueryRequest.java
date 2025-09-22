package com.zcw.np.model.dto.dietrecord;

import com.zcw.np.common.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 饮食记录查询请求
 *
 * @author zcw
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "饮食记录查询请求")
public class DietRecordQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", example = "1")
    @Positive(message = "用户ID必须为正数")
    private Long userId;

    @ApiModelProperty(value = "开始日期", example = "2025-09-01")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期", example = "2025-09-30")
    private LocalDate endDate;

    @ApiModelProperty(value = "餐别：1-早餐，2-午餐，3-晚餐，4-加餐", example = "1")
    @Min(value = 1, message = "餐别值必须在1-4之间")
    @Max(value = 4, message = "餐别值必须在1-4之间")
    private Integer mealType;

    @ApiModelProperty(value = "食物名称（模糊查询）", example = "米饭")
    private String foodName;
}