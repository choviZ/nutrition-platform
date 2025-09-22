package com.zcw.np.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcw.np.common.ErrorCode;
import com.zcw.np.constant.CommonConstant;
import com.zcw.np.exception.ThrowUtils;
import com.zcw.np.mapper.DietRecordMapper;
import com.zcw.np.model.dto.dietrecord.DietRecordAddRequest;
import com.zcw.np.model.dto.dietrecord.DietRecordQueryRequest;
import com.zcw.np.model.dto.dietrecord.DietRecordUpdateRequest;
import com.zcw.np.model.entity.DietRecord;
import com.zcw.np.model.vo.DietRecordStatisticsVO;
import com.zcw.np.model.vo.DietRecordVO;
import com.zcw.np.service.DietRecordService;
import com.zcw.np.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author zcw
* @description 针对表【diet_record(饮食记录表)】的数据库操作Service实现
* @createDate 2025-09-22 16:22:37
*/
@Service
@Slf4j
public class DietRecordServiceImpl extends ServiceImpl<DietRecordMapper, DietRecord>
    implements DietRecordService{

    @Override
    public Long addDietRecord(DietRecordAddRequest dietRecordAddRequest, Long userId) {
        ThrowUtils.throwIf(dietRecordAddRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");

        DietRecord dietRecord = new DietRecord();
        BeanUtils.copyProperties(dietRecordAddRequest, dietRecord);
        dietRecord.setUserId(userId);
        dietRecord.setCreateTime(LocalDateTime.now());
        dietRecord.setUpdateTime(LocalDateTime.now());

        boolean result = this.save(dietRecord);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "添加饮食记录失败");

        return dietRecord.getRecordId();
    }

    @Override
    public Boolean updateDietRecord(DietRecordUpdateRequest dietRecordUpdateRequest, Long userId) {
        ThrowUtils.throwIf(dietRecordUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(dietRecordUpdateRequest.getRecordId() == null, ErrorCode.PARAMS_ERROR, "记录ID不能为空");
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");

        // 检查记录是否存在且属于当前用户
        DietRecord existingRecord = this.getById(dietRecordUpdateRequest.getRecordId());
        ThrowUtils.throwIf(existingRecord == null, ErrorCode.NOT_FOUND_ERROR, "饮食记录不存在");
        ThrowUtils.throwIf(!existingRecord.getUserId().equals(userId), ErrorCode.NO_AUTH_ERROR, "无权限修改此记录");

        DietRecord dietRecord = new DietRecord();
        BeanUtils.copyProperties(dietRecordUpdateRequest, dietRecord);
        dietRecord.setUserId(userId);
        dietRecord.setUpdateTime(LocalDateTime.now());

        boolean result = this.updateById(dietRecord);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新饮食记录失败");

        return true;
    }

    @Override
    public Boolean deleteDietRecord(Long recordId, Long userId) {
        ThrowUtils.throwIf(recordId == null || recordId <= 0, ErrorCode.PARAMS_ERROR, "记录ID不能为空");
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");

        // 检查记录是否存在且属于当前用户
        DietRecord existingRecord = this.getById(recordId);
        ThrowUtils.throwIf(existingRecord == null, ErrorCode.NOT_FOUND_ERROR, "饮食记录不存在");
        ThrowUtils.throwIf(!existingRecord.getUserId().equals(userId), ErrorCode.NO_AUTH_ERROR, "无权限删除此记录");

        boolean result = this.removeById(recordId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除饮食记录失败");

        return true;
    }

    @Override
    public IPage<DietRecordVO> queryDietRecords(DietRecordQueryRequest dietRecordQueryRequest) {
        ThrowUtils.throwIf(dietRecordQueryRequest == null, ErrorCode.PARAMS_ERROR);

        long current = dietRecordQueryRequest.getCurrent();
        long size = dietRecordQueryRequest.getPageSize();

        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        // 查询数据库
        Page<DietRecord> dietRecordPage = this.page(new Page<>(current, size),
                this.getQueryWrapper(dietRecordQueryRequest));

        // 转换为VO
        Page<DietRecordVO> dietRecordVOPage = new Page<>(current, size, dietRecordPage.getTotal());
        List<DietRecordVO> dietRecordVOList = dietRecordPage.getRecords().stream()
                .map(this::getDietRecordVO)
                .collect(Collectors.toList());
        dietRecordVOPage.setRecords(dietRecordVOList);

        return dietRecordVOPage;
    }

    @Override
    public DietRecordVO getDietRecordById(Long recordId, Long userId) {
        ThrowUtils.throwIf(recordId == null || recordId <= 0, ErrorCode.PARAMS_ERROR, "记录ID不能为空");
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");

        DietRecord dietRecord = this.getById(recordId);
        ThrowUtils.throwIf(dietRecord == null, ErrorCode.NOT_FOUND_ERROR, "饮食记录不存在");
        ThrowUtils.throwIf(!dietRecord.getUserId().equals(userId), ErrorCode.NO_AUTH_ERROR, "无权限查看此记录");

        return this.getDietRecordVO(dietRecord);
    }

    @Override
    public List<DietRecordStatisticsVO> getDietRecordStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        ThrowUtils.throwIf(startDate == null, ErrorCode.PARAMS_ERROR, "开始日期不能为空");
        ThrowUtils.throwIf(endDate == null, ErrorCode.PARAMS_ERROR, "结束日期不能为空");
        ThrowUtils.throwIf(startDate.isAfter(endDate), ErrorCode.PARAMS_ERROR, "开始日期不能晚于结束日期");

        QueryWrapper<DietRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .ge("record_date", startDate)
                .le("record_date", endDate)
                .orderByAsc("record_date");

        List<DietRecord> dietRecords = this.list(queryWrapper);

        // 按日期分组统计
        Map<LocalDate, List<DietRecord>> recordsByDate = dietRecords.stream()
                .collect(Collectors.groupingBy(DietRecord::getRecordDate));

        List<DietRecordStatisticsVO> statisticsList = new ArrayList<>();
        for (Map.Entry<LocalDate, List<DietRecord>> entry : recordsByDate.entrySet()) {
            LocalDate date = entry.getKey();
            List<DietRecord> records = entry.getValue();

            DietRecordStatisticsVO statistics = new DietRecordStatisticsVO();
            statistics.setStatisticsDate(date);
            statistics.setRecordCount(records.size());

            // 计算总营养成分
            // 计算总营养成分
            BigDecimal totalCalories = BigDecimal.ZERO;
            BigDecimal totalProtein = BigDecimal.ZERO;
            BigDecimal totalFat = BigDecimal.ZERO;
            BigDecimal totalCarbohydrate = BigDecimal.ZERO;

            for (DietRecord record : records) {
                totalCalories = totalCalories.add(record.getCalories() != null ? record.getCalories() : BigDecimal.ZERO);
                totalProtein = totalProtein.add(record.getProtein() != null ? record.getProtein() : BigDecimal.ZERO);
                totalFat = totalFat.add(record.getFat() != null ? record.getFat() : BigDecimal.ZERO);
                totalCarbohydrate = totalCarbohydrate.add(record.getCarbohydrate() != null ? record.getCarbohydrate() : BigDecimal.ZERO);
            }

            // 按餐别统计
            Map<Integer, List<DietRecord>> recordsByMeal = records.stream()
                    .collect(Collectors.groupingBy(DietRecord::getMealType));

            List<DietRecordStatisticsVO.MealStatistics> mealStatisticsList = new ArrayList<>();
            for (Map.Entry<Integer, List<DietRecord>> mealEntry : recordsByMeal.entrySet()) {
                Integer mealType = mealEntry.getKey();
                List<DietRecord> mealRecords = mealEntry.getValue();

                BigDecimal mealCalories = mealRecords.stream()
                        .map(DietRecord::getCalories)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                DietRecordStatisticsVO.MealStatistics mealStats = new DietRecordStatisticsVO.MealStatistics();
                mealStats.setMealType(mealType);
                mealStats.setCalories(mealCalories);
                mealStats.setRecordCount(mealRecords.size());

                mealStatisticsList.add(mealStats);
            }

            statistics.setTotalCalories(totalCalories);
            statistics.setTotalProtein(totalProtein);
            statistics.setTotalFat(totalFat);
            statistics.setTotalCarbohydrate(totalCarbohydrate);
            statistics.setMealStatistics(mealStatisticsList);

            statisticsList.add(statistics);
        }

        return statisticsList;
    }

    @Override
    public List<DietRecordVO> getDietRecordsByDate(Long userId, LocalDate recordDate) {
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        ThrowUtils.throwIf(recordDate == null, ErrorCode.PARAMS_ERROR, "记录日期不能为空");

        QueryWrapper<DietRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("record_date", recordDate)
                .orderByAsc("meal_type", "create_time");

        List<DietRecord> dietRecords = this.list(queryWrapper);
        return dietRecords.stream()
                .map(this::getDietRecordVO)
                .collect(Collectors.toList());
    }

    /**
     * 获取查询条件
     *
     * @param dietRecordQueryRequest 查询请求
     * @return 查询条件
     */
    private QueryWrapper<DietRecord> getQueryWrapper(DietRecordQueryRequest dietRecordQueryRequest) {
        QueryWrapper<DietRecord> queryWrapper = new QueryWrapper<>();
        if (dietRecordQueryRequest == null) {
            return queryWrapper;
        }

        Long userId = dietRecordQueryRequest.getUserId();
        LocalDate startDate = dietRecordQueryRequest.getStartDate();
        LocalDate endDate = dietRecordQueryRequest.getEndDate();
        Integer mealType = dietRecordQueryRequest.getMealType();
        String foodName = dietRecordQueryRequest.getFoodName();
        String sortField = dietRecordQueryRequest.getSortField();
        String sortOrder = dietRecordQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(userId != null, "user_id", userId);
        queryWrapper.ge(startDate != null, "record_date", startDate);
        queryWrapper.le(endDate != null, "record_date", endDate);
        queryWrapper.eq(mealType != null, "meal_type", mealType);
        queryWrapper.like(StringUtils.isNotBlank(foodName), "food_name", foodName);

        // 排序
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);

        return queryWrapper;
    }

    /**
     * 获取饮食记录视图对象
     *
     * @param dietRecord 饮食记录实体
     * @return 饮食记录视图对象
     */
    private DietRecordVO getDietRecordVO(DietRecord dietRecord) {
        if (dietRecord == null) {
            return null;
        }
        DietRecordVO dietRecordVO = new DietRecordVO();
        BeanUtils.copyProperties(dietRecord, dietRecordVO);
        return dietRecordVO;
    }
}




