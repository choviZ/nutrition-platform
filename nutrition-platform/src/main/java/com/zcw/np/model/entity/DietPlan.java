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
 * 饮食方案表
 * @TableName diet_plan
 */
@TableName(value ="diet_plan")
@Data
public class DietPlan implements Serializable {
    /**
     * 方案ID
     */
    @TableId(value = "plan_id", type = IdType.ASSIGN_ID)
    private Long planId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 方案日期
     */
    @TableField(value = "plan_date")
    private Date planDate;

    /**
     * 方案名称
     */
    @TableField(value = "plan_name")
    private String planName;

    /**
     * 早餐内容（JSON格式）
     */
    @TableField(value = "breakfast")
    private String breakfast;

    /**
     * 午餐内容（JSON格式）
     */
    @TableField(value = "lunch")
    private String lunch;

    /**
     * 晚餐内容（JSON格式）
     */
    @TableField(value = "dinner")
    private String dinner;

    /**
     * 加餐内容（JSON格式）
     */
    @TableField(value = "snacks")
    private String snacks;

    /**
     * 总热量
     */
    @TableField(value = "total_calories")
    private BigDecimal totalCalories;

    /**
     * 总蛋白质
     */
    @TableField(value = "total_protein")
    private BigDecimal totalProtein;

    /**
     * 总碳水化合物
     */
    @TableField(value = "total_carbohydrate")
    private BigDecimal totalCarbohydrate;

    /**
     * 总脂肪
     */
    @TableField(value = "total_fat")
    private BigDecimal totalFat;

    /**
     * 状态：1-草稿，2-已确认，3-已执行
     */
    @TableField(value = "status")
    private Integer status;

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