package com.zcw.np.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcw.np.model.entity.NutritionRequirement;
import com.zcw.np.model.dto.nutrition.NutritionAssessmentRequest;
import com.zcw.np.model.dto.nutrition.NutritionAssessmentResponse;
import com.zcw.np.model.dto.nutrition.NutritionQueryRequest;

import java.util.List;

/**
* @author zcw
* @description 针对表【nutrition_requirement(营养需求表)】的数据库操作Service
* @createDate 2025-09-22 15:22:42
*/
public interface NutritionRequirementService extends IService<NutritionRequirement> {

    /**
     * 营养需求评估
     * @param request 评估请求参数
     * @return 评估结果
     */
    NutritionAssessmentResponse assessNutritionRequirement(NutritionAssessmentRequest request);

    /**
     * 查询营养需求记录
     * @param request 查询条件
     * @return 营养需求记录列表
     */
    List<NutritionAssessmentResponse> queryNutritionRequirements(NutritionQueryRequest request);
}
