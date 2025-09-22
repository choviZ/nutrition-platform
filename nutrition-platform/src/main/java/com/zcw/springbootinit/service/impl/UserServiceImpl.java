package com.zcw.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcw.springbootinit.common.ErrorCode;
import com.zcw.springbootinit.constant.CommonConstant;
import com.zcw.springbootinit.exception.BusinessException;
import com.zcw.springbootinit.exception.ThrowUtils;
import com.zcw.springbootinit.mapper.UserMapper;
import com.zcw.springbootinit.model.dto.user.UserQueryRequest;
import com.zcw.springbootinit.model.entity.User;
import com.zcw.springbootinit.model.vo.UserVO;
import com.zcw.springbootinit.service.UserService;
import com.zcw.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author zcw
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-09-22 09:14:28
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "zcw_nutrition";

    @Override
    public void validUser(User user, boolean add) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        String phone = user.getPhone();
        String userRole = user.getUserRole();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(username, password, userRole), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(username) && username.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名过长");
        }
        if (StringUtils.isNotBlank(password) && password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (StringUtils.isNotBlank(email) && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式不正确");
        }
        if (StringUtils.isNotBlank(phone) && !phone.matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号格式不正确");
        }
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (userQueryRequest == null) {
            return queryWrapper;
        }
        Long userId = userQueryRequest.getUserId();
        String username = userQueryRequest.getUsername();
        String email = userQueryRequest.getEmail();
        String phone = userQueryRequest.getPhone();
        String realName = userQueryRequest.getRealName();
        Integer gender = userQueryRequest.getGender();
        Integer minAge = userQueryRequest.getMinAge();
        Integer maxAge = userQueryRequest.getMaxAge();
        Integer healthGoal = userQueryRequest.getHealthGoal();
        String userRole = userQueryRequest.getUserRole();
        Integer status = userQueryRequest.getStatus();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        queryWrapper.eq(userId != null, "user_id", userId);
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        queryWrapper.like(StringUtils.isNotBlank(email), "email", email);
        queryWrapper.like(StringUtils.isNotBlank(phone), "phone", phone);
        queryWrapper.like(StringUtils.isNotBlank(realName), "real_name", realName);
        queryWrapper.eq(gender != null, "gender", gender);
        queryWrapper.ge(minAge != null, "age", minAge);
        queryWrapper.le(maxAge != null, "age", maxAge);
        queryWrapper.eq(healthGoal != null, "health_goal", healthGoal);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "user_role", userRole);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        // 设置性别描述
        if (user.getGender() != null) {
            userVO.setGenderDesc(user.getGender() == 0 ? "女" : "男");
        }
        
        // 设置健康目标描述
        if (user.getHealthGoal() != null) {
            switch (user.getHealthGoal()) {
                case 0:
                    userVO.setHealthGoalDesc("减重");
                    break;
                case 1:
                    userVO.setHealthGoalDesc("增重");
                    break;
                case 2:
                    userVO.setHealthGoalDesc("维持");
                    break;
                default:
                    userVO.setHealthGoalDesc("未知");
            }
        }
        
        // 设置状态描述
        if (user.getStatus() != null) {
            userVO.setStatusDesc(user.getStatus() == 0 ? "禁用" : "启用");
        }
        
        return userVO;
    }

    @Override
    public Page<UserVO> getUserVOPage(Page<User> userPage) {
        List<User> userList = userPage.getRecords();
        Page<UserVO> userVOPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        if (userList.isEmpty()) {
            return userVOPage;
        }
        // 填充信息
        List<UserVO> userVOList = userList.stream().map(this::getUserVO).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);
        return userVOPage;
    }

    /**
     * 加密密码
     *
     * @param userPassword 原密码
     * @return 加密后的密码
     */
    public String getEncryptPassword(String userPassword) {
        // 盐值 + 密码 + 盐值
        return DigestUtils.md5DigestAsHex((SALT + userPassword + SALT).getBytes());
    }

}




