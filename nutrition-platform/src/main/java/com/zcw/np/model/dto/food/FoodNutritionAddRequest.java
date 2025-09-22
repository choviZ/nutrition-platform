package com.zcw.np.model.dto.food;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 食物营养数据添加请求DTO
 *
 * @author zcw
 */
@Data
@ApiModel(description = "食物营养数据添加请求")
public class FoodNutritionAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "食物名称", required = true, example = "苹果")
    @NotBlank(message = "食物名称不能为空")
    @Size(max = 100, message = "食物名称长度不能超过100个字符")
    private String foodName;

    @ApiModelProperty(value = "食物分类", example = "水果")
    @Size(max = 50, message = "食物分类长度不能超过50个字符")
    private String foodCategory;

    @ApiModelProperty(value = "每100g热量（kcal）", required = true, example = "52.0")
    @NotNull(message = "热量不能为空")
    @DecimalMin(value = "0.0", message = "热量不能小于0")
    @DecimalMax(value = "9999.99", message = "热量不能超过9999.99")
    @Digits(integer = 6, fraction = 2, message = "热量格式不正确")
    private BigDecimal caloriesPer100g;

    @ApiModelProperty(value = "每100g蛋白质（g）", required = true, example = "0.3")
    @NotNull(message = "蛋白质含量不能为空")
    @DecimalMin(value = "0.0", message = "蛋白质含量不能小于0")
    @DecimalMax(value = "999.99", message = "蛋白质含量不能超过999.99")
    @Digits(integer = 5, fraction = 2, message = "蛋白质含量格式不正确")
    private BigDecimal proteinPer100g;

    @ApiModelProperty(value = "每100g碳水化合物（g）", required = true, example = "13.8")
    @NotNull(message = "碳水化合物含量不能为空")
    @DecimalMin(value = "0.0", message = "碳水化合物含量不能小于0")
    @DecimalMax(value = "999.99", message = "碳水化合物含量不能超过999.99")
    @Digits(integer = 5, fraction = 2, message = "碳水化合物含量格式不正确")
    private BigDecimal carbohydratePer100g;

    @ApiModelProperty(value = "每100g脂肪（g）", required = true, example = "0.2")
    @NotNull(message = "脂肪含量不能为空")
    @DecimalMin(value = "0.0", message = "脂肪含量不能小于0")
    @DecimalMax(value = "999.99", message = "脂肪含量不能超过999.99")
    @Digits(integer = 5, fraction = 2, message = "脂肪含量格式不正确")
    private BigDecimal fatPer100g;

    @ApiModelProperty(value = "每100g膳食纤维（g）", example = "2.4")
    @DecimalMin(value = "0.0", message = "膳食纤维含量不能小于0")
    @DecimalMax(value = "999.99", message = "膳食纤维含量不能超过999.99")
    @Digits(integer = 5, fraction = 2, message = "膳食纤维含量格式不正确")
    private BigDecimal fiberPer100g;

    @ApiModelProperty(value = "每100g维生素A（μg）", example = "3.0")
    @DecimalMin(value = "0.0", message = "维生素A含量不能小于0")
    @DecimalMax(value = "99999.99", message = "维生素A含量不能超过99999.99")
    @Digits(integer = 7, fraction = 2, message = "维生素A含量格式不正确")
    private BigDecimal vitaminAPer100g;

    @ApiModelProperty(value = "每100g维生素C（mg）", example = "4.0")
    @DecimalMin(value = "0.0", message = "维生素C含量不能小于0")
    @DecimalMax(value = "99999.99", message = "维生素C含量不能超过99999.99")
    @Digits(integer = 7, fraction = 2, message = "维生素C含量格式不正确")
    private BigDecimal vitaminCPer100g;

    @ApiModelProperty(value = "每100g钙（mg）", example = "6.0")
    @DecimalMin(value = "0.0", message = "钙含量不能小于0")
    @DecimalMax(value = "99999.99", message = "钙含量不能超过99999.99")
    @Digits(integer = 7, fraction = 2, message = "钙含量格式不正确")
    private BigDecimal calciumPer100g;

    @ApiModelProperty(value = "每100g铁（mg）", example = "0.1")
    @DecimalMin(value = "0.0", message = "铁含量不能小于0")
    @DecimalMax(value = "99999.99", message = "铁含量不能超过99999.99")
    @Digits(integer = 7, fraction = 2, message = "铁含量格式不正确")
    private BigDecimal ironPer100g;
}