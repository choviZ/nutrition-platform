package com.zcw.np.model.dto.food;

import com.zcw.np.common.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 食物营养数据查询请求DTO
 *
 * @author zcw
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "食物营养数据查询请求")
public class FoodNutritionQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "食物名称（模糊查询）", example = "苹果")
    private String foodName;

    @ApiModelProperty(value = "食物分类", example = "水果")
    private String foodCategory;

    @ApiModelProperty(value = "最小热量（kcal/100g）", example = "0.0")
    private BigDecimal minCalories;

    @ApiModelProperty(value = "最大热量（kcal/100g）", example = "500.0")
    private BigDecimal maxCalories;

    @ApiModelProperty(value = "最小蛋白质含量（g/100g）", example = "0.0")
    private BigDecimal minProtein;

    @ApiModelProperty(value = "最大蛋白质含量（g/100g）", example = "50.0")
    private BigDecimal maxProtein;

    @ApiModelProperty(value = "最小碳水化合物含量（g/100g）", example = "0.0")
    private BigDecimal minCarbohydrate;

    @ApiModelProperty(value = "最大碳水化合物含量（g/100g）", example = "100.0")
    private BigDecimal maxCarbohydrate;

    @ApiModelProperty(value = "最小脂肪含量（g/100g）", example = "0.0")
    private BigDecimal minFat;

    @ApiModelProperty(value = "最大脂肪含量（g/100g）", example = "50.0")
    private BigDecimal maxFat;

    @ApiModelProperty(value = "排序字段", example = "calories_per_100g")
    private String sortField;

    @ApiModelProperty(value = "排序方向（asc/desc）", example = "asc")
    private String sortOrder;
}