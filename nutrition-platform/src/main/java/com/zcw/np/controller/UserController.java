package com.zcw.np.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcw.np.annotation.AuthCheck;
import com.zcw.np.common.BaseResponse;
import com.zcw.np.common.DeleteRequest;
import com.zcw.np.common.ErrorCode;
import com.zcw.np.common.ResultUtils;
import com.zcw.np.constant.UserConstant;
import com.zcw.np.exception.BusinessException;
import com.zcw.np.exception.ThrowUtils;
import com.zcw.np.model.dto.user.UserAddRequest;
import com.zcw.np.model.dto.user.UserQueryRequest;
import com.zcw.np.model.dto.user.UserUpdateRequest;
import com.zcw.np.model.entity.User;
import com.zcw.np.model.vo.UserVO;
import com.zcw.np.service.UserService;
import com.zcw.np.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户接口
 *
 * @author zcw
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserServiceImpl userServiceImpl;

    // region 增删改查

    /**
     * 创建用户
     *
     * @param userAddRequest 用户创建请求
     * @return 新用户id
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody @Valid UserAddRequest userAddRequest) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        
        // 设置默认值
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(1); // 默认启用

        // 密码加密
        String encryptPassword = userServiceImpl.getEncryptPassword(userAddRequest.getPassword());
        user.setPassword(encryptPassword);
        
        // 校验参数
        userService.validUser(user, true);
        
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        long count = userService.count(queryWrapper);
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "用户名已存在");
        
        // 检查邮箱是否已存在
        if (user.getEmail() != null) {
            QueryWrapper<User> emailQueryWrapper = new QueryWrapper<>();
            emailQueryWrapper.eq("email", user.getEmail());
            long emailCount = userService.count(emailQueryWrapper);
            ThrowUtils.throwIf(emailCount > 0, ErrorCode.PARAMS_ERROR, "邮箱已存在");
        }
        
        // 检查手机号是否已存在
        if (user.getPhone() != null) {
            QueryWrapper<User> phoneQueryWrapper = new QueryWrapper<>();
            phoneQueryWrapper.eq("phone", user.getPhone());
            long phoneCount = userService.count(phoneQueryWrapper);
            ThrowUtils.throwIf(phoneCount > 0, ErrorCode.PARAMS_ERROR, "手机号已存在");
        }
        
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getUserId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest 删除请求
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody @Valid DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        
        // 判断是否存在
        User oldUser = userService.getById(id);
        ThrowUtils.throwIf(oldUser == null, ErrorCode.NOT_FOUND_ERROR);
        
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        
        // 获取当前用户ID和角色
        Long currentUserId = getCurrentUserId(authentication);
        String currentUserRole = getCurrentUserRole(authentication);
        
        // 如果无法获取用户信息，说明token可能有问题，但用户认为自己已登录
        if (currentUserId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "登录状态异常，请重新登录");
        }
        
        // 权限验证：只有本人或管理员可以删除
        boolean isAdmin = UserConstant.ADMIN_ROLE.equals(currentUserRole);
        boolean isSelf = currentUserId.equals(id);
        
        if (!isAdmin && !isSelf) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "只有本人或管理员可以删除用户");
        }
        
        // 逻辑删除
        boolean result = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest 用户更新请求
     * @return 是否更新成功
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE )
    public BaseResponse<Boolean> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getUserId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        user.setUpdateTime(LocalDateTime.now());
        
        // 参数校验
        userService.validUser(user, false);
        long id = userUpdateRequest.getUserId();
        // 获取当前用户ID和角色
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = getCurrentUserId(authentication);
        String currentUserRole = getCurrentUserRole(authentication);
        // 权限验证：只有本人或管理员可以删除
        boolean isAdmin = UserConstant.ADMIN_ROLE.equals(currentUserRole);
        boolean isSelf = currentUserId.equals(id);

        if (!isAdmin && !isSelf) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "只有本人或管理员可以删除用户");
        }
        // 判断是否存在
        User oldUser = userService.getById(id);
        ThrowUtils.throwIf(oldUser == null, ErrorCode.NOT_FOUND_ERROR);
        
        // 检查用户名是否已被其他用户使用
        if (user.getUsername() != null) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", user.getUsername());
            queryWrapper.ne("user_id", id);
            long count = userService.count(queryWrapper);
            ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "用户名已存在");
        }
        
        // 检查邮箱是否已被其他用户使用
        if (user.getEmail() != null) {
            QueryWrapper<User> emailQueryWrapper = new QueryWrapper<>();
            emailQueryWrapper.eq("email", user.getEmail());
            emailQueryWrapper.ne("user_id", id);
            long emailCount = userService.count(emailQueryWrapper);
            ThrowUtils.throwIf(emailCount > 0, ErrorCode.PARAMS_ERROR, "邮箱已存在");
        }
        
        // 检查手机号是否已被其他用户使用
        if (user.getPhone() != null) {
            QueryWrapper<User> phoneQueryWrapper = new QueryWrapper<>();
            phoneQueryWrapper.eq("phone", user.getPhone());
            phoneQueryWrapper.ne("user_id", id);
            long phoneCount = userService.count(phoneQueryWrapper);
            ThrowUtils.throwIf(phoneCount > 0, ErrorCode.PARAMS_ERROR, "手机号已存在");
        }
        
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取用户
     *
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/get")
    public BaseResponse<UserVO> getUserById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表
     *
     * @param userQueryRequest 查询请求
     * @return 用户列表
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<UserVO>> listUserByPage(@RequestBody @Valid UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userService.getUserVOPage(userPage));
    }

    // endregion

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId(Authentication authentication) {
        Object details = authentication.getDetails();
        if (details instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> detailsMap = (Map<String, Object>) details;
            return (Long) detailsMap.get("userId");
        }
        return null;
    }

    /**
     * 获取当前用户角色
     */
    private String getCurrentUserRole(Authentication authentication) {
        Object details = authentication.getDetails();
        if (details instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> detailsMap = (Map<String, Object>) details;
            return (String) detailsMap.get("userRole");
        }
        return null;
    }
}