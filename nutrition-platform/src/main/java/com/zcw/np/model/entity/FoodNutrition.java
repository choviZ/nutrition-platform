package com.zcw.np.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 食物营养数据表
 * @TableName food_nutrition
 */
@TableName(value ="food_nutrition")
@Data
public class FoodNutrition implements Serializable {
    /**
     * 食物ID
     */
    @TableId(value = "food_id", type = IdType.ASSIGN_ID)
    private Long foodId;

    /**
     * 食物名称
     */
    @TableField(value = "food_name")
    private String foodName;

    /**
     * 食物分类
     */
    @TableField(value = "food_category")
    private String foodCategory;

    /**
     * 每100g热量（kcal）
     */
    @TableField(value = "calories_per_100g")
    private BigDecimal caloriesPer100g;

    /**
     * 每100g蛋白质（g）
     */
    @TableField(value = "protein_per_100g")
    private BigDecimal proteinPer100g;

    /**
     * 每100g碳水化合物（g）
     */
    @TableField(value = "carbohydrate_per_100g")
    private BigDecimal carbohydratePer100g;

    /**
     * 每100g脂肪（g）
     */
    @TableField(value = "fat_per_100g")
    private BigDecimal fatPer100g;

    /**
     * 每100g膳食纤维（g）
     */
    @TableField(value = "fiber_per_100g")
    private BigDecimal fiberPer100g;

    /**
     * 每100g维生素A（μg）
     */
    @TableField(value = "vitamin_a_per_100g")
    private BigDecimal vitaminAPer100g;

    /**
     * 每100g维生素C（mg）
     */
    @TableField(value = "vitamin_c_per_100g")
    private BigDecimal vitaminCPer100g;

    /**
     * 每100g钙（mg）
     */
    @TableField(value = "calcium_per_100g")
    private BigDecimal calciumPer100g;

    /**
     * 每100g铁（mg）
     */
    @TableField(value = "iron_per_100g")
    private BigDecimal ironPer100g;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}