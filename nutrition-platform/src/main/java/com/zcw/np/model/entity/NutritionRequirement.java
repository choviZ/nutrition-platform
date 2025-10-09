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
 * 营养需求表
 * @TableName nutrition_requirement
 */
@TableName(value ="nutrition_requirement")
@Data
public class NutritionRequirement implements Serializable {
    /**
     * 需求ID
     */
    @TableId(value = "requirement_id", type = IdType.ASSIGN_ID)
    private Long requirementId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * BMI指数
     */
    @TableField(value = "bmi")
    private BigDecimal bmi;

    /**
     * BMI状态：0-偏瘦 1-正常，2-偏重，3-肥胖
     */
    @TableField(value = "bmi_status")
    private Integer bmiStatus;

    /**
     * 基础代谢率（BMR）
     */
    @TableField(value = "bmr")
    private BigDecimal bmr;

    /**
     * 评估日期
     */
    @TableField(value = "assessment_date")
    private Date assessmentDate;

    /**
     * 每日热量需求（kcal）
     */
    @TableField(value = "daily_calories")
    private BigDecimal dailyCalories;

    /**
     * 蛋白质需求（g）
     */
    @TableField(value = "protein")
    private BigDecimal protein;

    /**
     * 碳水化合物需求（g）
     */
    @TableField(value = "carbohydrate")
    private BigDecimal carbohydrate;

    /**
     * 脂肪需求（g）
     */
    @TableField(value = "fat")
    private BigDecimal fat;

    /**
     * 膳食纤维需求（g）
     */
    @TableField(value = "fiber")
    private BigDecimal fiber;

    /**
     * 维生素A需求（μg）
     */
    @TableField(value = "vitamin_a")
    private BigDecimal vitaminA;

    /**
     * 维生素C需求（mg）
     */
    @TableField(value = "vitamin_c")
    private BigDecimal vitaminC;

    /**
     * 钙需求（mg）
     */
    @TableField(value = "calcium")
    private BigDecimal calcium;

    /**
     * 铁需求（mg）
     */
    @TableField(value = "iron")
    private BigDecimal iron;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}