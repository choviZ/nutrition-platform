package com.zcw.np.model.dto.nutrition;

import com.zcw.np.common.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 营养需求查询请求DTO
 *
 * @author zcw
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "营养需求查询请求")
public class NutritionQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;

    @ApiModelProperty(value = "评估开始日期", example = "2024-01-01")
    private Date startDate;

    @ApiModelProperty(value = "评估结束日期", example = "2024-12-31")
    private Date endDate;

    @ApiModelProperty(value = "最小热量需求", example = "1500.0")
    private Double minCalories;

    @ApiModelProperty(value = "最大热量需求", example = "3000.0")
    private Double maxCalories;
}