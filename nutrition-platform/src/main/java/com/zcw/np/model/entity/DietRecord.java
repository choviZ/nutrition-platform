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
 * 饮食记录表
 * @TableName diet_record
 */
@TableName(value ="diet_record")
@Data
public class DietRecord implements Serializable {
    /**
     * 记录ID
     */
    @TableId(value = "record_id", type = IdType.ASSIGN_ID)
    private Long recordId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 记录日期
     */
    @TableField(value = "record_date")
    private Date recordDate;

    /**
     * 餐别：1-早餐，2-午餐，3-晚餐，4-加餐
     */
    @TableField(value = "meal_type")
    private Integer mealType;

    /**
     * 食物名称
     */
    @TableField(value = "food_name")
    private String foodName;

    /**
     * 食物分量（g）
     */
    @TableField(value = "food_amount")
    private BigDecimal foodAmount;

    /**
     * 热量（kcal）
     */
    @TableField(value = "calories")
    private BigDecimal calories;

    /**
     * 蛋白质（g）
     */
    @TableField(value = "protein")
    private BigDecimal protein;

    /**
     * 碳水化合物（g）
     */
    @TableField(value = "carbohydrate")
    private BigDecimal carbohydrate;

    /**
     * 脂肪（g）
     */
    @TableField(value = "fat")
    private BigDecimal fat;

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