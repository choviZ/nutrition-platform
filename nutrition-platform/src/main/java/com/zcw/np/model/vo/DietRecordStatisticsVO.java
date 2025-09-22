package com.zcw.np.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 饮食记录统计视图对象
 *
 * @author zcw
 */
@Data
@ApiModel(description = "饮食记录统计视图对象")
public class DietRecordStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "统计日期", example = "2025-09-22")
    private LocalDate statisticsDate;

    @ApiModelProperty(value = "总热量（kcal）", example = "2000.0")
    private BigDecimal totalCalories;

    @ApiModelProperty(value = "总蛋白质（g）", example = "80.0")
    private BigDecimal totalProtein;

    @ApiModelProperty(value = "总碳水化合物（g）", example = "250.0")
    private BigDecimal totalCarbohydrate;

    @ApiModelProperty(value = "总脂肪（g）", example = "65.0")
    private BigDecimal totalFat;

    @ApiModelProperty(value = "早餐热量（kcal）", example = "500.0")
    private BigDecimal breakfastCalories;

    @ApiModelProperty(value = "午餐热量（kcal）", example = "700.0")
    private BigDecimal lunchCalories;

    @ApiModelProperty(value = "晚餐热量（kcal）", example = "600.0")
    private BigDecimal dinnerCalories;

    @ApiModelProperty(value = "加餐热量（kcal）", example = "200.0")
    private BigDecimal snackCalories;

    @ApiModelProperty(value = "记录条数", example = "8")
    private Integer recordCount;

    @ApiModelProperty(value = "各餐别记录详情")
    private List<MealStatistics> mealStatistics;

    /**
     * 餐别统计内部类
     */
    @Data
    @ApiModel(description = "餐别统计")
    public static class MealStatistics implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "餐别：1-早餐，2-午餐，3-晚餐，4-加餐", example = "1")
        private Integer mealType;

        @ApiModelProperty(value = "餐别名称", example = "早餐")
        private String mealTypeName;

        @ApiModelProperty(value = "热量（kcal）", example = "500.0")
        private BigDecimal calories;

        @ApiModelProperty(value = "蛋白质（g）", example = "20.0")
        private BigDecimal protein;

        @ApiModelProperty(value = "碳水化合物（g）", example = "60.0")
        private BigDecimal carbohydrate;

        @ApiModelProperty(value = "脂肪（g）", example = "15.0")
        private BigDecimal fat;

        @ApiModelProperty(value = "记录条数", example = "2")
        private Integer recordCount;

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
}