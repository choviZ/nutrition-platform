package com.zcw.np.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcw.np.common.ErrorCode;
import com.zcw.np.exception.BusinessException;
import com.zcw.np.exception.ThrowUtils;
import com.zcw.np.model.dto.health.HealthProfileCreateRequest;
import com.zcw.np.model.dto.health.HealthProfileUpdateRequest;
import com.zcw.np.model.entity.UserHealthProfile;
import com.zcw.np.model.vo.HealthProfileVO;
import com.zcw.np.service.UserHealthProfileService;
import com.zcw.np.mapper.UserHealthProfileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
* @author zcw
* @description 针对表【user_health_profile(用户健康档案表)】的数据库操作Service实现
* @createDate 2025-09-22 09:14:28
*/
@Service
@Slf4j
public class UserHealthProfileServiceImpl extends ServiceImpl<UserHealthProfileMapper, UserHealthProfile>
    implements UserHealthProfileService{

    /**
     * 活动水平描述映射
     */
    private static final Map<Integer, String> ACTIVITY_LEVEL_DESC_MAP = new HashMap<>();

    static {
        ACTIVITY_LEVEL_DESC_MAP.put(1, "久坐");
        ACTIVITY_LEVEL_DESC_MAP.put(2, "轻度活动");
        ACTIVITY_LEVEL_DESC_MAP.put(3, "中度活动");
        ACTIVITY_LEVEL_DESC_MAP.put(4, "重度活动");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HealthProfileVO createHealthProfile(HealthProfileCreateRequest healthProfileCreateRequest, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(healthProfileCreateRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");

        // 检查用户是否已有健康档案
        ThrowUtils.throwIf(hasHealthProfile(userId), ErrorCode.OPERATION_ERROR, "用户已存在健康档案，请使用更新功能");

        // 创建健康档案实体
        UserHealthProfile healthProfile = new UserHealthProfile();
        BeanUtils.copyProperties(healthProfileCreateRequest, healthProfile);
        healthProfile.setUserId(userId);
        healthProfile.setCreateTime(LocalDateTime.now());
        healthProfile.setUpdateTime(LocalDateTime.now());

        // 保存到数据库
        boolean result = this.save(healthProfile);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建健康档案失败");

        log.info("用户 {} 创建健康档案成功", userId);
        return convertToVO(healthProfile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HealthProfileVO updateHealthProfile(HealthProfileUpdateRequest healthProfileUpdateRequest, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(healthProfileUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        ThrowUtils.throwIf(healthProfileUpdateRequest.getProfileId() == null, ErrorCode.PARAMS_ERROR, "档案ID不能为空");

        // 查询现有健康档案
        UserHealthProfile existingProfile = this.getById(healthProfileUpdateRequest.getProfileId());
        ThrowUtils.throwIf(existingProfile == null, ErrorCode.NOT_FOUND_ERROR, "健康档案不存在");
        ThrowUtils.throwIf(!existingProfile.getUserId().equals(userId), ErrorCode.NO_AUTH_ERROR, "无权限修改此健康档案");

        // 更新健康档案
        BeanUtils.copyProperties(healthProfileUpdateRequest, existingProfile);
        existingProfile.setUpdateTime(LocalDateTime.now());

        // 保存到数据库
        boolean result = this.updateById(existingProfile);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新健康档案失败");

        log.info("用户 {} 更新健康档案成功", userId);
        return convertToVO(existingProfile);
    }

    @Override
    public HealthProfileVO getHealthProfileByUserId(Long userId) {
        // 参数校验
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");

        // 查询健康档案
        QueryWrapper<UserHealthProfile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserHealthProfile healthProfile = this.getOne(queryWrapper);

        if (healthProfile == null) {
            return null;
        }

        return convertToVO(healthProfile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteHealthProfile(Long userId) {
        // 参数校验
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");

        // 查询健康档案
        QueryWrapper<UserHealthProfile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserHealthProfile healthProfile = this.getOne(queryWrapper);
        
        if (healthProfile == null) {
            return true; // 档案不存在，视为删除成功
        }

        // 删除健康档案
        boolean result = this.removeById(healthProfile.getProfileId());
        if (result) {
            log.info("用户 {} 删除健康档案成功", userId);
        }
        
        return result;
    }

    @Override
    public boolean hasHealthProfile(Long userId) {
        // 参数校验
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");

        // 查询健康档案数量
        QueryWrapper<UserHealthProfile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        long count = this.count(queryWrapper);
        
        return count > 0;
    }

    @Override
    public String getActivityLevelDesc(Integer activityLevel) {
        if (activityLevel == null) {
            return "";
        }
        return ACTIVITY_LEVEL_DESC_MAP.getOrDefault(activityLevel, "未知");
    }

    /**
     * 将实体转换为VO
     *
     * @param healthProfile 健康档案实体
     * @return 健康档案VO
     */
    private HealthProfileVO convertToVO(UserHealthProfile healthProfile) {
        if (healthProfile == null) {
            return null;
        }

        HealthProfileVO healthProfileVO = new HealthProfileVO();
        BeanUtils.copyProperties(healthProfile, healthProfileVO);
        
        // 设置活动水平描述
        healthProfileVO.setActivityLevelDesc(getActivityLevelDesc(healthProfile.getActivityLevel()));
        
        return healthProfileVO;
    }
}




