package com.zcw.np.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 食物营养数据视图对象
 *
 * @author zcw
 */
@Data
@ApiModel(description = "食物营养数据视图对象")
public class FoodNutritionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "食物ID", example = "1")
    private Long foodId;

    @ApiModelProperty(value = "食物名称", example = "苹果")
    private String foodName;

    @ApiModelProperty(value = "食物分类", example = "水果")
    private String foodCategory;

    @ApiModelProperty(value = "每100g热量（kcal）", example = "52.0")
    private BigDecimal caloriesPer100g;

    @ApiModelProperty(value = "每100g蛋白质（g）", example = "0.3")
    private BigDecimal proteinPer100g;

    @ApiModelProperty(value = "每100g碳水化合物（g）", example = "13.8")
    private BigDecimal carbohydratePer100g;

    @ApiModelProperty(value = "每100g脂肪（g）", example = "0.2")
    private BigDecimal fatPer100g;

    @ApiModelProperty(value = "每100g膳食纤维（g）", example = "2.4")
    private BigDecimal fiberPer100g;

    @ApiModelProperty(value = "每100g维生素A（μg）", example = "3.0")
    private BigDecimal vitaminAPer100g;

    @ApiModelProperty(value = "每100g维生素C（mg）", example = "4.0")
    private BigDecimal vitaminCPer100g;

    @ApiModelProperty(value = "每100g钙（mg）", example = "6.0")
    private BigDecimal calciumPer100g;

    @ApiModelProperty(value = "每100g铁（mg）", example = "0.1")
    private BigDecimal ironPer100g;

    @ApiModelProperty(value = "创建时间", example = "2024-01-01 12:00:00")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", example = "2024-01-01 12:00:00")
    private Date updateTime;
}