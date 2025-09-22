package com.zcw.np.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 饮食记录视图对象
 *
 * @author zcw
 */
@Data
@ApiModel(description = "饮食记录视图对象")
public class DietRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录ID", example = "1")
    private Long recordId;

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;

    @ApiModelProperty(value = "记录日期", example = "2025-09-22")
    private LocalDate recordDate;

    @ApiModelProperty(value = "餐别：1-早餐，2-午餐，3-晚餐，4-加餐", example = "1")
    private Integer mealType;

    @ApiModelProperty(value = "餐别名称", example = "早餐")
    private String mealTypeName;

    @ApiModelProperty(value = "食物名称", example = "白米饭")
    private String foodName;

    @ApiModelProperty(value = "食物分量（g）", example = "150.0")
    private BigDecimal foodAmount;

    @ApiModelProperty(value = "热量（kcal）", example = "174.0")
    private BigDecimal calories;

    @ApiModelProperty(value = "蛋白质（g）", example = "3.9")
    private BigDecimal protein;

    @ApiModelProperty(value = "碳水化合物（g）", example = "38.85")
    private BigDecimal carbohydrate;

    @ApiModelProperty(value = "脂肪（g）", example = "0.45")
    private BigDecimal fat;

    @ApiModelProperty(value = "创建时间", example = "2025-09-22 08:30:00")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", example = "2025-09-22 08:30:00")
    private LocalDateTime updateTime;

    /**
     * 获取餐别名称
     */
    public String getMealTypeName() {
        if (mealType == null) {
            return "";
        }
        switch (mealType) {
            case 1:
                return "早餐";
            case 2:
                return "午餐";
            case 3:
                return "晚餐";
            case 4:
                return "加餐";
            default:
                return "未知";
        }
    }
}